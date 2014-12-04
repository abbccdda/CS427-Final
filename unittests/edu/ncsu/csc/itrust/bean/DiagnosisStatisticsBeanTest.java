package edu.ncsu.csc.itrust.bean;

import junit.framework.TestCase;
import edu.ncsu.csc.itrust.beans.DiagnosisStatisticsBean;

public class DiagnosisStatisticsBeanTest extends TestCase {

	public void testZipCode() throws Exception {
		DiagnosisStatisticsBean dsBean = new DiagnosisStatisticsBean();
		dsBean.setZipCode("12345");
		assertEquals("12345", dsBean.getZipCode());
	}
	
	public void testZipCount() throws Exception {
		DiagnosisStatisticsBean dsBean = new DiagnosisStatisticsBean();
		dsBean.setZipStats(10);
		assertEquals(10, dsBean.getZipStats());
	}
	
	public void testRegionCount() throws Exception {
		DiagnosisStatisticsBean dsBean = new DiagnosisStatisticsBean();
		dsBean.setRegionStats(4);
		assertEquals(4, dsBean.getRegionStats());
	}
	
	public void testStateCount() throws Exception {
		DiagnosisStatisticsBean dsBean = new DiagnosisStatisticsBean();
		dsBean.setStateStats(4);
		assertEquals(4, dsBean.getStateStats());
	}

	public void testDatabaseCount() throws Exception {
		DiagnosisStatisticsBean dsBean = new DiagnosisStatisticsBean();
		dsBean.setDatabaseStats(4);
		assertEquals(4, dsBean.getDatabaseStats());
	}
	
	public void testAll() throws Exception {
		DiagnosisStatisticsBean dsBean = new DiagnosisStatisticsBean("12346", 2, 5, 3, 4);
		assertEquals("12346", dsBean.getZipCode());
		assertEquals(2, dsBean.getZipStats());
		assertEquals(5, dsBean.getRegionStats());
		assertEquals(3, dsBean.getStateStats());
		assertEquals(4, dsBean.getDatabaseStats());
	}
	
}
