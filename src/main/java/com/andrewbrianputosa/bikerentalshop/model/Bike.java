package com.andrewbrianputosa.bikerentalshop.model;

import org.joda.time.DateTime;

import com.couchbase.client.deps.com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties
public class Bike {
	
	private String bikeId;
	private String customerName;
	private DateTime checkOutTime;
	private DateTime checkInTime;
	private Integer totalTimeSpent;
	
	
	
	
	public Bike() {
		
	}

	public Bike(String bikeId, String customerName, DateTime checkOutTime, DateTime checkInTime,
			Integer totalTimeSpent) {
		super();
		this.bikeId = bikeId;
		this.customerName = customerName;
		this.checkOutTime = checkOutTime;
		this.checkInTime = checkInTime;
		this.totalTimeSpent = totalTimeSpent;
	}
	
	public String getBikeId() {
		return bikeId;
	}
	public void setBikeId(String bikeId) {
		this.bikeId = bikeId;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public DateTime getCheckOutTime() {
		return checkOutTime;
	}
	public void setCheckOutTime(DateTime checkOutTime) {
		this.checkOutTime = checkOutTime;
	}
	public DateTime getCheckInTime() {
		return checkInTime;
	}
	public void setCheckInTime(DateTime checkInTime) {
		this.checkInTime = checkInTime;
	}
	public Integer getTotalTimeSpent() {
		return totalTimeSpent;
	}
	public void setTotalTimeSpent(Integer totalTimeSpent) {
		this.totalTimeSpent = totalTimeSpent;
	}
	
	

}
