package edu.ncsu.csc.itrust.http;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;
import edu.ncsu.csc.itrust.enums.TransactionType;

public class TransactionLogWebTest extends iTrustHTTPTest {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.uap1();
		gen.patient2();
		gen.patient1();
		gen.patient4();
		gen.hcp0();
		gen.hcp3();
		gen.er4();
		gen.tester();
	}
	
	public void testLogDisplay() throws Exception {
		WebConversation wc = login("9999999999", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Tester Home", wr.getTitle());
		assertEquals(ADDRESS + "auth/tester/home.jsp", wr.getURL().toString());

		//assertLogged(TransactionType.LOGIN_SUCCESS, 9999999999L, 0L, "");
		wr = wr.getLinkWith("View transaction log").click();
		assertEquals(ADDRESS + "auth/tester/queryLog.jsp", wr.getURL().toString());
		WebForm query = wr.getForms()[0];
		
		query.getScriptableObject().setParameterValue("startDate", "12/01/2014");
		query.getScriptableObject().setParameterValue("endDate", "12/31/2014");
		query.getScriptableObject().setParameterValue("userRole1", "Tester");
		query.getScriptableObject().setParameterValue("userRole2", "Tester");
		query.getScriptableObject().setParameterValue("transactionType","300");
		
		System.out.println("button name is " + query.getButtons()[2].getValue());
		query.getButtons()[2].click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/tester/logDisplay.jsp", wr.getURL().toString());
		WebTable table = wr.getTableWithID("log");
		assertTrue(table.getText().contains("LOGIN_SUCCESS"));
		assertTrue(table.getText().contains("9999999999"));
		assertTrue(table.getText().contains("300"));
	}
	
	
	public void testVisualLog() throws Exception {
		WebConversation wc = login("9999999999", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Tester Home", wr.getTitle());
		assertEquals(ADDRESS + "auth/tester/home.jsp", wr.getURL().toString());

		//assertLogged(TransactionType.LOGIN_SUCCESS, 9999999999L, 0L, "");
		wr = wr.getLinkWith("View transaction log").click();
		assertEquals(ADDRESS + "auth/tester/queryLog.jsp", wr.getURL().toString());
		WebForm query = wr.getForms()[0];
		
		query.getScriptableObject().setParameterValue("startDate", "11/21/2011");
		query.getScriptableObject().setParameterValue("endDate", "11/21/2013");
		query.getScriptableObject().setParameterValue("userRole1", "HCP");
		query.getScriptableObject().setParameterValue("userRole2", "Tester");
		query.getScriptableObject().setParameterValue("transactionType","300");
		
		System.out.println("button 3 name is " + query.getButtons()[3].getValue());
		query.getButtons()[3].click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/tester/visualLog.jsp", wr.getURL().toString());
		assertTrue(wr.getText().contains("Role+and+Transaction+For+Logged+In+User"));
		assertTrue(wr.getText().contains("Role+and+Transaction+For+Secondary+User"));

	}
	
	public void testInvalidDate() throws Exception {
		WebConversation wc = login("9999999999", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Tester Home", wr.getTitle());
		assertEquals(ADDRESS + "auth/tester/home.jsp", wr.getURL().toString());

		//assertLogged(TransactionType.LOGIN_SUCCESS, 9999999999L, 0L, "");
		wr = wr.getLinkWith("View transaction log").click();
		assertEquals(ADDRESS + "auth/tester/queryLog.jsp", wr.getURL().toString());
		WebForm query = wr.getForms()[0];
		
		query.getScriptableObject().setParameterValue("startDate", "2911/22/2014");
		query.getButtons()[3].click();
		wr = wc.getCurrentPage();
		
		assertTrue(wr.getText().contains("Date must by in the format: MM/dd/yyyy"));
	}
	


	
	

}
