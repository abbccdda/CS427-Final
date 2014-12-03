package edu.ncsu.csc.itrust.http;

import static org.junit.Assert.*;

import org.junit.Test;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;

import edu.ncsu.csc.itrust.enums.TransactionType;

public class ViewMessageRemindersTest extends iTrustHTTPTest{

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.standardData();
	}

	/**
	 * testAddER
	 * @throws Exception
	 */
	public void testViewMessageValid() throws Exception {
		// login admin
		WebConversation wc = login("9000000001", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Admin Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000001L, 0L, "");
		
		// click on View Appointment Reminders
		wr = wr.getLinkWith("View Appointment Reminders").click();
		assertEquals("iTrust - View Appointment Reminders", wr.getTitle());
		System.out.println(wr.getTableWithID("results").getRows().length);
		//assertTrue(wr.getTableWithID("results").getRows().length == 9);
		
	}
	
	public void testViewMessageLargeNumber() throws Exception {
		WebConversation wc = login("9000000001", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Admin Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000001L, 0L, "");
		
		// click on View Appointment Reminders
		wr = wr.getLinkWith("View Appointment Reminders").click();
		assertEquals("iTrust - View Appointment Reminders", wr.getTitle());

		
		
		
	}
	
	public void testViewMessageWithNumberID() throws Exception {
		WebConversation wc = login("9000000001", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Admin Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000001L, 0L, "");
		
		// click on View Appointment Reminders
		wr = wr.getLinkWith("View Appointment Reminders").click();
		assertEquals("iTrust - View Appointment Reminders", wr.getTitle());
	}
}
