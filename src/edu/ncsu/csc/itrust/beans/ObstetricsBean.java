package edu.ncsu.csc.itrust.beans;

import java.io.Serializable;

public class ObstetricsBean implements Serializable, Comparable<ObstetricsBean> {
	private static final long serialVersionUID = -1939805819813678364L;
	
	private long MID = 0;
	private int yearOfConception;
	private String weeksPregnant;
	private double hoursLabor;
	private String deliveryMethod;
	
	@Override
	public int compareTo(ObstetricsBean o) {
		return (int)(o.MID-this.MID);
	}
	
	public long getMID() {
		return MID;
	}

	public void setMID(long mID) {
		MID = mID;
	}

	public int getYearOfConception() {
		return yearOfConception;
	}

	public void setYearOfConception(int yearOfConception) {
		this.yearOfConception = yearOfConception;
	}

	public String getWeeksPregnant() {
		return weeksPregnant;
	}

	public void setWeeksPregnant(String weeksPregnant) {
		this.weeksPregnant = weeksPregnant;
	}

	public double getHoursLabor() {
		return hoursLabor;
	}

	public void setHoursLabor(double hoursLabor) {
		this.hoursLabor = hoursLabor;
	}

	public String getDeliveryMethod() {
		return deliveryMethod;
	}

	public void setDeliveryMethod(String deliveryMethod) {
		this.deliveryMethod = deliveryMethod;
	}
}
