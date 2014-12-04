package edu.ncsu.csc.itrust.action;

import junit.framework.TestCase;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.testutils.TestDAOFactory;

public class ViewHCPProfileActionTest extends TestCase {
	private DAOFactory factory = TestDAOFactory.getTestInstance();
	private ViewHCPProfileAction action;
	@Override
	protected void setUp() throws Exception {
		action = new ViewHCPProfileAction(factory);
	}
	
	/**
	 * Affirmative test on a hcp that has a photo
	 */
	public void testHasPhoto(){
		long mid = 9000000003L;
		boolean exists = action.hasPhotoAssigned(mid);
		assertEquals(true, exists);
	}
	/**
	 * Negative test on a hcp that has no photo
	 */
	public void testHasNoPhoto(){
		long mid = 9000000009L;
		boolean exists = action.hasPhotoAssigned(mid);
		assertEquals(false, exists);
	}
}
