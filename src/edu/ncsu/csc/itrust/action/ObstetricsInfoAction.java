/**
 * 
 */
package edu.ncsu.csc.itrust.action;

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
	private DAOFactory factory;
	private long pid;
	
	public ObstetricsInfoAction(DAOFactory factory, String pidString)
			throws ITrustException {
		super(factory, pidString);
		this.factory = factory;
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

}
