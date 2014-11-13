package edu.ncsu.csc.itrust.action;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import edu.ncsu.csc.itrust.beans.PersonnelBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.testutils.TestDAOFactory;
import junit.framework.TestCase;

public class ZipCodeActionTest extends TestCase
{
	private ZipCodeAction zipCodeAction; 
	private TestDataGenerator gen = new TestDataGenerator();
	private long loggedInMID = 2;
	protected void setUp() throws FileNotFoundException, SQLException, IOException
	{
		gen = new TestDataGenerator();
		zipCodeAction = new ZipCodeAction(TestDAOFactory.getTestInstance(), loggedInMID);
		gen.clearAllTables();
		gen.standardData();
//		gen.testExpertSearch();
	}
	
	public void testGetExperts() throws DBException
	{
		List<PersonnelBean> physicians = zipCodeAction.getExperts("OB/GYN", "10453", "250");
		assertEquals(0, physicians.size());
		physicians = zipCodeAction.getExperts("OB/GYN", "10453", "500");
		physicians.get(0).getFullName().equals("Kelly Doctor");
	}
}
