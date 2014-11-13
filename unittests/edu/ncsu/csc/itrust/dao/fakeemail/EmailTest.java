package edu.ncsu.csc.itrust.dao.fakeemail;

import java.util.Arrays;
import java.util.List;
import junit.framework.TestCase;
import edu.ncsu.csc.itrust.EmailUtil;
import edu.ncsu.csc.itrust.beans.Email;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.testutils.TestDAOFactory;

public class EmailTest extends TestCase {
	DAOFactory factory = TestDAOFactory.getTestInstance();

	@Override
	protected void setUp() throws Exception {
		TestDataGenerator gen = new TestDataGenerator();
		gen.clearFakeEmail();
		gen.fakeEmail();
	}

	public void testListAllEmails() throws Exception {
		List<Email> emails = factory.getFakeEmailDAO().getAllEmails();
		assertEquals(6, emails.size());
		Email email = getTestEmail();
		new EmailUtil(factory).sendEmail(email);
		emails = factory.getFakeEmailDAO().getAllEmails();
		assertEquals(7, emails.size());
		assertEquals(getTestEmail(), emails.get(0));
	}

	public void testListEmailsByPerson() throws Exception {
		String email = "noreply@itrust.com";
		List<Email> emails = factory.getFakeEmailDAO().getEmailsByPerson(email);
		assertEquals(4, emails.size());
		assertEquals("this is an email", emails.get(0).getSubject());
		assertEquals("this is another email", emails.get(1).getSubject());
	}

	public void testFindWithString() throws Exception {
		factory.getFakeEmailDAO().sendEmailRecord(getTestEmail());
		factory.getFakeEmailDAO().sendEmailRecord(getTestEmail());
		Email other = getTestEmail();
		other.setBody("");
		// assertEquals(true, factory.getFakeEmailDAO().sendRealEmail(other ));
		factory.getFakeEmailDAO().sendEmailRecord(other);
		List<Email> emails = factory.getFakeEmailDAO().getEmailWithBody("is the");
		assertEquals(2, emails.size());
		assertEquals(getTestEmail(), emails.get(0));
		assertEquals(getTestEmail(), emails.get(1));
	}

	private Email getTestEmail() {
		Email email = new Email();
		email.setBody("this is the body");
		email.setFrom("ncsucsc326@gmail.com");
		email.setSubject("this is the subject");
		email.setToList(Arrays.asList("ncsucsc326@gmail.com"));
		return email;
	}
	
	public void testSendReminders() throws Exception{
		TestDataGenerator gen = new TestDataGenerator();
		gen.appointmentCase1();
		factory.getFakeEmailDAO().sendReminderEmails(14);
		List<Email> emails = factory.getFakeEmailDAO().getEmailsByPerson("System Reminder");
		Email test = emails.get(0);
		assertEquals(test.getFrom(), "System Reminder");
	}

}
