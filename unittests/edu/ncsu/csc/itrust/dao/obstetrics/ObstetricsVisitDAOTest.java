package edu.ncsu.csc.itrust.dao.obstetrics;

import java.util.List;

import edu.ncsu.csc.itrust.beans.ObstetricsBean;
import edu.ncsu.csc.itrust.beans.ObstetricsVisitBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.ObstetricsDAO;
import edu.ncsu.csc.itrust.dao.mysql.ObstetricsVisitDAO;
import edu.ncsu.csc.itrust.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.testutils.TestDAOFactory;
import junit.framework.TestCase;

/**
 * Tests all DAO functions for ability to communcate with the database
 *
 */
public class ObstetricsVisitDAOTest extends TestCase{
	private DAOFactory factory = TestDAOFactory.getTestInstance();
	private ObstetricsVisitDAO oDAO = factory.getObstetricsVisitDAO();

	@Override
	protected void setUp() throws Exception {
		TestDataGenerator gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.hospitals();
		gen.hcp0();
		gen.icd9cmCodes();
		gen.family();
	}
	

	/**
	 *	Tests that obstetric information can correctly be added to the database via ObstetricsDAO 
	 * @throws Exception
	 */
	public void testAddObstetricsVisitBean() throws Exception{
		List<ObstetricsVisitBean> records = oDAO.getAllObstetricsVisitRecords(21);
		assertEquals(0,records.size());
		ObstetricsVisitBean visit = new ObstetricsVisitBean();
		addPeachVisit();
		records = oDAO.getAllObstetricsVisitRecords(21);
		assertEquals(1,records.size());
	}
	
	
	public void testEditObstetricsVisitBean() throws Exception{
		List<ObstetricsVisitBean> records = oDAO.getAllObstetricsVisitRecords(21);
		assertEquals(0,records.size());
		ObstetricsVisitBean visit = new ObstetricsVisitBean();
		addPeachVisit();
		records = oDAO.getAllObstetricsVisitRecords(21);
		assertEquals(1,records.size());
		ObstetricsVisitBean testBean = records.get(0);
		testBean.setVisitDate("11/11/11");;
		oDAO.edit(testBean);
	}
	
	
		/**
	 * Adds obstetric information about Princess Peach
	 * @throws Exception
	 */
	private void addPeachVisit() throws Exception{
		ObstetricsVisitBean princessPeachGotKnockedUp = new ObstetricsVisitBean();
		princessPeachGotKnockedUp.setBloodPressure("111/111");
		princessPeachGotKnockedUp.setFetalHeartRate(55);
		princessPeachGotKnockedUp.setFundalHeightUterus(22);
		princessPeachGotKnockedUp.setVisitDate("11/11/92");
		princessPeachGotKnockedUp.setWeeksPregnant("07-5");
		princessPeachGotKnockedUp.setMID(21);
		oDAO.add(princessPeachGotKnockedUp);
	}

	
}
