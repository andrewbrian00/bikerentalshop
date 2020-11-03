package com.andrewbrianputosa.bikerentalshop.model;

import java.util.List;

public class PageData<T> {

	 private List<T> list;

	  private Integer total;

	  private Integer filtered;

	  public List<T> getList() {
	    return list;
	  }

	  public void setList(List<T> list) {
	    this.list = list;
	  }

	  public Integer getTotal() {
	    return total;
	  }

	  public void setTotal(Integer total) {
	    this.total = total;
	  }

	  public Integer getFiltered() {
	    return filtered;
	  }

	  public void setFiltered(Integer filtered) {
	    this.filtered = filtered;
	  }
}
