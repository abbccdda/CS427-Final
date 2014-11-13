/**
 * 
 */
package edu.ncsu.csc.itrust.action;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.ncsu.csc.itrust.HtmlEncoder;
import edu.ncsu.csc.itrust.action.base.PatientBaseAction;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.ObstetricsDAO;
import edu.ncsu.csc.itrust.dao.mysql.ObstetricsVisitDAO;
import edu.ncsu.csc.itrust.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO;
import edu.ncsu.csc.itrust.enums.Gender;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.beans.ObstetricsBean;
import edu.ncsu.csc.itrust.beans.ObstetricsVisitBean;
import edu.ncsu.csc.itrust.beans.PatientBean;

/**
 * ObstetricsInfoAction holds all the neccesary functions to communicate with the DAO's for obstetricsVisit and patient information
 *
 */
public class ObstetricsInfoAction extends PatientBaseAction {
	private ObstetricsVisitDAO obstetricsVisitDAO;
	private ObstetricsDAO obstetricsDAO;
	private PatientDAO patientDAO;
	private PersonnelDAO personnelDAO;
	private long patientMID;
	private long loggedInMID;
	private DAOFactory factory;
	
	public ObstetricsInfoAction(DAOFactory factory, String pidString, long loggedInMID) throws ITrustException {
		super(factory, pidString);
		this.factory = factory;
		this.obstetricsVisitDAO = factory.getObstetricsVisitDAO();
		this.patientDAO = factory.getPatientDAO();
		this.obstetricsDAO = factory.getObstetricsDAO();
		this.personnelDAO = factory.getPersonnelDAO();
		this.patientMID = Long.parseLong(pidString);
		this.loggedInMID = loggedInMID;
	}
	
	/**
	 * Uses the ObstetricsVisitDAO to return the Obsteterics Records
	 * @return
	 * @throws DBException
	 */
	public List<ObstetricsVisitBean> getObstetricsVisits() throws DBException{
		return obstetricsVisitDAO.getAllObstetricsVisitRecords(patientMID);
	}
	
	/**
	 * Get all Obstetrics Records for a specific patient by theirMID.
	 * @param mid
	 * @return
	 * @throws ITrustException
	 */
	public List<ObstetricsBean> getAllObstetricsRecords() throws ITrustException{
		List<ObstetricsBean> obstetricsList = obstetricsDAO.getAllObstetricsRecords(patientMID);
		return obstetricsList;
	}
	

	public String getPatientName() throws ITrustException{
		return patientDAO.getName(patientMID);
	}
	
	public PatientBean getPatient() throws DBException {
		return patientDAO.getPatient(patientMID);
	}
	
	public long getPatientMID() throws DBException {
		return this.patientMID;
	}
	
	public String getPersonnelSpecialty() throws ITrustException{
		return personnelDAO.getPersonnel(loggedInMID).getSpecialty();
	}
	
	public void addObstetricsInfo(ObstetricsBean ob) throws ITrustException{
		ob.setMID(patientMID);
		obstetricsDAO.add(ob);
	}
	
	public void addObstetricsVisitInfo(ObstetricsVisitBean ob) throws ITrustException{
		ob.setMID(patientMID);
		obstetricsVisitDAO.add(ob);
	}
	
	
	/**
	 * Calculate the EED by input LMP
	 * @param input LMP to be processed
	 * @return List[0] is the EED; List[1] is # of weeks; List[2] is # of days
	 * @throws Exception 
	 */
	public static String[]  calculateEDDAndWeek(String LMP) throws Exception{
		String result[] = new String[3];
		// note this will parse all kinds of good stuff like 1/11/11 as 01/11/11 or 9/1/1992 as 09/01/92
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy");
		try {
			Date date = formatter.parse(LMP); 
			Calendar cal = Calendar.getInstance();
			String[] tokens = LMP.split("/");
			int m = Integer.parseInt(tokens[0]);
			int d = Integer.parseInt(tokens[1]);
			int y = Integer.parseInt(tokens[2]);
			
			if(date.after(cal.getTime()) || d > 31 || d < 0 || m < 0 ||m > 12 || y < 0){
				throw new Exception("Invalid Input");
			}
			/*step 1, go to calendar to find current date*/
			long diff = cal.getTimeInMillis() - date.getTime();
			long diffDays = diff / (24 * 60 * 60 * 1000);
			long diffWeeks = diff / (7 * 24 * 60 * 60 * 1000);
			diffDays -=diffWeeks * 7;
			
			result[1] = String.valueOf(diffWeeks);
			result[2] = String.valueOf(diffDays);
			
			/*step 2, get EED by adding 280 to input date*/
			cal.setTime(date); // Now use LMP date.
			cal.add(Calendar.DATE, 280); // Adding 280 days
			result[0] = formatter.format(cal.getTime());
		} 
		catch (java.text.ParseException e) {
			throw e;
		}
		catch (Exception e) {
			throw e;
		}
		return result;
	}
}
