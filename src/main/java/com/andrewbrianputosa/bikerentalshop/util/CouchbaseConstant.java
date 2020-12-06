package com.andrewbrianputosa.bikerentalshop.util;

public enum CouchbaseConstant {

	  ALL("*"),
	  META_ID("meta().id"),
	  JAVA_CLASS("javaClass"),
	  BIKE_ID("id");
	  
	  private String constant;

	  private CouchbaseConstant(String couchbaseConstant) {
	    this.constant = couchbaseConstant;
	  }

	  public String value() {
	    return constant;
	  }

	  public String from(String from) {
	    return String.format("%s.%s", from, constant);
	  }

	  public String leftLike() {
	    return String.format("%%%s", constant);
	  }

	  public String rightLike() {
	    return String.format("%s%%", constant);
	  }

	  public String like() {
	    return String.format("%%%s%%", constant);
	  }

}
