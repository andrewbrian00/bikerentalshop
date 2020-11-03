package com.andrewbrianputosa.bikerentalshop.repository;

import java.util.List;

import com.andrewbrianputosa.bikerentalshop.model.Bike;

public interface BikerentalshopRepository {
	
	List<Bike> findBike();

}
