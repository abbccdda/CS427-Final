package edu.ncsu.csc.itrust.action;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import edu.ncsu.csc.itrust.beans.HospitalBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.HospitalsDAO;
import edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO;

public class ViewHCPProfileAction {
	private PersonnelDAO personnelDAO;
	private HospitalsDAO hospitalsDAO;
	
	public ViewHCPProfileAction(DAOFactory factory){
		hospitalsDAO = new HospitalsDAO(factory);
		personnelDAO = new PersonnelDAO(factory);
	}
	
	public List<HospitalBean> findHospitalsAssignedToHCP(long pid) throws SQLException{
		return hospitalsDAO.getHospitalsAssignedToPhysician(pid);
	}
	
	/**
	 * Checks whether the HCP with mid has a photo assigned 
	 * @param pid
	 * @return
	 */
	public boolean hasPhotoAssigned(long mid){
		String dir = "WebRoot/image/user/" + String.valueOf(mid) + ".png";
		File f = new File(dir);
		if(f.exists() && !f.isDirectory()) {
			System.out.println("viecotry");
			return true;
		}
		else{
			File a = new File(".");
			System.out.println(dir);
			return false;
		}
	}
}
