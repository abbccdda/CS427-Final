package edu.ncsu.csc.itrust.beans;

import java.io.Serializable;

public class ObstetricsVisitBean implements Serializable, Comparable<ObstetricsBean> {
	private static final long serialVersionUID = 8200500237498907126L;
	private long MID = 0;
	String visitDate;
	String weeksPregnant;
	int bloodPressure;
	int fetalHeartRate;
	long fundalHeightUterus;
	
	@Override
	public int compareTo(ObstetricsBean o) {
		// TODO Auto-generated method stub
		return 0;
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
	public int getBloodPressure() {
		return bloodPressure;
	}
	public void setBloodPressure(int bloodPressure) {
		this.bloodPressure = bloodPressure;
	}
	public int getFetalHeartRate() {
		return fetalHeartRate;
	}
	public void setFetalHeartRate(int fetalHeartRate) {
		this.fetalHeartRate = fetalHeartRate;
	}
	public long getFundalHeightUterus() {
		return fundalHeightUterus;
	}
	public void setFundalHeightUterus(long fundalHeightUterus) {
		this.fundalHeightUterus = fundalHeightUterus;
	}
	
}
