package com.andrewbrianputosa.bikerentalshop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.andrewbrianputosa.bikerentalshop.model.Bike;
import com.andrewbrianputosa.bikerentalshop.repository.BikerentalshopRepository;

@Service
public class BikerentalshopServiceImpl implements BikerentalshopService {

	@Autowired
	private BikerentalshopRepository bikerentalshopRepository;
	
	@Override
	public List<Bike> findBike() {
		// TODO Auto-generated method stub
		return bikerentalshopRepository.findBike();
	}

}
