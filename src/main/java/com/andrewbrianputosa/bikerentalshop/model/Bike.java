package com.andrewbrianputosa.bikerentalshop.model;

import org.joda.time.DateTime;

import com.couchbase.client.deps.com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties
public class Bike {
	
	private String id;
	private String customerName;
	private DateTime checkOutTime;
	private DateTime checkInTime;
	private Integer totalTimeSpent;
	private String contactNo;
	private String javaClass;
	
	
	
	public Bike() {
		
	}

	public Bike(String id, String customerName, DateTime checkOutTime, DateTime checkInTime, Integer totalTimeSpent,
			String contactNo, String javaClass) {
		super();
		this.id = id;
		this.customerName = customerName;
		this.checkOutTime = checkOutTime;
		this.checkInTime = checkInTime;
		this.totalTimeSpent = totalTimeSpent;
		this.contactNo = contactNo;
		this.javaClass = javaClass;
	}

	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
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
	
	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public void setJavaClass(String javaClass) {
		this.javaClass = javaClass;
	}
	
	public String getJavaClass() {
		return javaClass;
	}


	@Override
	public String toString() {
		return "Bike [id=" + id + ", customerName=" + customerName + ", checkOutTime=" + checkOutTime + ", checkInTime="
				+ checkInTime + ", totalTimeSpent=" + totalTimeSpent + ", javaClass=" + javaClass + "]";
	}
	
	

}
