package edu.ncsu.csc.itrust.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.beans.CauseOfDeathBean;
import edu.ncsu.csc.itrust.beans.loaders.BillingBeanLoader;
import edu.ncsu.csc.itrust.beans.loaders.CODBeanLoader;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.exception.DBException;

public class CauseOfDeathDAO {

	/**DAOfactory used to make a CauseOfDeathDAO*/
	private transient final DAOFactory factory;
	/**used to get load things into the database or from the database*/
	private transient final CODBeanLoader codbloader;
	
	/**
	 * makes a new CauseOfDeathDAO
	 * @param factory the DAOfactory used to make a CauseOfDeathDAO
	 */
	public CauseOfDeathDAO(final DAOFactory factory) {
		this.factory = factory;
		this.codbloader = new CODBeanLoader();
	}
	
	/**
	 * Gets the top 2 causes of death for ALL patients.
	 * @param mid HCP mid
	 * @param gender
	 * @param lowerYear lower bound year
	 * @param upperYear upper bound year
	 * @return Beans representing the top 2 causes of death
	 * @throws DBException
	 */
	public List<CauseOfDeathBean> getTop2All(long mid, String gender, int lowerYear, int upperYear) throws DBException {
		Connection conn = null;
		PreparedStatement pstring = null;
		
		if(lowerYear > upperYear)
			return new ArrayList<CauseOfDeathBean>();
		
		try {
			conn = factory.getConnection();
				if (gender != null){
				pstring = conn.prepareStatement("SELECT Gender, Description, Code, count(Code) "
					+ "FROM patients LEFT OUTER JOIN icdcodes ON "
					+ "patients.CauseOfDeath = icdcodes.Code WHERE Gender=? "
					+ "AND (? <= year(DateOfDeath) AND year(DateOfDeath) <= ?) "
					+ "GROUP BY Description, Code ORDER BY count(Code) DESC LIMIT 2");
				pstring.setString(1, gender);
				pstring.setInt(2, lowerYear);
				pstring.setInt(3, upperYear);
			
				final ResultSet results = pstring.executeQuery();
				List<CauseOfDeathBean> result = new ArrayList<CauseOfDeathBean>();
				if(results.next()) {
					results.absolute(1);
					result.add(this.codbloader.loadSingleWithMID(results, mid));
					if (results.next())
						result.add(this.codbloader.loadSingleWithMID(results, mid));
				}
				return result;
			}
			else {
				pstring = conn.prepareStatement("SELECT Gender, Description, Code, count(Code) "
						+ "FROM patients LEFT OUTER JOIN icdcodes ON "
						+ "patients.CauseOfDeath = icdcodes.Code WHERE "
						+ "(? <= year(DateOfDeath) AND year(DateOfDeath) <= ?) "
						+ "GROUP BY Description, Code ORDER BY count(Code) DESC LIMIT 2");
					pstring.setInt(1, lowerYear);
					pstring.setInt(2, upperYear);
				
					final ResultSet results = pstring.executeQuery();
					List<CauseOfDeathBean> result = new ArrayList<CauseOfDeathBean>();
					if(results.next()) {
						results.absolute(1);
						result.add(this.codbloader.loadSingleWithMIDNullGender(results, mid));
						if (results.next())
							result.add(this.codbloader.loadSingleWithMIDNullGender(results, mid));
					}
				
					return result;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, pstring);
		}
		
	}
	
	/**
	 * Gets the top 2 causes of death for patients specific to the HCP mid (per the office visits).
	 * @param mid HCP mid
	 * @param gender
	 * @param lowerYear lower bound year
	 * @param upperYear upper bound year
	 * @return Beans representing the top 2 causes of death per the HCP's patients
	 * @throws DBException
	 */
	public List<CauseOfDeathBean> getTop2Specific(long mid, String gender, int lowerYear, int upperYear) throws DBException {
		Connection conn = null;
		PreparedStatement pstring = null;
		
		if(lowerYear > upperYear)
			return new ArrayList<CauseOfDeathBean>();
		
		try {
			conn = factory.getConnection();
			if (gender != null){
				pstring = conn.prepareStatement("select Gender, Description, Code, count(Code) from "
					+ "(select distinct CauseOfDeath, Gender, DateOfDeath, MID from patients inner join officevisits on "
					+ "patients.MID = officevisits.PatientID where HCPID=?) "
					+ "as mp left outer join icdcodes on mp.CauseOfDeath = icdcodes.Code "
					+ "where Gender=? and (? <= year(DateOfDeath) and year(DateOfDeath) <= ?) "
					+ "group by Description, Code order by count(Code) desc limit 2");
				pstring.setLong(1, mid);
				pstring.setString(2, gender);
				pstring.setInt(3, lowerYear);
				pstring.setInt(4, upperYear);
			
				final ResultSet results = pstring.executeQuery();
				List<CauseOfDeathBean> result = new ArrayList<CauseOfDeathBean>();
				if(results.next()){
					results.absolute(1);
					result.add(this.codbloader.loadSingleWithMID(results, mid));
					if (results.next())
						result.add(this.codbloader.loadSingleWithMID(results, mid));
				}
				return result;
			} else {
				pstring = conn.prepareStatement("select Gender, Description, Code, count(Code) from "
						+ "(select distinct CauseOfDeath, Gender, DateOfDeath, MID from patients inner join officevisits on "
						+ "patients.MID = officevisits.PatientID where HCPID=?) "
						+ "as mp left outer join icdcodes on mp.CauseOfDeath = icdcodes.Code "
						+ "where (? <= year(DateOfDeath) and year(DateOfDeath) <= ?) "
						+ "group by Description, Code order by count(Code) desc limit 2");
					pstring.setLong(1, mid);
					pstring.setInt(2, lowerYear);
					pstring.setInt(3, upperYear);
					
					final ResultSet results = pstring.executeQuery();
					List<CauseOfDeathBean> result = new ArrayList<CauseOfDeathBean>();
					if(results.next()) {
						results.absolute(1);
						result.add(this.codbloader.loadSingleWithMIDNullGender(results, mid));
						if (results.next())
							result.add(this.codbloader.loadSingleWithMIDNullGender(results, mid));
					}
					return result;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, pstring);
		}
		
	}
	
}
