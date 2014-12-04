package edu.ncsu.csc.itrust.dao.officevisit;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

import edu.ncsu.csc.itrust.beans.DiagnosisStatisticsBean;
import edu.ncsu.csc.itrust.dao.mysql.DiagnosesDAO;
import edu.ncsu.csc.itrust.testutils.TestDAOFactory;

/**
 * tests for both original counts functions and diagnosis bean test
 * @author bchen11
 *
 */
public class ViewDiagnosisModifiedTest extends TestCase {
	//private OfficeVisitDAO ovDAO = TestDAOFactory.getTestInstance().getOfficeVisitDAO();
	private DiagnosesDAO diagDAO = TestDAOFactory.getTestInstance().getDiagnosesDAO();
	private int thisYear = Calendar.getInstance().get(Calendar.YEAR);
	

	@Test
	public void testTwoWeeksStatisticsValid() throws Exception {
		Date lower = new SimpleDateFormat("MM/dd/yyyy").parse("06/28/2011");
		Date upper = new SimpleDateFormat("MM/dd/yyyy").parse("09/28/2011");
		List<DiagnosisStatisticsBean> db = diagDAO.getWeeklyCounts("487.00", "27607", lower, upper);
		
		assertEquals(new SimpleDateFormat("MM/dd/yyyy").parse("06/27/2011"), db.get(0).getStartDate());
		assertEquals(new SimpleDateFormat("MM/dd/yyyy").parse("07/03/2011"), db.get(0).getEndDate());
		assertEquals(new SimpleDateFormat("MM/dd/yyyy").parse("09/26/2011"), db.get(db.size()-1).getStartDate());
		
		long totalRegion = 0;
		for (DiagnosisStatisticsBean d : db) {
			totalRegion += d.getRegionStats();
		}
		//If previous test fails, this test may fail
		long totalRegionNonsplit = diagDAO.getDiagnosisCounts("487.00", "27607", lower, upper).getRegionStats();
		assertEquals(totalRegionNonsplit, totalRegion);
	}
	
	@Test
	public void testTwoWeeksInvalid() throws Exception {
		Date lower = new SimpleDateFormat("MM/dd/yyyy").parse("06/28/2011");
		Date upper = new SimpleDateFormat("MM/dd/yyyy").parse("09/28/2011");
		List<DiagnosisStatisticsBean> db = diagDAO.getWeeklyCounts("487.00", "27607", lower, upper);
		
		assertEquals(new SimpleDateFormat("MM/dd/yyyy").parse("06/27/2011"), db.get(0).getStartDate());
		assertEquals(new SimpleDateFormat("MM/dd/yyyy").parse("07/03/2011"), db.get(0).getEndDate());
		assertEquals(new SimpleDateFormat("MM/dd/yyyy").parse("09/26/2011"), db.get(db.size()-1).getStartDate());
		
		long totalRegion = 0;
		for (DiagnosisStatisticsBean d : db) {
			totalRegion += d.getRegionStats();
		}
		//If previous test fails, this test may fail
		long totalRegionNonsplit = diagDAO.getDiagnosisCounts("487.00", "27607", lower, upper).getRegionStats();
		assertEquals(totalRegionNonsplit, totalRegion);
	}
	
	
	@Test
	public void testHasInfluenza() throws Exception {
		Date start = new SimpleDateFormat("MM/dd/yyyy").parse("11/02/2014");
		DiagnosisStatisticsBean db = diagDAO.getPreviousTwoWeeksRecord("487.00", "27607",start);
		assertEquals(new SimpleDateFormat("MM/dd/yyyy").parse("10/19/2014"), db.getStartDate());
	}
	
	@Test
	public void testHasMalaria() throws Exception {
		Date start = new SimpleDateFormat("MM/dd/yyyy").parse("11/02/2014");
		DiagnosisStatisticsBean db = diagDAO.getPreviousTwoWeeksRecord("84.5", "27607",start);
		assertEquals(new SimpleDateFormat("MM/dd/yyyy").parse("10/19/2014"), db.getStartDate());
	}
	
	
}
