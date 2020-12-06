package com.andrewbrianputosa.bikerentalshop.api;

import org.springframework.http.ResponseEntity;
import com.andrewbrianputosa.bikerentalshop.model.Bike;
import com.andrewbrianputosa.bikerentalshop.model.IValue;

public interface BikerentalshopApi {
	
	ResponseEntity<? extends IValue> createBike(Bike bike);
	
	ResponseEntity<? extends IValue> retrieveAllBikes();
	
	ResponseEntity<? extends IValue> updateBike(Bike bike);
	
	ResponseEntity<? extends IValue> deleteBikeById(String documentId);

}
