package edu.ncsu.csc.itrust.http;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebLink;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;
import edu.ncsu.csc.itrust.enums.TransactionType;

public class SendAppointmentRemindersTest extends iTrustHTTPTest {
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.admin1();
		gen.hcp0();
		gen.hcp3();
		gen.patient1();
		gen.patient2();
		gen.patient4();
		gen.appointmentType();
		gen.appointment();
	}
	

	public void testCreateValidReminder() throws Exception {
		// login admin
		WebConversation wc = login("9000000001", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Admin Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000001L, 0L, "");
		
		// click on send reminders
		wr = wr.getLinkWith("Send Appointment Reminders").click();
		// enter the number of days
		assertEquals("iTrust - Send Appointment Reminders", wr.getTitle());
		WebForm form = wr.getForms()[0];
		form.setParameter("days", "8");
		wr = form.submit();
		assertTrue(wr.getText().contains("Appointments Sent Successfully"));
		assertLogged(TransactionType.SEND_REMINDERS, 9000000001L, 0, "");
		
		//confirm email was sent
		wr = wr.getLinkWith("Show Fake Emails").click();
		assertTrue(wr.getText().contains("System Reminder"));
	}
	
	public void testCreateInvalidReminder() throws Exception {
		// login admin
		WebConversation wc = login("9000000001", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Admin Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000001L, 0L, "");
		
		// click on send reminders
		wr = wr.getLinkWith("Send Appointment Reminders").click();
		// enter the number of days
		assertEquals("iTrust - Send Appointment Reminders", wr.getTitle());
		WebForm form = wr.getForms()[0];
		form.setParameter("days", "-2");
		wr = form.submit();

		assertTrue(wr.getText().contains("The number of days must be greater than 0"));
		
		//confirm email was  not sent
		wr = wr.getLinkWith("Show Fake Emails").click();
		assertTrue(!(wr.getText().contains("System Reminder")));
	}
}