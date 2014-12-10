package edu.ncsu.csc.itrust.dao.transaction;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import junit.framework.TestCase;
import edu.ncsu.csc.itrust.beans.TransactionBean;
import edu.ncsu.csc.itrust.dao.mysql.TransactionDAO;
import edu.ncsu.csc.itrust.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.enums.TransactionType;
import edu.ncsu.csc.itrust.testutils.TestDAOFactory;

public class transactionModifiedTest extends TestCase {
	private TransactionDAO tranDAO = TestDAOFactory.getTestInstance()
			.getTransactionDAO();
	private TransactionDAO normalDAO = TestDAOFactory.getTestInstance()
			.getTransactionDAO();

	private TestDataGenerator gen;

	@Override
	protected void setUp() throws Exception {
		gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.transactionLog();
		gen.patient1();

	}

	public void testLogGeneralInvalid() throws Exception {
		List<TransactionBean> list = tranDAO.getLog("HCP", "HCP", "", new Date(
				11022011), new Date(11052011));
		;
		assertEquals(0, list.size());
		assertEquals(1, list.hashCode());
	}

	public void testGetAllTransactions() throws Exception {
		List<TransactionBean> list = tranDAO.getAllTransactions();
		assertEquals(8, list.size());
		// that last one inserted should be last because it was backdated
		assertEquals(1L, list.get(3).getLoggedInMID());
		assertEquals(TransactionType.DEMOGRAPHICS_EDIT, list.get(3)
				.getTransactionType());
	}

	public void testLogSimple() throws Exception {
		tranDAO.logTransaction(TransactionType.LOGIN_SUCCESS, 9000000000L);
		List<TransactionBean> list = tranDAO.getAllTransactions();
		assertEquals(9, list.size());
		assertEquals(9000000000L, list.get(0).getLoggedInMID());
		assertEquals(0L, list.get(0).getSecondaryMID());
		assertEquals("", list.get(0).getAddedInfo());
		assertEquals(TransactionType.LOGIN_SUCCESS, list.get(0)
				.getTransactionType());
	}

	public void testLogTimeInvalid() throws Exception {
		HashMap<String, Long> map = tranDAO.getLogByTime("HCP", "HCP", "",
				new Date(11022011), new Date(11052011));
		;
		assertEquals(0, map.size());
		assertEquals(0, map.hashCode());
	}

	public void testGetLogByType() throws Exception {

		List<TransactionBean> result = null;

		HashMap<String, Long> b = normalDAO.getLogByType("patient", "patient",
				"DEMOGRAPHICS_EDIT", new SimpleDateFormat("MM/dd/yyyy")

				.parse("06/23/2007"),
				new SimpleDateFormat("MM/dd/yyyy").parse("06/24/2007"));

		assertEquals(null, b.get(TransactionType.DEMOGRAPHICS_EDIT));

		/**
		 * catch(DBException e){
		 * 
		 * String err =
		 * "Context Lookup Naming Exception: Need to specify class name in environment or system property,"
		 * 
		 * + " or as an applet parameter,"
		 * 
		 * + " or in an application resource file:  java.naming.factory.initial"
		 * ;
		 * 
		 * 
		 * }
		 **/

	}

	public void testGetLogByRole() throws Exception {

		List<TransactionBean> result = null;

		HashMap<String, Long> g = normalDAO.getLogForRole("patient", "patient",
				new SimpleDateFormat("MM/dd/yyyy")

				.parse("06/23/2007"),
				new SimpleDateFormat("MM/dd/yyyy").parse("06/24/2007"), false);

		assertEquals(null, g.get(TransactionType.DEMOGRAPHICS_EDIT));

		/**
		 * catch(DBException e){
		 * 
		 * String err =
		 * "Context Lookup Naming Exception: Need to specify class name in environment or system property,"
		 * 
		 * + " or as an applet parameter,"
		 * 
		 * + " or in an application resource file:  java.naming.factory.initial"
		 * ;
		 * 
		 * 
		 * }
		 **/

	}

	public void testGetTransactionsAffecting() throws Exception {

		String time = "2007-06-23 06:54:59";

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		java.util.Date date = formatter.parse(time);

		List<TransactionBean> result2;

		result2 = normalDAO.getTransactionsAffecting(0L, 9000000000L, date, 3);

		assertEquals(3, result2.size());

		assertEquals(1L, result2.get(0).getLoggedInMID());

	}

	public void testGetLog() throws Exception {

		List<TransactionBean> result = null;

		result = normalDAO.getLog("patient", "patient", "DEMOGRAPHICS_EDIT",
				new SimpleDateFormat("MM/dd/yyyy")

				.parse("06/23/2007"),
				new SimpleDateFormat("MM/dd/yyyy").parse("06/24/2007"));

		assertEquals(0, result.size());

		/**
		 * catch(DBException e){
		 * 
		 * String err =
		 * "Context Lookup Naming Exception: Need to specify class name in environment or system property,"
		 * 
		 * + " or as an applet parameter,"
		 * 
		 * + " or in an application resource file:  java.naming.factory.initial"
		 * ;
		 * 
		 * 
		 * }
		 **/

	}

}
