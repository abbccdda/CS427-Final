package edu.ncsu.csc.itrust.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

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
 *
 */
@SuppressWarnings("unused")
public class ViewObstetricsVisitHistoryAction {
	private long loggedInMID;
	private PatientDAO patientDAO;
	private ObstetricsVisitDAO obstetricsVisitDAO;
	private ArrayList<ObstetricsVisitBean> visits;

	/**
	 * Set up defaults
	 * @param factory The DAOFactory used to create the DAOs used in this action.
	 * @param loggedInMID The MID of the person viewing the office visits.
	 */
	public ViewObstetricsVisitHistoryAction(DAOFactory factory, long loggedInMID) {
		this.loggedInMID = loggedInMID;
		this.obstetricsVisitDAO = factory.getObstetricsVisitDAO();
		this.patientDAO = factory.getPatientDAO();
		visits = new ArrayList<ObstetricsVisitBean>();
		
	}
	
}