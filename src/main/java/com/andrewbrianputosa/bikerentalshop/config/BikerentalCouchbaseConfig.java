package com.andrewbrianputosa.bikerentalshop.config;

import java.util.List;

import javax.annotation.PostConstruct;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.env.CouchbaseEnvironment;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;

@Configuration
public class BikerentalCouchbaseConfig {
	
	@Value("${couchbase.host}")
	private String host;
	
	@Value("${couchbase.bucket.user}")
	private String bucketName;
	
	@Value("${couchbase.bucket.password}")
	private String bucketPassword;
	
	private List<String> nodes;
	
	@PostConstruct
	public void init() {
		this.nodes = Arrays.asList(host);
	}

	public @Bean Cluster couchbaseCluster() {
		CouchbaseEnvironment env = DefaultCouchbaseEnvironment.builder().connectTimeout(60000)
				.socketConnectTimeout(60000).build();
		CouchbaseCluster cluster = CouchbaseCluster.create(env, nodes);
		cluster.authenticate(bucketName, bucketPassword);
		return cluster;
	}

	public @Bean Bucket bucket() {
		return couchbaseCluster().openBucket(bucketName);
	}
}
