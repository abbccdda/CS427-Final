package edu.ncsu.csc.itrust.http;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;

import edu.ncsu.csc.itrust.enums.TransactionType;

public class ViewObstetricsRecordsTest extends iTrustHTTPTest {
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		gen.hcp0();
		gen.patient3();
		gen.patient5();
		gen.patient6();
		gen.uc63();
	}
	
	/**
	 * Tests the ability for the HCP that is logged in to view a patients Obstetrics information if that obstetric information exists
	 * if any exists
	 * @throws Exception
	 */
	public void testViewObstetricsRequest() throws Exception{
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		wr = wr.getLinkWith("Obstetrics Information").click();
		assertEquals("iTrust - Please Select a Patient", wr.getTitle());
		wr.getForms()[1].setParameter("FIRST_NAME", "Baby");
		wr.getForms()[1].setParameter("LAST_NAME", "Programmer");
		wr.getForms()[1].getButtons()[0].click();
		wr = wc.getCurrentPage();		
		WebTable wt = wr.getTableStartingWith("MID");
		assertEquals("MID", wt.getCellAsText(0, 0));
		assertEquals("", wt.getCellAsText(1, 0));
		assertEquals("Baby", wt.getCellAsText(1, 1));
		assertEquals("Programmer", wt.getCellAsText(1, 2));
		wr.getForms()[2].getButtons()[0].click();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - View Obstetric Record", wr.getTitle());
	}
	
	
	/**
	 * View Obstetrics information for a female patient who doesn't have any obstetrics information
	 * @throws Exception
	 */
	public void testViewObstetricsEmptyRequest() throws Exception{
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		wr = wr.getLinkWith("Obstetrics Information").click();
		assertEquals("iTrust - Please Select a Patient", wr.getTitle());
		wr.getForms()[1].setParameter("FIRST_NAME", "Baby");
		wr.getForms()[1].setParameter("LAST_NAME", "A");
		wr.getForms()[1].getButtons()[0].click();
		wr = wc.getCurrentPage();		
		WebTable wt = wr.getTableStartingWith("MID");
		assertEquals("MID", wt.getCellAsText(0, 0));
		assertEquals("", wt.getCellAsText(1, 0));
		assertEquals("Baby", wt.getCellAsText(1, 1));
		assertEquals("A", wt.getCellAsText(1, 2));
		wr.getForms()[2].getButtons()[0].click();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - View Obstetric Record", wr.getTitle());
		assertTrue(wr.getText().contains("No Obstetric Information"));
	}
	
	/**
	 * View Obstetrics information for a male patient who doesn't have any obstetrics information
	 * @throws Exception
	 */
	public void testViewObstetricsMaleRequest() throws Exception{
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		wr = wr.getLinkWith("Obstetrics Information").click();
		assertEquals("iTrust - Please Select a Patient", wr.getTitle());
		wr.getForms()[1].setParameter("FIRST_NAME", "Care");
		wr.getForms()[1].setParameter("LAST_NAME", "Needs");
		wr.getForms()[1].getButtons()[0].click();
		wr = wc.getCurrentPage();		
		WebTable wt = wr.getTableStartingWith("MID");
		assertEquals("MID", wt.getCellAsText(0, 0));
		assertEquals("", wt.getCellAsText(1, 0));
		assertEquals("Care", wt.getCellAsText(1, 1));
		assertEquals("Needs", wt.getCellAsText(1, 2));
		wr.getForms()[2].getButtons()[0].click();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - View Obstetric Record", wr.getTitle());
		assertTrue(wr.getText().contains("No Obstetric Information"));
	}
}
