package com.andrewbrianputosa.bikerentalshop.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.andrewbrianputosa.bikerentalshop.model.Bike;
import com.andrewbrianputosa.bikerentalshop.model.Result;
import com.andrewbrianputosa.bikerentalshop.repository.BikerentalshopRepository;
import com.andrewbrianputosa.bikerentalshop.util.ResultStatusEnum;
import com.andrewbrianputosa.bikerentalshop.model.ServiceMessage;

@Service
public class BikerentalshopServiceImpl implements BikerentalshopService {

	@Autowired
	private BikerentalshopRepository bikerentalshopRepository;
	
	
	@Override
	public Result<Bike> createBike(Bike bike) {
		Result<Bike> result = new Result<>();
		try {
			bikerentalshopRepository.save(bike);
		} catch (Exception e) {
			result = new Result<>(bike, ResultStatusEnum.ERROR,
					"Unable to save document.");
			result.setInfoMessage(Arrays.asList(new ServiceMessage(null, null, "FAILURE")));
		}
		return  result;
	}
	
	@Override
	public List<Bike> findAllBikes() {
		return bikerentalshopRepository.findAllBikes();
	}

	@Override
	public Bike findBikeById(String bike) {
		return bikerentalshopRepository.findBikeById(bike);
	}

	@Override
	public Result<Object> updateBike(Bike bike) {
		Result<Object> result = new Result<>();
		try {
			bikerentalshopRepository.updateBike(bike);
			result = new Result<>(bike, ResultStatusEnum.SUCCESS, "Successfully Update Document");
		} catch (Exception e) {
			result = new Result<>(bike, ResultStatusEnum.ERROR,
					"Unable to Update Document");
			result.setInfoMessage(Arrays.asList(new ServiceMessage(null, null, "FAILURE")));
		}
		
		return  result;
	}

	@Override
	public Result<Object> deleteById(String documentId) {
		Result<Object> result = new Result<>();
		
		try {
			bikerentalshopRepository.deleteById(documentId);
			result = new Result<>(documentId, ResultStatusEnum.SUCCESS, "Delete Documents with id: " + documentId);
		} catch (Exception e) {
			result = new Result<>(documentId, ResultStatusEnum.ERROR,
					"Unable to delete document with Id of: " + documentId);
			result.setInfoMessage(Arrays.asList(new ServiceMessage(null, null, "FAILURE")));
		}
		return result;
	}
	
	

}
