package edu.ncsu.csc.itrust.action;

import java.util.List;

import edu.ncsu.csc.itrust.beans.ObstetricsBean;
import edu.ncsu.csc.itrust.beans.ObstetricsVisitBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.testutils.TestDAOFactory;
import junit.framework.TestCase;

public class ObstetricsInfoActionTest extends TestCase {
	private ObstetricsInfoAction action;
	private DAOFactory factory;
	private String mid = "1";
	private long actualMid = 1L;
	TestDataGenerator gen;
	
	@Override
	protected void setUp() throws Exception {
		gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.standardData();
		
		this.factory = TestDAOFactory.getTestInstance();
		this.action = new ObstetricsInfoAction(factory, "1",10L);
	}
	
	/**
	 * Tests that invalid patient MID's are handled correctly.
	 */
	public void testPatientCheckID() {
		try {
			this.action = new ObstetricsInfoAction(factory, "2",10L);
		} catch (ITrustException e) {
			assert(e.getMessage().equals("The patient is not eligible for obstetric care."));
		}
		
		try {
			this.action = new ObstetricsInfoAction(factory, "12",10L);
		} catch (ITrustException e) {
			assert(e.getMessage().equals("Patient does not exist"));
		}
		
		try {
			this.action = new ObstetricsInfoAction(factory, "5",10L);
		} catch (ITrustException e) {
		}
	}
	
	/**
	 * Tests that we can get a list of all obstetrics records.
	 * @throws ITrustException
	 */
	public void testGetAllObstetrics() throws ITrustException {
		List<ObstetricsBean> obstetrics = action.getAllObstetricsRecords();
		assertTrue(obstetrics.size() != 0);
	}
	
	/**
	 * Tests that we can successfully obtain a (non-null) List of obstetrics
	 * visits from getObstetricsVisits().
	 * @throws ITrustException
	 */
	public void testGetObstetricsVisits() throws ITrustException {
		List<ObstetricsVisitBean> visits = action.getObstetricsVisits();
		assertNotNull(visits);
	}
	
	/**
	 * Tests that we can successfully add obstetrics info.
	 * @throws ITrustException
	 */
	public void testAddObbstetricsInfo() throws ITrustException {
		List<ObstetricsBean> obstetrics = action.getAllObstetricsRecords();
		assertTrue(obstetrics.size() != 0);
		ObstetricsBean testBean = obstetrics.get(0);
		action.addObstetricsInfo(testBean);
	}
	
	/**
	 * Tests that we can successfully add obstetrics visit info.
	 * @throws ITrustException
	 */
	public void testAddObbstetricsVisitInfo() throws ITrustException {
		ObstetricsVisitBean testBean = new ObstetricsVisitBean();
		testBean.setMID(21);
		testBean.setVisitDate("11/11/11");
		testBean.setWeeksPregnant("35-2");
		testBean.setBloodPressure("180/70");
		testBean.setFetalHeartRate(150);
		testBean.setFundalHeightUterus(5.0);
		action.addObstetricsVisitInfo(testBean);
	}
	
	/**
	 * Tests that we can successfully update obstetrics visit info.
	 * @throws ITrustException
	 */
	public void testUpdateObbstetricsVisitInfo() throws ITrustException {
		ObstetricsVisitBean testBean = new ObstetricsVisitBean();
		testBean.setMID(21);
		testBean.setVisitDate("11/11/11");
		testBean.setWeeksPregnant("35-2");
		testBean.setBloodPressure("180/70");
		testBean.setFetalHeartRate(150);
		testBean.setFundalHeightUterus(5.0);
		action.addObstetricsVisitInfo(testBean);
		testBean.setVisitDate("09/14/82");
		action.updateObstetricsVisitInfo(testBean);
	}
	
	/**
	 * Tests a valid calculation of EDD, as well as invalid input.
	 * @throws ITrustException
	 */
	public void testCalculateEDDAndWeek() throws ITrustException {
		try {
			String[] EDD = action.calculateEDDAndWeek("11/12/14");
			assertTrue(EDD[0].equals("08/19/15"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			String[] EDD = action.calculateEDDAndWeek("31/12/9999");
		} catch (Exception e) {
			assertTrue(e.getMessage().equals("Invalid Input"));
		}
		try {
			String[] EDD = action.calculateEDDAndWeek("fsdfisdfhsfds");
		} catch (Exception e) {
			//Ignore, no message with this exception
		}
	}

}
