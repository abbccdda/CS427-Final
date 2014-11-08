package edu.ncsu.csc.itrust.action;

import java.util.List;

import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.AuthDAO;
import edu.ncsu.csc.itrust.dao.mysql.CDCBmiStatsDAO;
import edu.ncsu.csc.itrust.dao.mysql.CDCHeadCircStatsDAO;
import edu.ncsu.csc.itrust.dao.mysql.CDCHeightStatsDAO;
import edu.ncsu.csc.itrust.dao.mysql.CDCWeightStatsDAO;
import edu.ncsu.csc.itrust.dao.mysql.HealthRecordsDAO;
import edu.ncsu.csc.itrust.dao.mysql.NormalDAO;
import edu.ncsu.csc.itrust.dao.mysql.ObstetricsDAO;
import edu.ncsu.csc.itrust.dao.mysql.OfficeVisitDAO;
import edu.ncsu.csc.itrust.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO;
import edu.ncsu.csc.itrust.action.base.PatientBaseAction;
import edu.ncsu.csc.itrust.beans.ObstetricsBean;
import edu.ncsu.csc.itrust.beans.PatientBean;
import edu.ncsu.csc.itrust.enums.Role;
import edu.ncsu.csc.itrust.enums.TransactionType;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.ITrustException;

public class ViewObstetricsRecordsAction extends PatientBaseAction {
	private DAOFactory factory;
	private PatientDAO patientDAO;
	private ObstetricsDAO obstetricsDAO;
	private PersonnelDAO personnelDAO;
	private long patientMID;
	private long loggedInMID;
	
	/**
	 * The super class validates the patient id
	 * 
	 * @param factory The DAOFactory used to create the DAOs for this action.
	 * @param pidString The MID of the patient being edited.
	 * @param loggedInMID The MID of the user who is authorizing this action.
	 * @throws ITrustException
	 */
	public ViewObstetricsRecordsAction(DAOFactory factory, String pidString, long loggedInMID) throws ITrustException{
		super(factory,pidString);
		this.factory = factory;
		this.patientDAO = factory.getPatientDAO();
		this.personnelDAO = factory.getPersonnelDAO();
		this.obstetricsDAO = factory.getObstetricsDAO();
		this.patientMID = Long.parseLong(pidString);
		this.loggedInMID = loggedInMID;
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
	
	/**
	 * Get all Obstetrics Records for a specific patient by theirMID.
	 * @param mid
	 * @return
	 * @throws ITrustException
	 */
	public List<ObstetricsBean> getAllObstetricsRecords() throws ITrustException{
		// Log for an HCP viewing this role
		//loggingAction.logEvent(TransactionType.VIEW_OBSTETRICS, loggedInMID, patientMID, "");
		List<ObstetricsBean> obstetricsList = obstetricsDAO.getAllObstetricsRecords(patientMID);
		return obstetricsList;
	}
	
}
