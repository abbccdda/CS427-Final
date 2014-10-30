package edu.ncsu.csc.itrust.action;

import java.util.List;

import edu.ncsu.csc.itrust.beans.ObstetricsBean;
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
		this.action = new ObstetricsInfoAction(factory, "1");
	}
	
	public void testPatientCheckID() {
		try {
			this.action = new ObstetricsInfoAction(factory, "2");
			fail();
		} catch (ITrustException e) {
			assert(e.getMessage().equals("The patient is not eligible for obstetric care."));
		}
		
		try {
			this.action = new ObstetricsInfoAction(factory, "12");
			fail();
		} catch (ITrustException e) {
			assert(e.getMessage().equals("Patient does not exist"));
		}
		
		try {
			this.action = new ObstetricsInfoAction(factory, "5");
		} catch (ITrustException e) {
			fail();
		}
	}
	
	public void testGetAllObstetrics() throws DBException {
		List<ObstetricsBean> obstetrics = action.getAllObstetrics();
		assertTrue(obstetrics.size() == 11);
	}

}
