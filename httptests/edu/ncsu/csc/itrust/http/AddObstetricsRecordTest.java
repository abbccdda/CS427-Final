package edu.ncsu.csc.itrust.http;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebLink;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;

import edu.ncsu.csc.itrust.enums.TransactionType;

public class AddObstetricsRecordTest extends iTrustHTTPTest {

	@Override
	public void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.standardData();
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Logs in as a OB/GYN, navigates to Obstetrics info, clicks add button, inputs information,
	 * submits the information, and verifies the information was saved in the records.
	 * Year of conception: 1992
	 * Weeks pregnant: 37-5
	 * Hours labor: 25.0
	 * Delivery method: Vaginal Delivery
	 * @throws Exception
	 */
	@Test
	public void testAddObstetrics() throws Exception {
		//Login as admin
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		//Go to All Patients
		wr = wr.getLinkWith("All Patients").click();
		assertEquals("iTrust - View All Patients", wr.getTitle());
		
		//Select Random Person
		wr = wr.getLinkWith("Random Person").click();
		assertEquals("iTrust - Edit Personal Health Record", wr.getTitle());
		
		//Select Obstetrics
		wr = wr.getLinkWith("Obstetrics").click();
		WebTable wt = wr.getTableStartingWith("Obstetric History");
		assertEquals("Obstetric History", wt.getCellAsText(0, 0));
		
		//Click Add Obstetrics Info
		WebForm form = wr.getForms()[0];
		form.getSubmitButtons()[0].click();
		wr = wc.getCurrentPage();
		form = wr.getForms()[0];
		
		//Enter info
		form.setParameter("yearOfConception", "1992");
		form.setParameter("weeksPregnant", "37-5");
		form.setParameter("hoursLabor", "25.0");
		form.setParameter("deliveryMethod", "Vaginal Delivery");
		form.getSubmitButtons()[0].click();
		
		//Verify info was saved
		wr = wc.getCurrentPage();
		wt = wr.getTableStartingWith("Obstetric History");
		assertEquals("1992", wt.getCellAsText(wt.getRowCount()-1, 2));
		assertEquals("37-5", wt.getCellAsText(wt.getRowCount()-1, 1));
		assertEquals("25.0", wt.getCellAsText(wt.getRowCount()-1, 3));
		assertEquals("Vaginal Delivery", wt.getCellAsText(wt.getRowCount()-1, 0));
	}
	
	/**
	 * Tests invalid input.
	 * Case 1: Year 992, Weeks 37-5, Hours 25.0
	 * Case 2: Year 1985, Weeks 37-50, Hours 25.0
	 * Case 3: Year 1985, Weeks 37-5, Hours 250.0
	 * Valid case: Year 1985, Weeks 37-5, Hours 25.0
	 * @throws Exception
	 */
	@Test
	public void testBadInput() throws Exception{
		//Login as admin
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
				
		//Go to All Patients
		wr = wr.getLinkWith("All Patients").click();
		assertEquals("iTrust - View All Patients", wr.getTitle());
				
		//Select Random Person
		wr = wr.getLinkWith("Random Person").click();
		assertEquals("iTrust - Edit Personal Health Record", wr.getTitle());
				
		//Select Obstetrics
		wr = wr.getLinkWith("Obstetrics").click();
		WebTable wt = wr.getTableStartingWith("Random Person's Obstetric History");
		assertEquals("Random Person's Obstetric History", wt.getCellAsText(0, 0));
		
		//Click Add Obstetrics Info
		WebForm form = wr.getForms()[0];
		form.getSubmitButtons()[0].click();
		wr = wc.getCurrentPage();
		form = wr.getForms()[0];
		
		//Enter bad year of conception
		form.setParameter("yearOfConception", "992");
		form.setParameter("weeksPregnant", "37-5");
		form.setParameter("hoursLabor", "25.0");
		form.setParameter("deliveryMethod", "Vaginal Delivery");
		form.getSubmitButtons()[0].click();
		
		//Verify info was not submitted
		wr = wc.getCurrentPage();
		wt = wr.getTableStartingWith("Obstetric History for Random Person");
		assertEquals(wt.getCellAsText(0,0), "Obstetric History for Random Person");
		
		//Enter bad weeks pregnant
		form = wr.getForms()[0];
		form.setParameter("yearOfConception", "1985");
		form.setParameter("weeksPregnant", "37-50");
		form.setParameter("hoursLabor", "25.0");
		form.setParameter("deliveryMethod", "Vaginal Delivery");
		form.getSubmitButtons()[0].click();
		
		//Verify info was not submitted
		wr = wc.getCurrentPage();
		wt = wr.getTableStartingWith("Obstetric History for Random Person");
		assertEquals(wt.getCellAsText(0,0), "Obstetric History for Random Person");
		
		//Enter bad hours labor
		form = wr.getForms()[0];
		form.setParameter("yearOfConception", "1985");
		form.setParameter("weeksPregnant", "37-5");
		form.setParameter("hoursLabor", "250.0");
		form.setParameter("deliveryMethod", "Vaginal Delivery");
		form.getSubmitButtons()[0].click();
		
		//Verify info was not submitted
		wr = wc.getCurrentPage();
		wt = wr.getTableStartingWith("Obstetric History for Random Person");
		assertEquals(wt.getCellAsText(0,0), "Obstetric History for Random Person");
		
		//Now, enter valid info
		form = wr.getForms()[0];
		form.setParameter("yearOfConception", "1985");
		form.setParameter("weeksPregnant", "37-5");
		form.setParameter("hoursLabor", "25.0");
		form.setParameter("deliveryMethod", "Vaginal Delivery");
		form.getSubmitButtons()[0].click();
		
		//Verify info was saved
		wr = wc.getCurrentPage();
		wt = wr.getTableStartingWith("Random Person's Obstetric History");
		assertEquals("1985", wt.getCellAsText(wt.getRowCount()-1, 2));
		assertEquals("37-5", wt.getCellAsText(wt.getRowCount()-1, 1));
		assertEquals("25.0", wt.getCellAsText(wt.getRowCount()-1, 3));
		assertEquals("Vaginal Delivery", wt.getCellAsText(wt.getRowCount()-1, 0));
	}

}

