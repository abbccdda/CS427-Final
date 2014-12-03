package edu.ncsu.csc.itrust.beans;

/**
 * Bean for cause of death. Contains HCP MID, gender, cause of death icd code, cause of death description,
 * and count of a certain cause of death.
 * @author Steve
 *
 */
public class CauseOfDeathBean {
	
	private long hcp;
	private String gender = null;
	private String code = null;
	private String description = null;
	private int count = 0;
	
	public long getHCP(){
		return this.hcp;
	}
	
	public void setHCP(long mid){
		this.hcp = mid;
	}
	
	public String getGender(){
		return this.gender;
	}
	
	public void setGender(String gender){
		this.gender = gender;
	}
	
	public String getCode(){
		return this.code;
	}
	
	public void setCode(String code){
		this.code = code;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	public void setDescription(String description){
		this.description = description;
	}
	
	public int getCount(){
		return this.count;
	}
	
	public void setCount(int count){
		this.count = count;
	}
	
}
