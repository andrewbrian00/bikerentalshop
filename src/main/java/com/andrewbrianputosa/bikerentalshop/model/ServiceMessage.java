package com.andrewbrianputosa.bikerentalshop.model;

public class ServiceMessage {
  private String code;
  private String propertyName;
  private String shortMessage;

  public ServiceMessage() {
    super();
  }

  public ServiceMessage(String code, String propertyName, String shortMessage) {
    super();
    this.code = code;
    this.propertyName = propertyName;
    this.shortMessage = shortMessage;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getPropertyName() {
    return propertyName;
  }

  public void setPropertyName(String propertyName) {
    this.propertyName = propertyName;
  }

  public String getShortMessage() {
    return shortMessage;
  }

  public void setShortMessage(String shortMessage) {
    this.shortMessage = shortMessage;
  }

}
