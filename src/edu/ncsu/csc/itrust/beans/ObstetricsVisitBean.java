package edu.ncsu.csc.itrust.beans;

import java.io.Serializable;

public class ObstetricsVisitBean implements Serializable, Comparable<ObstetricsBean> {
	private static final long serialVersionUID = 8200500237498907126L;
	private long id = 0;
	private long obid = 0;
	private long MID = 0;
	String visitDate;
	String weeksPregnant;
	String bloodPressure;
	int fetalHeartRate;
	double fundalHeightUterus;
	
	@Override
	public int compareTo(ObstetricsBean o) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public long getObid() {
		return obid;
	}


	public void setObid(long obid) {
		this.obid = obid;
	}


	public long getMID() {
		return MID;
	}
	public void setMID(long mID) {
		MID = mID;
	}
	public String getVisitDate() {
		return visitDate;
	}
	public void setVisitDate(String visitDate) {
		this.visitDate = visitDate;
	}
	public String getWeeksPregnant() {
		return weeksPregnant;
	}
	public void setWeeksPregnant(String weeksPregnant) {
		this.weeksPregnant = weeksPregnant;
	}
	public String getBloodPressure() {
		return bloodPressure;
	}
	public void setBloodPressure(String bloodPressure) {
		this.bloodPressure = bloodPressure;
	}
	public int getFetalHeartRate() {
		return fetalHeartRate;
	}
	public void setFetalHeartRate(int fetalHeartRate) {
		this.fetalHeartRate = fetalHeartRate;
	}
	public double getFundalHeightUterus() {
		return fundalHeightUterus;
	}
	public void setFundalHeightUterus(double fundalHeightUterus) {
		this.fundalHeightUterus = fundalHeightUterus;
	}
	
}
