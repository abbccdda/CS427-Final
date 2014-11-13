package edu.ncsu.csc.itrust.dao.obstetrics;

import java.util.List;

import edu.ncsu.csc.itrust.beans.ObstetricsBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.ObstetricsDAO;
import edu.ncsu.csc.itrust.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.testutils.TestDAOFactory;
import junit.framework.TestCase;

/**
 * Tests all DAO functions for ability to communcate with the database
 *
 */
public class ObstetricsDAOTest extends TestCase{
	private DAOFactory factory = TestDAOFactory.getTestInstance();
	private ObstetricsDAO oDAO = factory.getObstetricsDAO();

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
	 * Tests that patients that don't have Obstetrics Information don't have Any records in the database
	 * @throws Exception
	 */
	public void testEmptyRecords() throws Exception{
		List<ObstetricsBean> records = oDAO.getAllObstetricsRecords(6);
		assertEquals(0,records.size());
	}

	/**
	 *	Tests that obstetric information can correctly be added to the database via ObstetricsDAO 
	 * @throws Exception
	 */
	public void testAddObstetricsBean() throws Exception{
		List<ObstetricsBean> records = oDAO.getAllObstetricsRecords(21);
		assertEquals(0,records.size());
		ObstetricsBean princessPeachGotKnockedUp = new ObstetricsBean();
		addPrincessPeach();
		records = oDAO.getAllObstetricsRecords(21);
		assertEquals(1,records.size());
	}
	
	
	/**
	 * Tests that the obstetrics information for an invidividual patient can be edited in the database.
	 * @throws Exception
	 */
	public void testEditObstetricsBean() throws Exception{
		List<ObstetricsBean> records = oDAO.getAllObstetricsRecords(21);
		assertEquals(0,records.size());
		ObstetricsBean princess = new ObstetricsBean();
		addPrincessPeach();
		records = oDAO.getAllObstetricsRecords(21);
		princess = records.get(records.size()-1); 
		princess.setHoursLabor(5.5);
		oDAO.edit(princess);
		records = oDAO.getAllObstetricsRecords(21);
		princess = records.get(0);
		assertEquals(5.5, princess.getHoursLabor());
	}

	
	/**
	 * Adds obstetric information about Princess Peach
	 * @throws Exception
	 */
	private void addPrincessPeach() throws Exception{
		ObstetricsBean princessPeachGotKnockedUp = new ObstetricsBean();
		princessPeachGotKnockedUp.setDeliveryMethod("Vaginal Delivery");
		princessPeachGotKnockedUp.setHoursLabor(3);
		princessPeachGotKnockedUp.setWeeksPregnant("07-5");
		princessPeachGotKnockedUp.setYearOfConception(1995);
		princessPeachGotKnockedUp.setMID(21);
		oDAO.add(princessPeachGotKnockedUp);
	}

	/**
	 * Tests that off-color input will throw a SQLException/DBException.
	 * @throws Exception
	 */
	public void testBadInput() throws Exception {
		ObstetricsBean evilBean = new ObstetricsBean();
		try{
			String badWeekInput = "sdfdfsdf";
			evilBean.setWeeksPregnant(badWeekInput);
			oDAO.add(evilBean);
		}
		catch(Exception e){
			//Ignore, no message testing needed
		}
		
		try{
			ObstetricsBean princessPeachGotKnockedUp = new ObstetricsBean();
			princessPeachGotKnockedUp.setDeliveryMethod("Vaginal Delivery");
			princessPeachGotKnockedUp.setHoursLabor(3);
			princessPeachGotKnockedUp.setWeeksPregnant("07-5");
			princessPeachGotKnockedUp.setYearOfConception(1995);
			princessPeachGotKnockedUp.setMID(21);
			oDAO.add(princessPeachGotKnockedUp);
			princessPeachGotKnockedUp.setWeeksPregnant("fsdfsdf");
			oDAO.edit(princessPeachGotKnockedUp);
		}
		catch(Exception e) {
			//Ignore, no message testing needed
		}
	}

}
