package edu.ncsu.csc.itrust.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.beans.OperationalProfile;
import edu.ncsu.csc.itrust.beans.TransactionBean;
import edu.ncsu.csc.itrust.beans.loaders.OperationalProfileLoader;
import edu.ncsu.csc.itrust.beans.loaders.TransactionBeanLoader;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.enums.TransactionType;
import edu.ncsu.csc.itrust.exception.DBException;

/**
 * Used for the logging mechanism.
 * 
 * DAO stands for Database Access Object. All DAOs are intended to be
 * reflections of the database, that is, one DAO per table in the database (most
 * of the time). For more complex sets of queries, extra DAOs are added. DAOs
 * can assume that all data has been validated and is correct.
 * 
 * DAOs should never have setters or any other parameter to the constructor than
 * a factory. All DAOs should be accessed by DAOFactory (@see {@link DAOFactory}
 * ) and every DAO should have a factory - for obtaining JDBC connections and/or
 * accessing other DAOs.
 * 
 * 
 * 
 */
//Cite from T817 for log query implementation
@SuppressWarnings("unused")
public class TransactionDAO {
	private DAOFactory factory;
	private TransactionBeanLoader loader = new TransactionBeanLoader();
	private OperationalProfileLoader operationalProfileLoader = new OperationalProfileLoader();

	/**
	 * The typical constructor.
	 * 
	 * @param factory
	 *            The {@link DAOFactory} associated with this DAO, which is used
	 *            for obtaining SQL connections, etc.
	 */
	public TransactionDAO(DAOFactory factory) {
		this.factory = factory;
	}

