package com.andrewbrianputosa.bikerentalshop.model;

public enum CouchbaseConstant {

  ALL("*"),
  META_ID("meta().id"),
  JAVA_CLASS("javaClass"),
  
  LAST_NAME("lastName"),
  FIRST_NAME("firstName"),
  MIDDLE_NAME("middleName"),
  EMAIL_ADDRESS("emailAddress"),
  SUFFIX("suffix"),
  BIRTH_DATE("birthDate"),
  GENDER("gender"),
  CIVIL_STATUS("civilStatus"),
  BIRTHPLACE("birthPlace"),
  NATIONALITY("nationality"),
  CONTACT_NO("contactNo"),
  
  DATE_REGISTERED("dateRegistered"),
  
  ITEM_CODE("itemCode"),
  
  QUANTITY("quantity"),
  DOCUMENT_TYPE("documentType"),
  COUNT("count"),
  ITEMS("items"),
  
  
  // USER
  USER_ID("id"),
  USER_NAME("name"),
  USER_USERNAME("username"),
  USER_USERPASS("userpass"),
  USER_EMPLOYEE_ID("employeeId"),
  USER_BRANCH_ASSIGNMENT("branchAssignments"),
  USER_FULL_NAME("fullName"),
	
  //ORDER
  ORDER_QTY("orderQty"),
  ORDER_DATE("orderDate"),
  
  //PAYMENT TYPE REF
  PAYMENT_TYPE_DESC("paymentTypeDesc");
	
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
