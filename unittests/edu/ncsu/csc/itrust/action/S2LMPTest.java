package edu.ncsu.csc.itrust.action;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import junit.framework.TestCase;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import edu.ncsu.csc.itrust.action.ObstetricsInfoAction;

public class S2LMPTest extends TestCase{
	@Rule
  	public ExpectedException exception = ExpectedException.none();

	private Calendar cal;
	private SimpleDateFormat formatter;
	@Override
	protected void setUp() throws Exception {
		cal = Calendar.getInstance();
		formatter = new SimpleDateFormat("dd/MM/yyyy");
	}

	public void testInvalidDate1(){
		try{
			ObstetricsInfoAction.calculateEDDAndWeek("abcsdw");
			fail("No Exception Thrown");
		}catch(Exception e){
			//Not a date format
		}
	}
	
	public void testInvalidDate2(){
		try{
			ObstetricsInfoAction.calculateEDDAndWeek("-2/11/2032");
			fail("No Exception Thrown");
		}catch(Exception e){
			//Not a date format
		}
	}
	
	public void testInvalidDate3(){
		try{
			ObstetricsInfoAction.calculateEDDAndWeek("23/13/2012");
			fail("No Exception Thrown");
		}catch(Exception e){
			//Not a proper date
		}
	}

	
//	@Test
//	public void testvalidDateSame() throws Exception {
//		//same date testing
//		assertEquals(ObstetricsInfoAction.calculateEDDAndWeek(formatter.format(cal.getTime()))[2], "0");
//		assertEquals(ObstetricsInfoAction.calculateEDDAndWeek(formatter.format(cal.getTime()))[1], "0");
//		String curr = formatter.format(cal.getTime());
//		cal.add(Calendar.DATE, 280);
//		assertEquals(ObstetricsInfoAction.calculateEDDAndWeek(curr)[0], formatter.format(cal.getTime()));
//
//	}
	//These need to be rewritten with the new way we throw exceptions in CalculateEDDandWeek
//	@Test
//	public void testvalidDateDiff() throws Exception {
//		cal.add(Calendar.DATE, 25);
//		String curr = formatter.format(cal.getTime());
//		try{
//			ObstetricsInfoAction.calculateEDDAndWeek(curr);
//			fail("No Exception Thrown");
//		}
//		catch(Exception e){
//			//Invalid Date difference
//		}
//		cal.add(Calendar.DATE, -25);
//		curr = formatter.format(cal.getTime());
//		assertEquals(ObstetricsInfoAction.calculateEDDAndWeek(curr)[1], "0");
//		
//	}
	
	
	
}
