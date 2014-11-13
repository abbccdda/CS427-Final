package edu.ncsu.csc.itrust.http;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;

import edu.ncsu.csc.itrust.enums.TransactionType;

public class ObstetricsVisitTest extends iTrustHTTPTest {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.standardData();
	}
	
	public void testViewObstetricsVisit() throws Exception{
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		wr = wr.getLinkWith("Obstetrics Information").click();
		assertEquals("iTrust - Please Select a Patient", wr.getTitle());
		wr.getForms()[1].setParameter("FIRST_NAME", "Random");
		wr.getForms()[1].setParameter("LAST_NAME", "Person");
		wr.getForms()[1].getButtons()[0].click();
		wr = wc.getCurrentPage();		
		WebTable wt = wr.getTableStartingWith("MID");
		assertEquals("MID", wt.getCellAsText(0, 0));
		assertEquals("", wt.getCellAsText(1, 0));
		assertEquals("Random", wt.getCellAsText(1, 1));
		assertEquals("Person", wt.getCellAsText(1, 2));
		wr.getForms()[2].getButtons()[0].click();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - View Obstetric Record", wr.getTitle());
		
		assertEquals("08-3", wr.getTables()[1].getCellAsText(7, 1));
		assertEquals("100/50", wr.getTables()[1].getCellAsText(7, 2));
		assertEquals("60", wr.getTables()[1].getCellAsText(7, 3));
		assertEquals("20.0", wr.getTables()[1].getCellAsText(7, 4));
		
	}
	
	public void testEditObstetricsVisit() throws Exception{
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		wr = wr.getLinkWith("Obstetrics Information").click();
		assertEquals("iTrust - Please Select a Patient", wr.getTitle());
		wr.getForms()[1].setParameter("FIRST_NAME", "Random");
		wr.getForms()[1].setParameter("LAST_NAME", "Person");
		wr.getForms()[1].getButtons()[0].click();
		wr = wc.getCurrentPage();		
		WebTable wt = wr.getTableStartingWith("MID");
		assertEquals("MID", wt.getCellAsText(0, 0));
		assertEquals("", wt.getCellAsText(1, 0));
		assertEquals("Random", wt.getCellAsText(1, 1));
		assertEquals("Person", wt.getCellAsText(1, 2));
		wr.getForms()[2].getButtons()[0].click();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - View Obstetric Record", wr.getTitle());	
		
		wr.getForms()[1].submit();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Update Obstetric Visit Record", wr.getTitle());
		
		wr.getForms()[0].setParameter("weeksPregnant", "08-1");
		wr.getForms()[0].setParameter("bloodPressure", "100/60");
		wr.getForms()[0].setParameter("fetalHeartRate", "70");
		wr.getForms()[0].setParameter("fundalHeightUterus", "30.0");
		wr.getForms()[0].submit();
		
		wr = wc.getCurrentPage();
		assertEquals("iTrust - View Obstetric Record", wr.getTitle());
		
		assertEquals("08-1", wr.getTables()[1].getCellAsText(7, 1));
		assertEquals("100/60", wr.getTables()[1].getCellAsText(7, 2));
		assertEquals("70", wr.getTables()[1].getCellAsText(7, 3));
		assertEquals("30.0", wr.getTables()[1].getCellAsText(7, 4));
		
		//dumpWebTable(wr.getTables());
	}
	
	public void testAddObstetricsVisit() throws Exception{
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		wr = wr.getLinkWith("Obstetrics Information").click();
		assertEquals("iTrust - Please Select a Patient", wr.getTitle());
		wr.getForms()[1].setParameter("FIRST_NAME", "Random");
		wr.getForms()[1].setParameter("LAST_NAME", "Person");
		wr.getForms()[1].getButtons()[0].click();
		wr = wc.getCurrentPage();		
		WebTable wt = wr.getTableStartingWith("MID");
		assertEquals("MID", wt.getCellAsText(0, 0));
		assertEquals("", wt.getCellAsText(1, 0));
		assertEquals("Random", wt.getCellAsText(1, 1));
		assertEquals("Person", wt.getCellAsText(1, 2));
		wr.getForms()[2].getButtons()[0].click();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - View Obstetric Record", wr.getTitle());
		
		wr = wc.getResponse("http://localhost:8080/iTrust/auth/hcp-uap/addObstetricsVisit.jsp");
		wr.getForms()[0].setParameter("visitDate", "11/09/14");
		wr.getForms()[0].setParameter("weeksPregnant", "07-2");
		wr.getForms()[0].setParameter("bloodPressure", "100/65");
		wr.getForms()[0].setParameter("fetalHeartRate", "75");
		wr.getForms()[0].setParameter("fundalHeightUterus", "35.0");
		wr.getForms()[0].submit();
					
		wr = wc.getCurrentPage();
		assertEquals("iTrust - View Obstetric Record", wr.getTitle());
		
		assertEquals("07-2", wr.getTables()[1].getCellAsText(2, 1));
		assertEquals("07-2", wr.getTables()[1].getCellAsText(8, 1));
		assertEquals("100/65", wr.getTables()[1].getCellAsText(8, 2));
		assertEquals("75", wr.getTables()[1].getCellAsText(8, 3));
		assertEquals("35.0", wr.getTables()[1].getCellAsText(8, 4));
		
		
	}

}
