package edu.ncsu.csc.itrust.action;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import junit.framework.TestCase;

import org.junit.Test;

import edu.ncsu.csc.itrust.action.ObstetricsInfoAction;

public class S2LMPTest extends TestCase{

	private Calendar cal;
	private SimpleDateFormat formatter;
	@Override
	protected void setUp() throws Exception {
		cal = Calendar.getInstance();
		formatter = new SimpleDateFormat("dd/MM/yyyy");
	}

	@Test
	public void testInvalidReturn() {
		assertEquals(ObstetricsInfoAction.errorResult()[0], "invalid input, try again");
	}
	
	@Test
	public void testInvalidDate() {
		//not date format
		assertEquals(ObstetricsInfoAction.calculateEDDAndWeek("abcsdw")[0], "invalid input, try again");
		assertEquals(ObstetricsInfoAction.calculateEDDAndWeek("-2/11/2032")[0], "invalid input, try again");

		//date format but not satisfying the requirement
		assertEquals(ObstetricsInfoAction.calculateEDDAndWeek("23/4")[0], "invalid input, try again");
		//date format but not satisfying the requirement
		assertEquals(ObstetricsInfoAction.calculateEDDAndWeek("23/13/2012")[0], "invalid input, try again");
		
		assertEquals(ObstetricsInfoAction.calculateEDDAndWeek("23/10/-2012")[0], "invalid input, try again");
	}
	
	@Test
	public void testvalidDateSame() {
		//same date testing
		assertEquals(ObstetricsInfoAction.calculateEDDAndWeek(formatter.format(cal.getTime()))[2], "0");
		assertEquals(ObstetricsInfoAction.calculateEDDAndWeek(formatter.format(cal.getTime()))[1], "0");
		String curr = formatter.format(cal.getTime());
		cal.add(Calendar.DATE, 280);
		assertEquals(ObstetricsInfoAction.calculateEDDAndWeek(curr)[0], formatter.format(cal.getTime()));

	}
	
	@Test
	public void testvalidDateDiff() {
		cal.add(Calendar.DATE, 25);
		String curr = formatter.format(cal.getTime());
		assertEquals(ObstetricsInfoAction.calculateEDDAndWeek(curr)[1], null);
		
		cal.add(Calendar.DATE, -25);
		curr = formatter.format(cal.getTime());
		assertEquals(ObstetricsInfoAction.calculateEDDAndWeek(curr)[1], "0");
		
	}
	
	
	
}
