package com.andrewbrianputosa.bikerentalshop.api;

import com.andrewbrianputosa.bikerentalshop.model.Bike;
import com.andrewbrianputosa.bikerentalshop.model.IValue;
import com.andrewbrianputosa.bikerentalshop.model.PageData;
import com.andrewbrianputosa.bikerentalshop.model.ResponseWrapper;
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
	@GetMapping(value ="/findBike")
	@ApiOperation(value="View List of bike", response = Object.class)
	public ResponseEntity<? extends IValue> findBike() {
		ResponseWrapper<PageData<Bike>> response = new ResponseWrapper<>();
		PageData<Bike> pageData = new PageData<>();
		
		try {
			List<Bike> resultList = bikerentalshopService.findBike();
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

}
