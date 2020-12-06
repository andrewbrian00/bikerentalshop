package com.andrewbrianputosa.bikerentalshop.api;

import com.andrewbrianputosa.bikerentalshop.model.Bike;
import com.andrewbrianputosa.bikerentalshop.model.IValue;
import com.andrewbrianputosa.bikerentalshop.model.PageData;
import com.andrewbrianputosa.bikerentalshop.model.ResponseWrapper;
import com.andrewbrianputosa.bikerentalshop.model.Result;
import com.andrewbrianputosa.bikerentalshop.model.ServiceMessage;
import com.andrewbrianputosa.bikerentalshop.service.BikerentalshopService;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;

@RestController
@RequestMapping("/bikerental")
@SwaggerDefinition(info = @Info(description = "API service bike rental", title = "Bike Rental API", version = ""), produces = "application/json")
public class BikerentalshopApiImpl implements BikerentalshopApi {
	
	@Autowired
	private BikerentalshopService bikerentalshopService;
	
	@Override
	@PostMapping(value ="/create")
	@ApiOperation(value="Create new or Save Details of Bike", response = Object.class)
	public ResponseEntity<? extends IValue> createBike(@RequestBody Bike bike) {
		ResponseWrapper<Bike> response = new ResponseWrapper<>();
		Result<Bike> result = new Result<>();
		
		try {
			 result = bikerentalshopService.createBike(bike);
		} catch (Exception e) {
			List<ServiceMessage> errorMessages = response.getErrorMessage();
			errorMessages.add(new ServiceMessage(null, null, e.getMessage()));
			response.setErrorMessage(errorMessages);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		
		response.setEntity(result.getResultObject());
		response.setValidationMessage(result.getValidationMessage());
		response.setInfoMessage(result.getInfoMessage());

		return ResponseEntity.ok(response);
	}
	
	@Override
	@GetMapping(value ="/retrieveAllBikes")
	@ApiOperation(value="View List of bike", response = Object.class)
	public ResponseEntity<? extends IValue> retrieveAllBikes() {
		ResponseWrapper<PageData<Bike>> response = new ResponseWrapper<>();
		PageData<Bike> pageData = new PageData<>();
		
		try {
			List<Bike> resultList = bikerentalshopService.findAllBikes();
			pageData.setList(resultList);
		} catch (Exception e) {
			List<ServiceMessage> errorMessages = response.getErrorMessage();
			errorMessages.add(new ServiceMessage(null, null, e.getMessage()));
			response.setErrorMessage(errorMessages);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		response.setEntity(pageData);
		return ResponseEntity.ok(response);
	}

	@Override
	@PutMapping(value ="/updateBike")
	@ApiOperation(value="Update bike", response = Object.class)
	public ResponseEntity<? extends IValue> updateBike(@RequestBody Bike bike) {
		ResponseWrapper<Result<Object>> response = new ResponseWrapper<>();
		Result<Object> result = new Result<>();
		
		try {
			result = bikerentalshopService.updateBike(bike);
		} catch (Exception e) {
			List<ServiceMessage> errorMessages = response.getErrorMessage();
			errorMessages.add(new ServiceMessage(null, null, e.getMessage()));
			response.setErrorMessage(errorMessages);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		response.setEntity(result);
		return ResponseEntity.ok(response);
	}

	@Override
	@DeleteMapping(value ="/deleteBikeById/{id}")
	@ApiOperation(value="Delete Bike by ID", response = Object.class)
	public ResponseEntity<? extends IValue> deleteBikeById(@PathVariable String id) {
		ResponseWrapper<Result<Object>> response = new ResponseWrapper<>();
		Result<Object> result = new Result<>();
		try {
			result = bikerentalshopService.deleteById(id);
		} catch (Exception e) {
			List<ServiceMessage> errorMessages = response.getErrorMessage();
			errorMessages.add(new ServiceMessage(null, null, e.getMessage()));
			response.setErrorMessage(errorMessages);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		response.setEntity(result);
		return ResponseEntity.ok(response);
	}
	
	
	

}
