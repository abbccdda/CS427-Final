package edu.ncsu.csc.itrust.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.beans.ObstetricsVisitBean;
import edu.ncsu.csc.itrust.beans.loaders.ObstetricsVisitLoader;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.ITrustException;

/**
 * This is the Data Access Object used to represent/communicate with the obstetricsvisit Database
 * @author dahmen2
 *
 */
public class ObstetricsVisitDAO {
	private DAOFactory factory;
	private ObstetricsVisitLoader obstetricsVisitLoader;
	
	public ObstetricsVisitDAO(DAOFactory factory){
		this.factory = factory;
		this.obstetricsVisitLoader = new ObstetricsVisitLoader();
	}
	
	public long add(ObstetricsVisitBean ob) throws DBException, ITrustException {
		Connection conn = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		
		try{
			conn = factory.getConnection();
			//Insert into the database, and update weeksPregnant in obstetrics
			ps = conn.prepareStatement(""
					+ "INSERT INTO obstetricsvisit (visitDate, weeksPregnant, bloodPressure, fetalHeartRate, fundalHeightUterus, MID) VALUES (?,?,?,?,?,?);");
			ps = obstetricsVisitLoader.loadParameters(ps, ob);
			ps2 = conn.prepareStatement("UPDATE obstetrics,obstetricsvisit SET obstetrics.weeksPregnant = obstetricsvisit.weeksPregnant where obstetrics.MID=?");
			ps2.setLong(1, ob.getMID());
			ps2.executeUpdate();
			ps.executeUpdate();
			ps.close();
			return DBUtil.getLastInsert(conn);
		} catch (SQLException e) {
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}
	
	public void edit(ObstetricsVisitBean ob) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		
		try{
			conn = factory.getConnection();
			//Update the information into obstetricsvisit database, then update weeksPregnant accordingly in obstetrics
			ps = conn.prepareStatement(""
						+"UPDATE obstetricsvisit SET visitDate=?, weeksPregnant=?, bloodPressure=?, fetalHeartRate=?, fundalHeightUterus=? WHERE MID=?;"
						+"UPDATE obstetrics,obstetricsvisit SET obstetrics.weeksPregnant = obstetricsvisit.weeksPregnant"
						+"WHERE obstetrics.MID=obstetricsvisit.MID");
			ps = obstetricsVisitLoader.loadParameters(ps, ob);
			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getLocalizedMessage());
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	/**
	 * Returns a list of all obstetrics visit records for a particular female patient
	 * 
	 * @param mid The MID of the patient to look up.
	 * @return A java.util.List of HealthRecords.
	 * @throws DBException
	 */
	public List<ObstetricsVisitBean> getAllObstetricsVisitRecords(long mid) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		List<ObstetricsVisitBean> records = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT * FROM obstetricsvisit WHERE MID=?");
			ps.setLong(1, mid);
			ResultSet rs;
			rs = ps.executeQuery();
			records = obstetricsVisitLoader.loadList(rs);
			rs.close();
		} catch (SQLException e) {
			
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
		return records;
	}


	
}
