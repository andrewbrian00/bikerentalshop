package com.andrewbrianputosa.bikerentalshop.api;

import org.springframework.http.ResponseEntity;

import com.andrewbrianputosa.bikerentalshop.model.IValue;

public interface BikerentalshopApi {
	
	ResponseEntity<? extends IValue> findBike();

}
