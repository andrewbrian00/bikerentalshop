package com.andrewbrianputosa.bikerentalshop.service;

import java.util.List;

import com.andrewbrianputosa.bikerentalshop.model.Bike;
import com.andrewbrianputosa.bikerentalshop.model.Result;

public interface BikerentalshopService {

	Result<Bike> createBike(Bike bike);
	
	Bike findBikeById(String bike);
	
	List<Bike> findAllBikes();
	
	Result<Object> updateBike(Bike bike);
	
	Result<Object> deleteById(String documentId);

	
}
