package com.andrewbrianputosa.bikerentalshop.repository;

import static com.couchbase.client.java.query.Select.select;
import static com.couchbase.client.java.query.dsl.Expression.i;
import static com.couchbase.client.java.query.dsl.Expression.s;
import static com.couchbase.client.java.query.dsl.Expression.x;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.andrewbrianputosa.bikerentalshop.model.Bike;
import com.andrewbrianputosa.bikerentalshop.model.Result;
import com.andrewbrianputosa.bikerentalshop.util.CBQuery;
import com.andrewbrianputosa.bikerentalshop.util.CouchbaseConstant;
import com.andrewbrianputosa.bikerentalshop.util.CouchbaseJavaClass;
import com.andrewbrianputosa.bikerentalshop.util.CouchbaseQueryUtil;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.query.Delete;
import com.couchbase.client.java.query.Statement;

@Repository
public class BikerentalshopRepositoryImpl implements BikerentalshopRepository{

	private static final Logger LOGGER = LoggerFactory.getLogger(BikerentalshopRepositoryImpl.class);
	private final Bucket bucket;
	private final String bucketName;
	
	@Autowired
	public BikerentalshopRepositoryImpl(Bucket bucket) {
		this.bucket = bucket;
		this.bucketName = bucket.name();
	}
	
	@Override
	public List<Bike> findAllBikes() {
		List<Bike> resultList = new ArrayList<>();
		
		try {
			CBQuery cbQuery = new CBQuery(bucket);
			Statement query = select(CouchbaseQueryUtil.selectAll(bucketName, Bike.class))
					.from(i(bucketName))
					.where(x(CouchbaseConstant.JAVA_CLASS.value())
							.eq(s(CouchbaseJavaClass.BIKE.getJavaClass())));
			LOGGER.info("BIKE findAllBike: {query} " ,query);
			resultList = cbQuery.extractResult(query, Bike.class, true);
			
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw e;
		}
		
		return resultList;
	}

	@Override
	public void save(Bike bike) {
		try {
			CBQuery cbQuery = new CBQuery(bucket);
			cbQuery.upsert(bike);
			LOGGER.info("Bike Details saved.");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw e;
		}
	}

	@Override
	public Bike findBikeById(String id) {
		CBQuery cbQuery = new CBQuery(bucket);
		try {
			Statement query = select(CouchbaseQueryUtil.selectAll(bucketName, Bike.class))
					.from(i(bucketName))
							.where(x(CouchbaseConstant.BIKE_ID.value()).eq(s(id))
									.and(x(CouchbaseConstant.JAVA_CLASS.value()))
									.eq(s(CouchbaseJavaClass.BIKE.getJavaClass())));

			return cbQuery.extractResult(query, Bike.class, true).stream().findFirst().orElse(null);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw e;
		}
	}

	@Override
	public void updateBike(Bike bike) {
		try {
			CBQuery cbQuery = new CBQuery(bucket);
			cbQuery.upsert(bike);
			LOGGER.info("Bike Details successfully updated.");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw e;
		}
	}

	@Override
	public void deleteById(String documentId) {
		try {
			CBQuery cbQuery = new CBQuery(bucket);
			Statement query = Delete.deleteFrom(bucketName).useKeys(s(documentId));
			cbQuery.delete(query);
			LOGGER.info("Document has been deleted: {documentId}" , documentId);
		} catch (Exception e) { 
			LOGGER.error(e.getMessage(), e);
			throw e;
		}
	}
	
	
}
