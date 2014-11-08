package edu.ncsu.csc.itrust.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import edu.ncsu.csc.itrust.action.base.PatientBaseAction;
import edu.ncsu.csc.itrust.beans.ObstetricsVisitBean;
import edu.ncsu.csc.itrust.beans.OfficeVisitBean;
import edu.ncsu.csc.itrust.beans.PatientBean;
import edu.ncsu.csc.itrust.beans.PatientVisitBean;
import edu.ncsu.csc.itrust.beans.PersonnelBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.ObstetricsVisitDAO;
import edu.ncsu.csc.itrust.dao.mysql.OfficeVisitDAO;
import edu.ncsu.csc.itrust.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.ITrustException;

/**
 * 
 * Action class for ViewPatientOfficeVisitHistory.jsp
 * Action classes are helpful for accessing the database objects via the jsp
 *
 */
@SuppressWarnings("unused")
public class ViewObstetricsVisitHistoryAction extends PatientBaseAction{
	private long loggedInMID;
	private PatientDAO patientDAO;
	private ObstetricsVisitDAO obstetricsVisitDAO;
	private ArrayList<ObstetricsVisitBean> visits;
	private long patientMID;

	/**
	 * Super class handles exception handling of invalid patientMID
	 * @param factory The DAOFactory used to create the DAOs used in this action.
	 * @param pidString The MID of the patient being edited.
	 * @param loggedInMID The MID of the person viewing the office visits.
	 * @throws ITrustException 
	 */
	public ViewObstetricsVisitHistoryAction(DAOFactory factory, String patientMID, long loggedInMID) throws ITrustException {
		super(factory,patientMID);
		this.loggedInMID = loggedInMID;
		this.obstetricsVisitDAO = factory.getObstetricsVisitDAO();
		this.patientDAO = factory.getPatientDAO();
		this.patientMID = Long.parseLong(patientMID);
		visits = new ArrayList<ObstetricsVisitBean>();
	}
	
	/**
	 * Uses the ObstetricsVisitDAO to return the Obsteterics Records
	 * @return
	 * @throws DBException
	 */
	public List<ObstetricsVisitBean> getObstetricsVisits() throws DBException{
		return obstetricsVisitDAO.getAllObstetricsRecords(patientMID);
	}
	
	
}