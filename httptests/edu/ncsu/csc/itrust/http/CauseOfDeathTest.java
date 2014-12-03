package edu.ncsu.csc.itrust.http;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;

import edu.ncsu.csc.itrust.enums.TransactionType;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

import org.junit.Test;


public class CauseOfDeathTest extends iTrustHTTPTest{
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.standardData();
	}

	
	public void testValidDateMales() throws Exception {
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		// click on Cause of Death Trends
		wr = wr.getLinkWith("Cause of Death Trends").click();
		assertEquals("iTrust - Cause of Death Trends", wr.getTitle());
		
		wr.getForms()[0].setParameter("sortby", "male");
		wr.getForms()[0].setParameter("startDate", "2014");
	}
	
	public void testValidDateAll() throws Exception {
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		// click on Cause of Death Trends
		wr = wr.getLinkWith("Cause of Death Trends").click();
		assertEquals("iTrust - Cause of Death Trends", wr.getTitle());
		
		wr.getForms()[0].setParameter("sortby", "all genders");
		wr.getForms()[0].setParameter("startDate", "2014");
	}
	
	public void testValidDateFemales() throws Exception {
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		// click on Cause of Death Trends
		wr = wr.getLinkWith("Cause of Death Trends").click();
		assertEquals("iTrust - Cause of Death Trends", wr.getTitle());
		
		wr.getForms()[0].setParameter("sortby", "female");
		wr.getForms()[0].setParameter("startDate", "2014");
	}
	
	public void testInalidDateNumber() throws Exception {
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		// click on Cause of Death Trends
		wr = wr.getLinkWith("Cause of Death Trends").click();
		assertEquals("iTrust - Cause of Death Trends", wr.getTitle());
		
		wr.getForms()[0].setParameter("sortby", "male");
		wr.getForms()[0].setParameter("startDate", "201423");
	}
	
	public void testInalidDateLetter() throws Exception {
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		// click on Cause of Death Trends
		wr = wr.getLinkWith("Cause of Death Trends").click();
		assertEquals("iTrust - Cause of Death Trends", wr.getTitle());
		
		wr.getForms()[0].setParameter("sortby", "male");
		wr.getForms()[0].setParameter("startDate", "abcdef");
	}
}
