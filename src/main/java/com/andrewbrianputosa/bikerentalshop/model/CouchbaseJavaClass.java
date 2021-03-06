package com.andrewbrianputosa.bikerentalshop.model;


public enum CouchbaseJavaClass {

   ROLE("Role"),
   BIKE("Bike");

  private String javaClass;

  private CouchbaseJavaClass(String javaClass) {
    this.javaClass = javaClass;
  }

  public String getJavaClass() {
    return javaClass;
  }

}