	/**
	 * Returns the whole transaction log
	 * 
	 * @return
	 * @throws DBException
	 */
	public List<TransactionBean> getAllTransactions() throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn
					.prepareStatement("SELECT * FROM transactionlog ORDER BY timeLogged DESC");
			ResultSet rs = ps.executeQuery();
			List<TransactionBean> loadlist = loader.loadList(rs);
			rs.close();
			ps.close();
			return loadlist;
		} catch (SQLException e) {

			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}
	/**
	 * get transactions by the specific role info (added!!)
	 * @param role
	 * @param transactionCode
	 * @param startDate
	 * @param endDate
	 * @param loggedInUser
	 * @return
	 * @throws DBException
	 * @throws SQLException
	 */
	public HashMap<String, Long> getLogForRole(String role, String transactionCode,
			java.util.Date startD, java.util.Date endD, boolean loggedInUser)
			throws DBException, SQLException {
		Connection conn = null;
		PreparedStatement ps = null;

		try {
			conn = factory.getConnection();

			String update;
			if (loggedInUser) {
				update = "CREATE OR REPLACE view table3 as SELECT * FROM transactionlog WHERE (loggedInMID IN (select MID from users where role LIKE ?)) AND transactionCode LIKE ? AND timeLogged >= ? AND timeLogged <= ?;";
			} else {
				update = "CREATE OR REPLACE view table3 as SELECT * FROM transactionlog WHERE (secondaryMID IN (select MID from users where role LIKE ?)) AND transactionCode LIKE ? AND timeLogged >= ? AND timeLogged <= ?;";
			}
			
			ps = conn
					.prepareStatement(update);

			ps.setString(1, role);
			ps.setString(2, transactionCode);
			ps.setTimestamp(3, new Timestamp(startD.getTime()));
			ps.setTimestamp(4, new Timestamp(endD.getTime() + 1000L * 60L
					* 60 * 24L));
			System.out.println(ps);
			ps.executeUpdate();
			
			String query;
			if (loggedInUser) {
				query = "SELECT role, count(*) AS count FROM table3,users WHERE table3.loggedInMID=users.MID GROUP BY users.role;";
			} else {
				query = "SELECT role, count(*) AS count FROM table3,users WHERE table3.secondaryMID=users.MID GROUP BY users.role;";
			}
			
			ps = conn
					.prepareStatement(query);
			
			HashMap<String, Long> map = new HashMap<String, Long>();
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				map.put(rs.getString("role"), rs.getLong("count"));
			}
			
			
			ps.close();
			rs.close();

			 
			
			return map;
			
		}  
		catch (Exception e) {
		      e.printStackTrace();
		    } finally {
			DBUtil.closeConnection(conn, ps);
		}
		return null;
	}
	
	
	

	/**
	 * query the database with month and year(added !!)
	 * @param role1
	 * @param role2
	 * @param tranType
	 * @param startD
	 * @param endD
	 * @return
	 * @throws DBException
	 * @throws SQLException
	 */
	public HashMap<String, Long> getLogByTime(String role1, String role2, String tranType,
			java.util.Date startD, java.util.Date endD)
			throws DBException, SQLException {
		
		Connection conn = null;
		PreparedStatement ps = null;

		try {
			conn = factory.getConnection();
			
			String update = "CREATE OR REPLACE view table3 as SELECT * FROM transactionlog WHERE (loggedInMID IN (select MID from users where role LIKE ?)) AND (secondaryMID IN (select MID from users where role LIKE ?)) AND transactionCode LIKE ? AND timeLogged >= ? AND timeLogged <= ?;";

			ps = conn
					.prepareStatement(update);

			ps.setTimestamp(4, new Timestamp(startD.getTime()));
			ps.setTimestamp(5, new Timestamp(endD.getTime() + 1000L * 60L
					* 60 * 24L));
			ps.setString(1, role1);
			ps.setString(2, role2);
			ps.setString(3, tranType);
			
			System.out.println(ps);
			ps.executeUpdate();
			
			String query = "SELECT count(*) AS count, YEAR(timeLogged) AS year, MONTH(timeLogged) AS month FROM table3 GROUP BY YEAR(timeLogged), MONTH(timeLogged)";
			
			ps = conn
					.prepareStatement(query);
			
			

			HashMap<String, Long> map = new HashMap<String, Long>();
			
			ResultSet rs = ps.executeQuery();
//			if(rs.getFetchSize() < 1){
//				ps.close();
//				rs.close();
//				ps = conn.prepareStatement("SELECT * FROM transactionlog WHERE timeLogged >= ? AND timeLogged <= ?");
//				ps.setTimestamp(1, new Timestamp(startD.getTime()));
//				ps.setTimestamp(2, new Timestamp(endD.getTime() + 1000L * 60L
//						* 60 * 24L));
//				rs = ps.executeQuery();
//				
//			}
			
			
			while (rs.next()) {
				
				//String dateString = rs.getString("month") + "_" + rs.getString("year");
				//System.out.println(dateString);
				//map.put(dateString, rs.getLong("count"));
				System.out.println("look at it!");
				System.out.println(rs.toString());
			}
		
			ps.close();
			rs.close();

			 
			
			return map;
			
		}  
		catch (Exception e) {
		      e.printStackTrace();
		    } finally {
			DBUtil.closeConnection(conn, ps);
		}
		return null;
	}
	/**
	 * query the transaction log by given transaction type(!!added)
	 * @param role1
	 * @param role2
	 * @param tranType
	 * @param startD
	 * @param endD
	 * @return
	 * @throws DBException
	 * @throws SQLException
	 */
	
	public HashMap<String, Long> getLogByType(String role1, String role2, String tranType,
			java.util.Date startD, java.util.Date endD)
			throws DBException, SQLException {
		
		Connection conn = null;
		PreparedStatement ps = null;
		System.out.println(role1);
		System.out.println(role2);
		System.out.println(tranType);
		

		try {
			conn = factory.getConnection();

			ps = conn
					.prepareStatement("CREATE OR REPLACE view table3 as SELECT * FROM transactionlog WHERE (loggedInMID IN (select MID from users where role LIKE ?)) AND (secondaryMID IN (select MID from users where role LIKE ?)) AND transactionCode LIKE ? AND timeLogged >= ? AND timeLogged <= ?;");
			ps.setTimestamp(4, new Timestamp(startD.getTime()));
			ps.setTimestamp(5, new Timestamp(endD.getTime() + 1000L * 60L
					* 60 * 24L));
			ps.setString(1, role1);
			ps.setString(2, role2);
			ps.setString(3, tranType);

			//System.out.println(ps);
			ps.executeUpdate();
			
			ps = conn
					.prepareStatement("SELECT count(*) AS count, transactionCode FROM table3 GROUP BY transactionCode");
			
			ResultSet rs = ps.executeQuery();

			HashMap<String, Long> map = new HashMap<String, Long>();
			
			while (rs.next()) {
				System.out.println(rs.getString("transactionCode"));
				map.put(rs.getString("transactionCode"), rs.getLong("count"));
			}
		
			ps.close();
			rs.close();


			return map;
			
		}  
		catch (Exception e) {
		      //e.printStackTrace();
		    } finally {
			DBUtil.closeConnection(conn, ps);
		}
		return null;
	}
	
	

	/**
	 * Log a transaction, just giving it the person who is logged in and the
	 * type
	 * 
	 * @param type
	 *            The {@link TransactionType} enum representing the type this
	 *            transaction is.
	 * @param loggedInMID
	 *            The MID of the user who is logged in.
	 * @throws DBException
	 */
	public void logTransaction(TransactionType type, long loggedInMID)
			throws DBException {
		logTransaction(type, loggedInMID, 0L, "");
	}

	/**
	 * Log a transaction, with all of the info. The meaning of secondaryMID and
	 * addedInfo changes depending on the transaction type.
	 * 
	 * @param type
	 *            The {@link TransactionType} enum representing the type this
	 *            transaction is.
	 * @param loggedInMID
	 *            The MID of the user who is logged in.
	 * @param secondaryMID
	 *            Typically, the MID of the user who is being acted upon.
	 * @param addedInfo
	 *            A note about a subtransaction, or specifics of this
	 *            transaction (for posterity).
	 * @throws DBException
	 */
	public void logTransaction(TransactionType type, long loggedInMID,
			long secondaryMID, String addedInfo) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn
					.prepareStatement("INSERT INTO transactionlog(loggedInMID, secondaryMID, "
							+ "transactionCode, addedInfo) VALUES(?,?,?,?)");
			ps.setLong(1, loggedInMID);
			ps.setLong(2, secondaryMID);
			ps.setInt(3, type.getCode());
			ps.setString(4, addedInfo);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {

			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	/**
	 * Return a list of all transactions in which an HCP accessed the given
	 * patient's record
	 * 
	 * @param patientID
	 *            The MID of the patient in question.
	 * @return A java.util.List of transactions.
	 * @throws DBException
	 */
	public List<TransactionBean> getAllRecordAccesses(long patientID,
			long dlhcpID, boolean getByRole) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;

		try {
			conn = factory.getConnection();
			ps = conn
					.prepareStatement("SELECT * FROM transactionlog WHERE secondaryMID=? AND transactionCode "
							+ "IN("
							+ TransactionType.patientViewableStr
							+ ") AND loggedInMID!=? ORDER BY timeLogged DESC");
			ps.setLong(1, patientID);
			ps.setLong(2, dlhcpID);
			ResultSet rs = ps.executeQuery();
			List<TransactionBean> tbList = loader.loadList(rs);

			tbList = addAndSortRoles(tbList, patientID, getByRole);

			rs.close();
			ps.close();
			return tbList;
		} catch (SQLException e) {

			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}
	/**
	 * Get transaction log by specific fields(added!!) 
	 * @param logR
	 * @param secondR
	 * @param tranType
	 * @param startD
	 * @param endD
	 * @return
	 * @throws DBException
	 */
	public List<TransactionBean> getLog(String logR,
			String secondR, String tranType,
			java.util.Date startD, java.util.Date endD)
			throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;

		try {
			conn = factory.getConnection();
			ps = conn
					.prepareStatement("SELECT * FROM transactionlog WHERE (loggedInMID IN (select MID from users where role LIKE ?)) AND (secondaryMID IN (select MID from users where role LIKE ? or MID = '0') OR secondaryMID = '0')  AND transactionCode LIKE ? AND timeLogged >= ? AND timeLogged <= ? ORDER BY timeLogged DESC");

			ps.setTimestamp(4, new Timestamp(startD.getTime()));
			ps.setTimestamp(5, new Timestamp(endD.getTime() + 1000L * 60L
					* 60 * 24L));
			ps.setString(1, logR);
			ps.setString(2, secondR);
			ps.setString(3, tranType);
			
//			System.out.println("The logRole is " + secondR);
//			ps = conn.prepareStatement("SELECT * FROM transactionlog WHERE loggedInMID IN (select MID from users where role LIKE ?) ");
//			ps.setString(1, secondR);
			
			ResultSet rs = ps.executeQuery();
			System.out.println("rs size is " + rs.getFetchSize());
//			ps.close();
//			rs.close();
//			ps = conn.prepareStatement("select MID from users where role LIKE ?");
//			ps.setString(1, logR);
//			rs = ps.executeQuery();
//			System.out.println("rs's mid size is " + rs.getFetchSize());
//
//			while (rs.next()) {
//				System.out.println("we have lines!");
//			}
			
//			if(rs.getFetchSize() < 1){
//				ps.close();
//				rs.close();
//				ps = conn.prepareStatement("SELECT * FROM transactionlog WHERE timeLogged >= ? AND timeLogged <= ?");
//				ps.setTimestamp(1, new Timestamp(startD.getTime()));
//				ps.setTimestamp(2, new Timestamp(endD.getTime() + 1000L * 60L
//						* 60 * 24L));
//				rs = ps.executeQuery();
//				
//			}
			List<TransactionBean> logList = loader.loadList(rs);
			rs.close();
			ps.close();
			return logList;
		} catch (SQLException e) {
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	/**
	 * The Most Thorough Fetch
	 * 
	 * @param mid
	 *            MID of the logged in user
	 * @param dlhcpID
	 *            MID of the user's DLHCP
	 * @param start
	 *            Index to start pulling entries from
	 * @param range
	 *            Number of entries to retrieve
	 * @return List of <range> TransactionBeans affecting the user starting from
	 *         the <start>th entry
	 * @throws DBException
	 */
	public List<TransactionBean> getTransactionsAffecting(long mid,
			long dlhcpID, java.util.Date start, int range) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;

		try {

			conn = factory.getConnection();
			ps = conn
					.prepareStatement("SELECT * FROM transactionlog WHERE ((timeLogged <= ?) "
							+ "AND  (secondaryMID=? AND transactionCode "
							+ "IN ("
							+ TransactionType.patientViewableStr
							+ ")) "
							+ "OR (loggedInMID=? AND transactionCode=?) ) "
							+ "AND NOT (loggedInMID=? AND transactionCode IN ("
							+ // exclude if DLHCP as specified in UC43
							TransactionType.dlhcpHiddenStr
							+ ")) "
							+ "ORDER BY timeLogged DESC LIMIT 0,?");
			ps.setString(2, mid + "");
			ps.setString(3, mid + "");
			ps.setInt(4, TransactionType.LOGIN_SUCCESS.getCode());
			ps.setTimestamp(1, new Timestamp(start.getTime()));
			ps.setLong(5, dlhcpID);
			ps.setInt(6, range);
			ResultSet rs = ps.executeQuery();
			List<TransactionBean> tbList = loader.loadList(rs);
			rs.close();
			ps.close();
			return tbList;
		} catch (SQLException e) {

			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	/**
	 * Return a list of all transactions in which an HCP accessed the given
	 * patient's record, within the dates
	 * 
	 * @param patientID
	 *            The MID of the patient in question.
	 * @param lower
	 *            The starting date as a java.util.Date
	 * @param upper
	 *            The ending date as a java.util.Date
	 * @return A java.util.List of transactions.
	 * @throws DBException
	 */
	public List<TransactionBean> getRecordAccesses(long patientID,
			long dlhcpID, java.util.Date lower, java.util.Date upper,
			boolean getByRole) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn
					.prepareStatement("SELECT * FROM transactionlog WHERE secondaryMID=? AND transactionCode IN ("
							+ TransactionType.patientViewableStr
							+ ") "
							+ "AND timeLogged >= ? AND timeLogged <= ? "
							+ "AND loggedInMID!=? "
							+ "ORDER BY timeLogged DESC");
			ps.setLong(1, patientID);
			ps.setTimestamp(2, new Timestamp(lower.getTime()));
			// add 1 day's worth to include the upper
			ps.setTimestamp(3, new Timestamp(upper.getTime() + 1000L * 60L * 60
					* 24L));
			ps.setLong(4, dlhcpID);
			ResultSet rs = ps.executeQuery();
			List<TransactionBean> tbList = loader.loadList(rs);

			tbList = addAndSortRoles(tbList, patientID, getByRole);
			rs.close();
			ps.close();
			return tbList;
		} catch (SQLException e) {

			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	/**
	 * Returns the operation profile
	 * 
	 * @return The OperationalProfile as a bean.
	 * @throws DBException
	 */
	public OperationalProfile getOperationalProfile() throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn
					.prepareStatement("SELECT TransactionCode, count(transactionID) as TotalCount, "
							+ "count(if(loggedInMID<9000000000, transactionID, null)) as PatientCount, "
							+ "count(if(loggedInMID>=9000000000, transactionID, null)) as PersonnelCount "
							+ "FROM transactionlog GROUP BY transactionCode ORDER BY transactionCode ASC");
			ResultSet rs = ps.executeQuery();
			OperationalProfile result = operationalProfileLoader.loadSingle(rs);
			rs.close();
			ps.close();
			return result;
		} catch (SQLException e) {

			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	/**
	 * 
	 * @param tbList
	 * @param patientID
	 * @param sortByRole
	 * @return
	 * @throws DBException
	 */
	private List<TransactionBean> addAndSortRoles(List<TransactionBean> tbList,
			long patientID, boolean sortByRole) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;

		try {
			conn = factory.getConnection();

			for (TransactionBean t : tbList) {
				ps = conn
						.prepareStatement("SELECT Role FROM users WHERE MID=?");
				ps.setLong(1, t.getLoggedInMID());
				ResultSet rs = ps.executeQuery();
				String role = "";
				if (rs.next())
					role = rs.getString("Role");
				if (role.equals("er"))
					role = "Emergency Responder";
				else if (role.equals("uap"))
					role = "UAP";
				else if (role.equals("hcp")) {
					role = "LHCP";
					ps = conn
							.prepareStatement("SELECT PatientID FROM declaredhcp WHERE HCPID=?");
					ps.setLong(1, t.getLoggedInMID());
					ResultSet rs2 = ps.executeQuery();
					while (rs2.next()) {
						if (rs2.getLong("PatientID") == patientID) {
							role = "DLHCP";
							break;
						}
					}
					rs2.close();
				} else if (role.equals("patient")) {
					role = "Patient";
					ps = conn
							.prepareStatement("SELECT representeeMID FROM representatives WHERE representerMID=?");
					ps.setLong(1, t.getLoggedInMID());
					ResultSet rs2 = ps.executeQuery();
					while (rs2.next()) {
						if (rs2.getLong("representeeMID") == patientID) {
							role = "Personal Health Representative";
							break;
						}
					}
					rs2.close();
				}

				t.setRole(role);
				rs.close();
				ps.close();
			}

			if (sortByRole) {
				TransactionBean[] array = new TransactionBean[tbList.size()];
				array[0] = tbList.get(0);
				TransactionBean t;
				for (int i = 1; i < tbList.size(); i++) {
					t = tbList.get(i);
					String role = t.getRole();
					int j = 0;
					while (array[j] != null
							&& role.compareToIgnoreCase(array[j].getRole()) >= 0)
						j++;
					for (int k = i; k > j; k--) {
						array[k] = array[k - 1];
					}
					array[j] = t;
				}
				int size = tbList.size();
				for (int i = 0; i < size; i++)
					tbList.set(i, array[i]);
			}

			return tbList;
		} catch (SQLException e) {

			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}
}