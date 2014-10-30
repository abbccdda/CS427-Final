/**
 * 
 */
package edu.ncsu.csc.itrust.action;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import edu.ncsu.csc.itrust.HtmlEncoder;
import edu.ncsu.csc.itrust.action.base.PatientBaseAction;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.AuthDAO;
import edu.ncsu.csc.itrust.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO;
import edu.ncsu.csc.itrust.enums.Gender;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.validate.PatientValidator;
import edu.ncsu.csc.itrust.beans.ObstetricsBean;

/**
 * @author kyle
 *
 */
public class ObstetricsInfoAction extends PatientBaseAction {

	private PatientValidator validator = new PatientValidator();
	private PatientDAO patientDAO;
	private PersonnelDAO personnelDAO;
	private AuthDAO authDAO;
	private long loggedInMID;
	private long patientMID;
	private DAOFactory factory;
	private long pid;
	
	public ObstetricsInfoAction(DAOFactory factory, String pidString)
			throws ITrustException {
		super(factory, pidString);
		this.factory = factory;
		this.patientMID = Long.parseLong(pidString);
		this.pid = this.checkPatientID(pidString);
		// TODO Auto-generated constructor stub
	}
	
	private long checkPatientID(String input) throws ITrustException {
		try {
			long pid = Long.valueOf(input);
			if (!factory.getPatientDAO().checkPatientExists(pid)) {	
				throw new ITrustException("Patient does not exist");
			}	
			
			if(factory.getPatientDAO().getPatient(pid).getGender() == Gender.Male) {
				throw new ITrustException("The patient is not eligible for obstetric care.");
			}
			
			return pid;
			
		} catch (NumberFormatException e) {
			throw new ITrustException("Patient ID is not a number: " + HtmlEncoder.encode(input));
		}
	}
	
	public List<ObstetricsBean> getAllObstetrics() throws DBException {
		return factory.getPatientDAO().getObstetricsForPatient(pid);
	}
	
	public String getPatientName() throws ITrustException{
		return "patient's";
	}
	
	/**
	 * func 1 I modified
	 * calculate the EED by input LMP
	 * @param input LMP to be processed
	 * @return List[0] is the EED; List[1] is # of weeks; List[2] is # of days
	 */
	public static String[]  calculateEDDAndWeek(String LMP){
		String result[] = new String[3];
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		try {
			java.util.Date date = formatter.parse(LMP);
			Calendar cal = Calendar.getInstance();
			
			String[] tokens = LMP.split("/");
			
			System.out.println("day of month" + tokens[0]);
			System.out.println("day of week" + tokens[1]);
			int d = Integer.parseInt(tokens[0]);
			int m = Integer.parseInt(tokens[1]);
			int y = Integer.parseInt(tokens[2]);
			if(date.after(cal.getTime()) || d > 31 || d < 0 || m < 0
					||m > 12 || y < 0){
				return errorResult();
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
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("invalid input");
			return errorResult();
			
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("invalid input");
			return errorResult();
			
		}
		
		
		return result;
		
	}
	
	/**
	 * func 2 I modified
	 * return when error occurs in parsing
	 * @return
	 */
	public static String[] errorResult(){
		String resultErr[] = new String[3];
		resultErr[0]= "invalid input, try again";
		System.out.println("invalid input");
		return resultErr;
		
	}

}
