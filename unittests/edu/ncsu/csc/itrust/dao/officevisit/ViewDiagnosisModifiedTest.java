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
	public void testHasInfluenza() throws Exception {
		Date start = new SimpleDateFormat("MM/dd/yyyy").parse("11/02/2014");
		DiagnosisStatisticsBean db = diagDAO.getPreviousTwoWeeksRecord("487.00", "27607",start);
		assertEquals(new SimpleDateFormat("MM/dd/yyyy").parse("10/19/2014"), db.getStartDate());
		assertEquals(new SimpleDateFormat("MM/dd/yyyy").parse("11/02/2014"), db.getEndDate());
		long totalRegionNonsplit = diagDAO.getDiagnosisCounts("487.00", "27607",new SimpleDateFormat("MM/dd/yyyy").parse("10/19/2014"), new SimpleDateFormat("MM/dd/yyyy").parse("11/02/2014") ).getRegionStats();
		assertEquals(totalRegionNonsplit, db.getRegionStats());
	}
	
	@Test
	public void testHasMalaria() throws Exception {
		Date start = new SimpleDateFormat("MM/dd/yyyy").parse("11/02/2014");
		DiagnosisStatisticsBean db = diagDAO.getPreviousTwoWeeksRecord("84.5", "27607",start);
		assertEquals(new SimpleDateFormat("MM/dd/yyyy").parse("10/19/2014"), db.getStartDate());
		assertEquals(new SimpleDateFormat("MM/dd/yyyy").parse("11/02/2014"), db.getEndDate());
		long totalRegionNonsplit = diagDAO.getDiagnosisCounts("84.5", "27607",new SimpleDateFormat("MM/dd/yyyy").parse("10/19/2014"), new SimpleDateFormat("MM/dd/yyyy").parse("11/02/2014") ).getRegionStats();
		assertEquals(totalRegionNonsplit, db.getRegionStats());
	}
	
	
}
