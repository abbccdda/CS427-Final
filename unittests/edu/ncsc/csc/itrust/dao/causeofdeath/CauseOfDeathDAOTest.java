package edu.ncsc.csc.itrust.dao.causeofdeath;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc.itrust.DBBuilder;
import edu.ncsu.csc.itrust.beans.CauseOfDeathBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.CauseOfDeathDAO;
import edu.ncsu.csc.itrust.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.testutils.TestDAOFactory;

public class CauseOfDeathDAOTest {
	
	private DAOFactory factory = TestDAOFactory.getTestInstance();
	private CauseOfDeathDAO codDAO = factory.getCauseOfDeathDAO();
	private static final long DOCTOR_MID = 9000000000L;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Tests functionality for getting the top 2 causes of death among all patients.
	 * @throws DBException
	 */
	@Test
	public void testGetTop2All() throws DBException {
		//fail("Not yet implemented");
		List<CauseOfDeathBean> testList = codDAO.getTop2All(DOCTOR_MID, "Male", 2000, 2008);
		CauseOfDeathBean testBean1 = testList.get(0);
		CauseOfDeathBean testBean2 = testList.get(1);
		assertTrue(testBean1.getDescription().equals("Malaria"));
		assertTrue(testBean1.getCode().equals("84.50"));
		assertTrue(testBean1.getCount() == 2);
		assertTrue(testBean2.getDescription().equals("Diabetes with ketoacidosis"));
		assertTrue(testBean2.getCode().equals("250.10"));
		assertTrue(testBean2.getCount() == 1);
		
		testList = codDAO.getTop2All(DOCTOR_MID, "Female", 2000, 2008);
		testBean1 = testList.get(0);
		assertTrue(testBean1.getDescription().equals("Malaria"));
		assertTrue(testBean1.getCode().equals("84.50"));
		assertTrue(testBean1.getCount() == 1);
		
		testList = codDAO.getTop2All(DOCTOR_MID, null, 2000, 2008);
		testBean1 = testList.get(0);
		testBean2 = testList.get(1);
		assertTrue(testBean1.getDescription().equals("Malaria"));
		assertTrue(testBean1.getCode().equals("84.50"));
		assertTrue(testBean1.getCount() == 3);
		assertTrue(testBean2.getDescription().equals("Diabetes with ketoacidosis"));
		assertTrue(testBean2.getCode().equals("250.10"));
		assertTrue(testBean2.getCount() == 1);
	}
	
	/**
	 * Tests functionality for getting the top 2 causes of death specific to an HCP.
	 * @throws DBException
	 */
	@Test
	public void testGetTop2Specific() throws DBException {
		List<CauseOfDeathBean> testList = codDAO.getTop2Specific(DOCTOR_MID, "Male", 2000, 2008);
		CauseOfDeathBean testBean1 = testList.get(0);
		CauseOfDeathBean testBean2 = testList.get(1);
		assertTrue(testBean1.getDescription().equals("Malaria"));
		assertTrue(testBean1.getCode().equals("84.50"));
		assertTrue(testBean1.getCount() == 2);
		assertTrue(testBean2.getDescription().equals("Diabetes with ketoacidosis"));
		assertTrue(testBean2.getCode().equals("250.10"));
		assertTrue(testBean2.getCount() == 1);
		
		testList = codDAO.getTop2Specific(DOCTOR_MID, "Female", 2000, 2008);
		testBean1 = testList.get(0);
		assertTrue(testBean1.getDescription().equals("Malaria"));
		assertTrue(testBean1.getCode().equals("84.50"));
		assertTrue(testBean1.getCount() == 1);
		
		testList = codDAO.getTop2Specific(DOCTOR_MID, null, 2000, 2008);
		testBean1 = testList.get(0);
		testBean2 = testList.get(1);
		assertTrue(testBean1.getDescription().equals("Malaria"));
		assertTrue(testBean1.getCode().equals("84.50"));
		assertTrue(testBean1.getCount() == 3);
		assertTrue(testBean2.getDescription().equals("Diabetes with ketoacidosis"));
		assertTrue(testBean2.getCode().equals("250.10"));
		assertTrue(testBean2.getCount() == 1);
	}

}
