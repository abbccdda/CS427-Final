package edu.ncsu.csc.itrust.dao.mysql;

import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.beans.Email;
import edu.ncsu.csc.itrust.beans.loaders.EmailBeanLoader;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.exception.DBException;

/**
 * 
 * DAO stands for Database Access Object. All DAOs are intended to be reflections of the database, that is,
 * one DAO per table in the database (most of the time). For more complex sets of queries, extra DAOs are
 * added. DAOs can assume that all data has been validated and is correct.
 * 
 * DAOs should never have setters or any other parameter to the constructor than a factory. All DAOs should be
 * accessed by DAOFactory (@see {@link DAOFactory}) and every DAO should have a factory - for obtaining JDBC
 * connections and/or accessing other DAOs.
 * 
 *  
 * 
 */
public class FakeEmailDAO {
	private DAOFactory factory;
	private EmailBeanLoader emailBeanLoader = new EmailBeanLoader();

	/**
	 * The typical constructor.
	 * @param factory The {@link DAOFactory} associated with this DAO, which is used for obtaining SQL connections, etc.
	 */
	public FakeEmailDAO(DAOFactory factory) {
		this.factory = factory;
	}

	/**
	 * Return all emails that have been "sent" (inserted into the database)
	 * 
	 * @return A java.util.List of Email objects representing fake e-mails.
	 * @throws DBException
	 */
	public List<Email> getAllEmails() throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT * FROM fakeemail ORDER BY AddedDate DESC");
			ResultSet rs = ps.executeQuery();
			List<Email> loadlist = emailBeanLoader.loadList(rs);
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
	 * Return all emails that a person has sent
	 * 
	 * @param email The "From" email address as a string.
	 * @return A java.util.List of fake emails.
	 * @throws DBException
	 */
	public List<Email> getEmailsByPerson(String email) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT * FROM fakeemail WHERE FromAddr LIKE ? ORDER BY AddedDate DESC");
			ps.setString(1, "%" + email + "%");
			ResultSet rs = ps.executeQuery();
			List<Email> loadlist = emailBeanLoader.loadList(rs);
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
	 * "Send" an email, which just inserts it into the database.
	 * 
	 * @param email The Email object to insert.
	 * @throws DBException
	 */
	public void sendEmailRecord(Email email) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("INSERT INTO fakeemail (ToAddr, FromAddr, Subject, Body) "
					+ "VALUES (?,?,?,?)");
			emailBeanLoader.loadParameters(ps, email);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	/**
	 * Returns a list of emails that have the given string as a substring of the body
	 * @param string The string to search within the body.
	 * @return A java.util.List of fake emails.
	 * @throws DBException 
	 */
	public List<Email> getEmailWithBody(String bodySubstring) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT * FROM fakeemail WHERE Instr(Body,?)>0 ORDER BY AddedDate DESC");
			ps.setString(1, bodySubstring);
			ResultSet rs = ps.executeQuery();
			List<Email> loadlist = emailBeanLoader.loadList(rs);
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
	 * helper function used to send appointments
	 */
	public void sendReminderEmails(int days) throws DBException{
		Connection conn = null;
		PreparedStatement ps = null;
		try{
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT appointment.sched_date, patients.email,patients.firstName as patientsFirst, patients.lastName as patientsLast,personnel.firstName as doctorFirst,personnel.lastName as doctorLast FROM (appointment LEFT JOIN (patients) on appointment.patient_id = patients.MID) LEFT JOIN personnel on appointment.doctor_id=personnel.MID where appointment.sched_date BETWEEN NOW() AND DATE_ADD(NOW(), INTERVAL ? DAY)");
			ps.setInt(1, days);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				Email email = new Email();
				String patient = rs.getString("patientsFirst") + " " + rs.getString("patientsLast");
				String doctor = rs.getString("doctorFirst") + " " + rs.getString("doctorLast");
				Date date = rs.getDate("sched_date");
				Date today = new Date();
				@SuppressWarnings("deprecation")
				int days_remain = date.getDate() - today.getDate();
				Time time = rs.getTime("sched_date");
				String patientEmail = rs.getString("email");
				List<String> patientEmails = new ArrayList<String>();
				patientEmails.add(patientEmail);
				email.setToList(patientEmails);
				email.setSubject("Reminder: upcoming appointment in "+ days_remain +" day(s)");
				email.setFrom("System Reminder");
				email.setBody(patient + ", You have an appointment on "+time.toString()+", "+date.toString()+" with Dr. "+ doctor);
				this.sendEmailRecord(email);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

}
