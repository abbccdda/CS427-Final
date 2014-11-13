package edu.ncsu.csc.itrust.dao.transaction;

import java.util.Date;
import java.util.List;

import junit.framework.TestCase;
import edu.ncsu.csc.itrust.beans.TransactionBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.TransactionDAO;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.testutils.EvilDAOFactory;
public class TransactionDAOTest extends TestCase {
	private TransactionDAO evilDAO = EvilDAOFactory.getEvilInstance().getTransactionDAO();
	private TransactionDAO normalDAO = DAOFactory.getProductionInstance().getTransactionDAO();

	@Override
	protected void setUp() throws Exception {
	}

	public void testGetAllAccessException() throws Exception {
		try {
			evilDAO.getAllRecordAccesses(0L, -1, false);
			fail("DBException should have been thrown");
		} catch (DBException e) {
			assertEquals(EvilDAOFactory.MESSAGE, e.getSQLException().getMessage());
		}
	}

	public void testAllTransactionsException() throws Exception {
		try {
			evilDAO.getAllTransactions();
			fail("DBException should have been thrown");
		} catch (DBException e) {
			assertEquals(EvilDAOFactory.MESSAGE, e.getSQLException().getMessage());
		}
	}
	
	public void testInValidTransaction() throws Exception{
		List<TransactionBean> result = null;
		try{
			normalDAO.getLog("tester", "tester", "LOGIN_SUCCESS", new Date(),new Date());	
		}	
		catch(DBException e){
			String err = "Context Lookup Naming Exception: Need to specify class name in environment or system property,"
					+ " or as an applet parameter,"
					+ " or in an application resource file:  java.naming.factory.initial";
			assertEquals(err, e.getSQLException().getMessage());
		}
	}
}	