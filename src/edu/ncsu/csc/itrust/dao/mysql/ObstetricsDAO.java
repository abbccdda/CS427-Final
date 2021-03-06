package edu.ncsu.csc.itrust.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.beans.HealthRecord;
import edu.ncsu.csc.itrust.beans.ObstetricsBean;
import edu.ncsu.csc.itrust.beans.OfficeVisitBean;
import edu.ncsu.csc.itrust.beans.PatientHistoryBean;
import edu.ncsu.csc.itrust.beans.PersonnelBean;
import edu.ncsu.csc.itrust.beans.loaders.ObstetricsLoader;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.ITrustException;

/**
 * This is the Data Access Object used to represent/communicate with the Obstetrics Database
 * @author dahmen2
 *
 */
public class ObstetricsDAO {
	private DAOFactory factory;
	private ObstetricsLoader obstetricsLoader;
	
	public ObstetricsDAO(DAOFactory factory){
		this.factory = factory;
		this.obstetricsLoader = new ObstetricsLoader();
	}
	
	public long add(ObstetricsBean ob) throws DBException, ITrustException {
		Connection conn = null;
		PreparedStatement ps = null;
		
		try{
			conn = factory.getConnection();
			ps = conn.prepareStatement("INSERT INTO obstetrics (yearOfConception, weeksPregnant, hoursLabor, deliveryMethod, MID) VALUES (?,?,?,?,?)");
			ps = obstetricsLoader.loadParameters(ps, ob);
			ps.executeUpdate();
			ps.close();
			return DBUtil.getLastInsert(conn);
		} catch (SQLException e) {
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}
	
	public void edit(ObstetricsBean ob) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		
		try{
			conn = factory.getConnection();
			ps = conn.prepareStatement("UPDATE obstetrics SET yearOfConception=?, weeksPregnant=?, hoursLabor=?, deliveryMethod=? WHERE id=?");
			ps.setInt(1, ob.getYearOfConception());
			ps.setString(2, ob.getWeeksPregnant());
			ps.setDouble(3, ob.getHoursLabor());
			ps.setString(4, ob.getDeliveryMethod());
			ps.setLong(5, ob.getID());
			
			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getLocalizedMessage());
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	/**
	 * Returns a list of all obstetrics records for a particular female patient
	 * 
	 * @param mid The MID of the patient to look up.
	 * @return A java.util.List of HealthRecords.
	 * @throws DBException
	 */
	public List<ObstetricsBean> getAllObstetricsRecords(long mid) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		List<ObstetricsBean> records = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT * FROM obstetrics WHERE MID=?");
			ps.setLong(1, mid);
			ResultSet rs;
			rs = ps.executeQuery();
			records = obstetricsLoader.loadList(rs);
			rs.close();
		} catch (SQLException e) {
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
		return records;
	}

	public ObstetricsBean getMostRecentRecord(long mid) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		ObstetricsBean record = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT * FROM obstetrics WHERE MID=? ORDER BY yearOfConception DESC");
			ps.setLong(1, mid);
			ResultSet rs;
			rs = ps.executeQuery();
			rs.next();
			record = obstetricsLoader.loadSingle(rs);
			rs.close();
		} catch (SQLException e) {
			
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
		return record;
	}
	
}
