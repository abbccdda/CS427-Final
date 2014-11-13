package edu.ncsu.csc.itrust.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.beans.ObstetricsBean;
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
	private ObstetricsDAO obDAO;
	
	public ObstetricsVisitDAO(DAOFactory factory){
		this.factory = factory;
		this.obstetricsVisitLoader = new ObstetricsVisitLoader();
		this.obDAO = new ObstetricsDAO(factory);
	}
	
	public long add(ObstetricsVisitBean ob) throws DBException, ITrustException {
		Connection conn = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		
		try{
			conn = factory.getConnection();
			//Insert into the database, and update weeksPregnant in obstetrics
			ps = conn.prepareStatement(""
					+ "INSERT INTO obstetricsvisit (visitDate, weeksPregnant, bloodPressure, fetalHeartRate, fundalHeightUterus, MID, obstetricsID) VALUES (?,?,?,?,?,?,?);");
			ps = obstetricsVisitLoader.loadParameters(ps, ob);
			ps.setFloat(7, ob.getObid());
			ps.executeUpdate();
			ps.close();
			
			ps2 = conn.prepareStatement("UPDATE obstetrics SET obstetrics.weeksPregnant =? where obstetrics.id=?");
			ps2.setString(1, ob.getWeeksPregnant());
			ps2.setLong(2, ob.getObid());
			ps2.executeUpdate();
			ps2.close();
			
			return DBUtil.getLastInsert(conn);
		} catch (SQLException e) {
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
			DBUtil.closeConnection(conn, ps2);
		}
	}
	
	public void edit(ObstetricsVisitBean ob) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		
		try{
			conn = factory.getConnection();
			//Update the information into obstetricsvisit database, then update weeksPregnant accordingly in obstetrics
			ps = conn.prepareStatement("UPDATE obstetricsvisit SET visitDate=?, weeksPregnant=?, bloodPressure=?, fetalHeartRate=?, fundalHeightUterus=? WHERE id=?;");
			ps.setString(1, ob.getVisitDate());
			ps.setString(2, ob.getWeeksPregnant());
			ps.setString(3, ob.getBloodPressure());
			ps.setInt(4, ob.getFetalHeartRate());
			ps.setDouble(5, ob.getFundalHeightUterus());
			ps.setLong(6, ob.getId());
			ps.executeUpdate();
			ps.close();
			
			ps2 = conn.prepareStatement("UPDATE obstetrics SET obstetrics.weeksPregnant =? where obstetrics.id=?");
			ps2.setString(1, ob.getWeeksPregnant());
			ps2.setLong(2, ob.getObid());
			ps2.executeUpdate();
			ps2.close();
			
		} catch (SQLException e) {
			System.out.println(e.getLocalizedMessage());
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
			DBUtil.closeConnection(conn, ps2);
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

	public ObstetricsVisitBean getObstetricsVisitByID(long id) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		ObstetricsVisitBean record = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT * FROM obstetricsvisit WHERE id=?");
			ps.setLong(1, id);
			ResultSet rs;
			rs = ps.executeQuery();
			rs.next();
			record = obstetricsVisitLoader.loadSingle(rs);
			rs.close();
		} catch (SQLException e) {
			
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
		return record;
	}
	
}
