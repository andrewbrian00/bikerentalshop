package com.andrewbrianputosa.bikerentalshop.model;


import java.util.ArrayList;
import java.util.List;

import com.andrewbrianputosa.bikerentalshop.util.ResultStatusEnum;

public class Result<T> {

  private T resultObject;
  private ResultStatusEnum status;
  private String statusMessage;

  private List<ServiceMessage> infoMessage = new ArrayList<ServiceMessage>();
  private List<ServiceMessage> errorMessage = new ArrayList<ServiceMessage>();
  private List<ServiceMessage> validationMessage = new ArrayList<ServiceMessage>();

  public Result() {
    super();
  }

  public Result(T resultObject, ResultStatusEnum status, String statusMessage) {
    super();
    this.resultObject = resultObject;
    this.status = status;
    this.statusMessage = statusMessage;
  }

  public T getResultObject() {
    return resultObject;
  }

  public void setResultObject(T resultObject) {
    this.resultObject = resultObject;
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
}
