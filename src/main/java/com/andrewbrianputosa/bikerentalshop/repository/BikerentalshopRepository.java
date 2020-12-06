package com.andrewbrianputosa.bikerentalshop.repository;

import java.util.List;

import com.andrewbrianputosa.bikerentalshop.model.Bike;
import com.andrewbrianputosa.bikerentalshop.model.Result;

public interface BikerentalshopRepository {
	
	void save(Bike bike);
	
	Bike findBikeById(String id);
	
	List<Bike> findAllBikes();
	
	void updateBike(Bike bike);
	
	void deleteById(String documentId);

}
