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
		wr = wr.getLinkWith("Send Appointment Reminders").click();
		assertEquals("iTrust - Send Appointment Reminders", wr.getTitle());
		
		wr.getForms()[0].setParameter("days", "14");
		wr = wr.getForms()[0].submit();
		
		wr = wr.getLinkWith("View Appointment Reminders").click();
		assertEquals("iTrust - View Appointment Reminders", wr.getTitle());
		
		assertTrue(wr.getTableWithID("resultstable").getRows()[1].getText().contains("andy.programmer@gmail.com"));
		wr = wr.getLinkWithID("0").click();
		assertEquals("iTrust - View Message", wr.getTitle());
		
		assertTrue(wr.getText().contains("Andy Programmer, You have an appointment on 10:30:00, 2014-12-18 with Dr. Kelly Doctor "));
	}
}
