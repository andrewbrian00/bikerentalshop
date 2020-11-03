package com.andrewbrianputosa.bikerentalshop.model;

import java.util.*;

import com.andrewbrianputosa.bikerentalshop.model.IValue;
import com.andrewbrianputosa.bikerentalshop.util.ResultStatusEnum;


public class ResponseWrapper<T> implements IValue {
	  private T entity;

	  private List<ServiceMessage> infoMessage = new ArrayList<ServiceMessage>();
	  private List<ServiceMessage> errorMessage = new ArrayList<ServiceMessage>();
	  private List<ServiceMessage> validationMessage = new ArrayList<ServiceMessage>();
	  private ResultStatusEnum status;
	  private String statusMessage;

	  public ResponseWrapper(T entity) {
	    this.entity = entity;
	  }

	  public ResponseWrapper(Result<T> result) {
		  this.setEntity(result.getResultObject());
		  this.setErrorMessage(result.getErrorMessage());
		  this.setInfoMessage(result.getInfoMessage());
		  this.setValidationMessage(result.getValidationMessage());
		  this.setStatus(result.getStatus());
		  this.setStatusMessage(result.getStatusMessage());
	  }

	  public ResponseWrapper() {

	  }

	  public T getEntity() {
	    return entity;
	  }

	  public void setEntity(T entity) {
	    this.entity = entity;
	  }

	  public List<ServiceMessage> getInfoMessage() {
	    return infoMessage;
	  }

	  public void setInfoMessage(List<ServiceMessage> infoMessage) {
	    this.infoMessage = infoMessage;
	  }

	  public List<ServiceMessage> getErrorMessage() {
	    return errorMessage;
	  }

	  public void setErrorMessage(List<ServiceMessage> errorMessage) {
	    this.errorMessage = errorMessage;
	  }

	  public List<ServiceMessage> getValidationMessage() {
	    return validationMessage;
	  }

	  public void setValidationMessage(List<ServiceMessage> validationMessage) {
	    this.validationMessage = validationMessage;
	  }

	  public ResultStatusEnum getStatus() {
		return status;
	  }

	  public void setStatus(ResultStatusEnum status) {
		this.status = status;
	  }

	  public String getStatusMessage() {
		return statusMessage;
	  }

	  public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	  }
	}