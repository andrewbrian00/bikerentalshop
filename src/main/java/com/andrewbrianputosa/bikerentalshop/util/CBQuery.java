package com.andrewbrianputosa.bikerentalshop.util;

import java.io.IOException;
import java.util.*;
import org.apache.commons.beanutils.PropertyUtils;
import org.joda.time.DateTime;
import org.slf4j.*;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;

/**
 * The Class CBQuery.
 */
public class CBQuery {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(CBQuery.class);

	/** The Constant ID. */
	private static final String ID = "id";

	/** The bucket. */
	private final Bucket bucket;

	private final int MAX_TRIES = 3;

	private final int MAX_DELAY = 5000;

	/**
	 * Instantiates a new CB query.
	 */
	public CBQuery() {
		this.bucket = null;
	}

	/**
	 * Instantiates a new CB query.
	 *
	 * @param bucket {@link Bucket}
	 */
	public CBQuery(Bucket bucket) {
		this.bucket = bucket;
	}

	// With Bucket from Constructor

	/**
	 * Gets the document from bucket using docId.
	 *
	 * @param        <T> the generic type
	 * @param docId  the doc id
	 * @param target the target
	 * @return the t
	 * @throws InterruptedException
	 */
	public <T> T get(String docId, Class<T> target) {
		int tries = 0;
		while (true) {
			tries++;
			try {
				JsonDocument doc = bucket.get(docId);
				return target.cast(doc.content());
			} catch (Exception e) {
				LOGGER.info("Retrying...... : Attempt - " + tries);
				sleep();
				if (tries >= MAX_TRIES) {
					LOGGER.warn("Get failed | {}", e.getMessage());
					LOGGER.warn("Error retrieving {} from bucket {}", docId, bucket.name());
					return null;
				}
			}

		}
	}

	/**
	 * Delete a document using a query.
	 *
	 * @param query the query
	 * @return true, if successful
	 * @throws InterruptedException
	 */
	public boolean delete(Statement query) {
		int tries = 0;
		if (bucket == null || query == null) {
			LOGGER.info("bucket/object is null");
			return false;
		}
		while (true) {
			tries++;
			try {
				bucket.query(query);
				CouchbaseQueryUtil.logQuery(query);
				return true;
			} catch (Exception e) {
				LOGGER.info("Retrying...... : Attempt - " + tries);
				sleep();
				if (tries >= MAX_TRIES) {
					LOGGER.warn("Delete failed | {}", e.getMessage());
					return false;
				}

			}

		}
	}

	public boolean delete(String query) {
		int tries = 0;
		if (bucket == null || query == null) {
			LOGGER.info("bucket/object is null");
			return false;
		}
		while (true) {
			tries++;
			try {
				bucket.query(N1qlQuery.simple(query));
				CouchbaseQueryUtil.logQuery(query);
				return true;
			} catch (Exception e) {
				LOGGER.info("Retrying...... : Attempt - " + tries);
				sleep();
				if (tries >= MAX_TRIES) {
					LOGGER.warn("Delete failed | {}", e.getMessage());
					return false;
				}

			}

		}
	}

	/**
	 * Remove a document in the bucket.
	 *
	 * @param obj Object to be removed
	 * @return true, if successful
	 */
	public boolean remove(Object obj) {
		int tries = 0;
		if (bucket == null || obj == null) {
			LOGGER.info("bucket/object is null");
			return false;
		}
		while (true) {
			tries++;
			try {
				CouchbaseQueryUtil.setTypeForObject(obj);
				final String DOCUMENT_ID = String.valueOf(PropertyUtils.getProperty(obj, ID));
				bucket.remove(convertEntityToDocument(DOCUMENT_ID, obj));
				CouchbaseQueryUtil.success(DOCUMENT_ID);
				return true;
			} catch (Exception e) {
				LOGGER.info("Retrying...... : Attempt - " + tries);
				sleep();
				if (tries >= MAX_TRIES) {
					LOGGER.warn("Remove failed | {}", e.getMessage());
					return false;
				}
			}
		}
	}

	/**
	 * Insert document to the bucket.
	 *
	 * @param obj Object to be inserted to the bucket
	 * @return true, if successful
	 */
	public boolean insert(Object obj) {
		int tries = 0;
		if (bucket == null || obj == null) {
			LOGGER.info("bucket/object is null");
			return false;
		}
		while (true) {
			tries++;
			try {
				CouchbaseQueryUtil.setTypeForObject(obj);
				final String DOCUMENT_ID = String.valueOf(PropertyUtils.getProperty(obj, ID));
				bucket.insert(convertEntityToDocument(DOCUMENT_ID, obj));
				CouchbaseQueryUtil.success(DOCUMENT_ID);
				return true;
			} catch (Exception e) {
				LOGGER.info("Retrying...... : Attempt - " + tries);
				sleep();
				if (tries >= MAX_TRIES) {
					LOGGER.warn("Insert failed | {}", e.getMessage());
					return false;
				}

			}
		}
	}

	/**
	 * Update document in the bucket given the Document ID. Else, if the document
	 * does not exist it will be inserted instead.
	 *
	 * @param obj Object to be inserted to the bucket
	 * @return true, if successful
	 */
	public boolean upsert(Object obj) {
		int tries = 0;
		if (bucket == null || obj == null) {
			LOGGER.info("bucket/object is null");
			return false;
		}
		while (true) {
			tries++;
			try {
				CouchbaseQueryUtil.setTypeForObject(obj);
				final String DOCUMENT_ID = String.valueOf(PropertyUtils.getProperty(obj, ID));
				bucket.upsert(convertEntityToDocument(DOCUMENT_ID, obj));
				CouchbaseQueryUtil.success(DOCUMENT_ID);
				return true;
			} catch (Exception e) {
				LOGGER.info("Retrying...... : Attempt - " + tries);
				LOGGER.warn("Upsert failed | {}", e.getMessage());
				sleep();
				if (tries >= MAX_TRIES) {
					LOGGER.warn("Upsert failed | {}", e.getMessage());
					return false;
				}
			}
		}
	}

	/**
	 * Replace a document in the bucket given the Document ID. It will throw an
	 * error if the document does not exist. Use {@link #upsert} instead to prevent
	 * errors on updating a non-existing document.
	 *
	 * @param obj Object to be inserted to the bucket
	 * @return true, if successful
	 */
	public boolean replace(Object obj) {
		int tries = 0;
		if (bucket == null || obj == null) {
			LOGGER.info("bucket/object is null");
			return false;
		}
		while (true) {
			tries++;
			try {
				CouchbaseQueryUtil.setTypeForObject(obj);
				final String DOCUMENT_ID = String.valueOf(PropertyUtils.getProperty(obj, ID));
				bucket.replace(convertEntityToDocument(DOCUMENT_ID, obj));
				CouchbaseQueryUtil.success(DOCUMENT_ID);
				return true;
			} catch (Exception e) {
				LOGGER.info("Retrying...... : Attempt - " + tries);
				sleep();
				if (tries >= MAX_TRIES) {
					LOGGER.warn("Replace failed | {}", e.getMessage());
					return false;
				}
			}

		}
	}

	/**
	 * Extracts the result using the query provided and returns a List of the given
	 * type.
	 *
	 * @param       <T> Generic Type
	 * @param query Query to be executed on the Bucket
	 * @param type  Type of Object that the method will return
	 * @return List of data with the given type
	 */
	public <T> List<T> extractResult(Statement query, Class<T> type) {
		int tries = 0;
		if (bucket == null || query == null) {
			LOGGER.info("bucket/query is null");
			return null;
		}
		while (true) {
			tries++;
			N1qlQueryResult result = bucket.query(N1qlQuery.simple(query));
			CouchbaseQueryUtil.logQuery(query);
			if (result.finalSuccess() && result.parseSuccess()) {
				ObjectMapper mapper = new ObjectMapper();
				List<T> content = new ArrayList<>();
				for (N1qlQueryRow row : result) {
					try {
						content.add(mapper.readValue(row.value().toString(), type));
					} catch (IOException e) {
						LOGGER.error(e.getMessage(), e);
						e.printStackTrace();
					}
				}
				return content;
			} else {
				LOGGER.info("Retrying...... : Attempt - " + tries);
				sleep();
				if (tries >= MAX_TRIES) {
					LOGGER.warn("Extract result failed | {}", result.errors());
					return null;
				}
			}
		}
//		if (bucket == null || query == null) {
//			LOGGER.info("bucket/query is null");
//			return null;
//		}
//		N1qlQueryResult result = bucket.query(N1qlQuery.simple(query));
//		CouchbaseQueryUtil.logQuery(query);
//		if (!result.finalSuccess()) {
//			LOGGER.warn("Query returned with errors: {}", result.errors());
//		}
//		ObjectMapper mapper = new ObjectMapper();
//		List<T> content = new ArrayList<>();
//		for (N1qlQueryRow row : result) {
//			try {
//				content.add(mapper.readValue(row.value().toString(), type));
//			} catch (IOException e) {
//				LOGGER.error(e.getMessage(), e);
//				e.printStackTrace();
//			}
//		}
//		return content;
	}

	public <T> List<T> extractResult(String query, Class<T> type) {
		int tries = 0;
		if (bucket == null || query == null) {
			LOGGER.info("bucket/query is null");
			return null;
		}
		while (true) {
			tries++;
			N1qlQueryResult result = bucket.query(N1qlQuery.simple(query));
			CouchbaseQueryUtil.logQuery(query);
			if (result.finalSuccess() && result.parseSuccess()) {
				ObjectMapper mapper = new ObjectMapper();
				List<T> content = new ArrayList<>();
				for (N1qlQueryRow row : result) {
					try {
						content.add(mapper.readValue(row.value().toString(), type));
					} catch (IOException e) {
						LOGGER.error(e.getMessage(), e);
						e.printStackTrace();
					}
				}
				return content;
			} else {
				LOGGER.info("Retrying...... : Attempt - " + tries);
				sleep();
				if (tries >= MAX_TRIES) {
					LOGGER.warn("Extract result failed | {}", result.errors());
					return null;
				}
			}
		}
//		if (bucket == null || query == null) {
//			LOGGER.info("bucket/query is null");
//			return null;
//		}
//		N1qlQueryResult result = bucket.query(N1qlQuery.simple(query));
//		CouchbaseQueryUtil.logQuery(query);
//		if (!result.finalSuccess()) {
//			LOGGER.warn("Query returned with errors: {}", result.errors());
//		}
//		ObjectMapper mapper = new ObjectMapper();
//		List<T> content = new ArrayList<>();
//		for (N1qlQueryRow row : result) {
//			try {
//				content.add(mapper.readValue(row.value().toString(), type));
//			} catch (IOException e) {
//				LOGGER.error(e.getMessage(), e);
//				e.printStackTrace();
//			}
//		}
//		return content;
	}

	/**
	 * Extracts the result using the query provided and returns a List of the given
	 * type. Prints the query if user sets logQuery boolean to true
	 *
	 * @param          <T> Generic Type
	 * @param query    Query to be executed on the Bucket
	 * @param type     Type of Object that the method will return
	 * @param logQuery Logs the query given if the value is true
	 * @return List of data with the given type
	 */
	public <T> List<T> extractResult(Statement query, Class<T> type, boolean logQuery) {
		int tries = 0;
		if (logQuery)
			CouchbaseQueryUtil.logQuery(query);
		while (true) {
			tries++;
			N1qlQueryResult result = bucket.query(N1qlQuery.simple(query));
			CouchbaseQueryUtil.logQuery(query);
			if (result.finalSuccess() && result.parseSuccess()) {
				ObjectMapper mapper = new ObjectMapper();
				List<T> content = new ArrayList<>();
				for (N1qlQueryRow row : result) {
					try {
						content.add(mapper.readValue(row.value().toString(), type));
					} catch (IOException e) {
						LOGGER.error(e.getMessage(), e);
						e.printStackTrace();
					}
				}
				return content;
			} else {
				LOGGER.info("Retrying...... : Attempt - " + tries);
				sleep();
				if (tries >= MAX_TRIES) {
					LOGGER.warn("Extract result failed | {}", result.errors());
					return null;
				}
			}
		}
//		if (logQuery)
//			CouchbaseQueryUtil.logQuery(query);
//		N1qlQueryResult result = bucket.query(N1qlQuery.simple(query));
//		if (!result.finalSuccess()) {
//			LOGGER.warn("Query returned with errors: {}", result.errors());
//		}
//		ObjectMapper mapper = new ObjectMapper();
//		List<T> content = new ArrayList<>();
//		for (N1qlQueryRow row : result) {
//			try {
//				content.add(mapper.readValue(row.value().toString(), type));
//			} catch (IOException e) {
//				LOGGER.error(e.getMessage(), e);
//				e.printStackTrace();
//			}
//		}
//		return content;
	}

	public <T> List<T> extractResult(String query, Class<T> type, boolean logQuery) {
		int tries = 0;
		if (logQuery)
			CouchbaseQueryUtil.logQuery(query);
		while (true) {
			tries++;
			N1qlQueryResult result = bucket.query(N1qlQuery.simple(query));
			CouchbaseQueryUtil.logQuery(query);
			if (result.finalSuccess() && result.parseSuccess()) {
				ObjectMapper mapper = new ObjectMapper();
				List<T> content = new ArrayList<>();
				for (N1qlQueryRow row : result) {
					try {
						content.add(mapper.readValue(row.value().toString(), type));
					} catch (IOException e) {
						LOGGER.error(e.getMessage(), e);
						e.printStackTrace();
					}
				}
				return content;
			} else {
				LOGGER.info("Retrying...... : Attempt - " + tries);
				sleep();
				if (tries >= MAX_TRIES) {
					LOGGER.warn("Extract result failed | {}", result.errors());
					return null;
				}
			}
		}
//		if (logQuery)
//			CouchbaseQueryUtil.logQuery(query);
//		N1qlQueryResult result = bucket.query(N1qlQuery.simple(query));
//		if (!result.finalSuccess()) {
//			LOGGER.warn("Query returned with errors: {}", result.errors());
//		}
//		ObjectMapper mapper = new ObjectMapper();
//		List<T> content = new ArrayList<>();
//		for (N1qlQueryRow row : result) {
//			try {
//				content.add(mapper.readValue(row.value().toString(), type));
//			} catch (IOException e) {
//				LOGGER.error(e.getMessage(), e);
//				e.printStackTrace();
//			}
//		}
//		return content;
	}
	
	public <T> List<T> extractPreparedResult(String query, Class<T> type, boolean logQuery) {
		int tries = 0;
		if (logQuery)
			CouchbaseQueryUtil.logQuery(query);
		while (true) {
			tries++;
			N1qlParams params = N1qlParams.build().adhoc(false);
			N1qlQueryResult result = bucket.query(N1qlQuery.simple(query, params));
			CouchbaseQueryUtil.logQuery(query);
			if (result.finalSuccess() && result.parseSuccess()) {
				ObjectMapper mapper = new ObjectMapper();
				List<T> content = new ArrayList<>();
				for (N1qlQueryRow row : result) {
					try {
						content.add(mapper.readValue(row.value().toString(), type));
					} catch (IOException e) {
						LOGGER.error(e.getMessage(), e);
						e.printStackTrace();
					}
				}
				return content;
			} else {
				LOGGER.info("Retrying...... : Attempt - " + tries);
				sleep();
				if (tries >= MAX_TRIES) {
					LOGGER.warn("Extract result failed | {}", result.errors());
					return null;
				}
			}
		}
//		if (logQuery)
//			CouchbaseQueryUtil.logQuery(query);
//		N1qlQueryResult result = bucket.query(N1qlQuery.simple(query));
//		if (!result.finalSuccess()) {
//			LOGGER.warn("Query returned with errors: {}", result.errors());
//		}
//		ObjectMapper mapper = new ObjectMapper();
//		List<T> content = new ArrayList<>();
//		for (N1qlQueryRow row : result) {
//			try {
//				content.add(mapper.readValue(row.value().toString(), type));
//			} catch (IOException e) {
//				LOGGER.error(e.getMessage(), e);
//				e.printStackTrace();
//			}
//		}
//		return content;
	}

	// ---------------- Without Bucket ---------------------

	/**
	 * Gets the document from bucket using docId.
	 *
	 * @param        <T> the generic type
	 * @param bucket the bucket
	 * @param docId  the doc id
	 * @param target the target
	 * @return the t
	 */
	public <T> T get(Bucket bucket, String docId, Class<T> target) {
		int tries = 0;
		if (bucket == null || docId == null || target == null) {
			LOGGER.info("bucket/docId/target is null");
			return null;
		}
		while (true) {
			tries++;
			try {
				JsonDocument doc = bucket.get(docId);
				return target.cast(doc.content());
			} catch (Exception e) {
				LOGGER.info("Retrying...... : Attempt - " + tries);
				sleep();
				if (tries >= MAX_TRIES) {
					LOGGER.warn("Error retrieving {} from bucket {}", docId, bucket.name());
					return null;
				}
			}
		}
//		if (bucket == null || docId == null || target == null) {
//			LOGGER.info("bucket/docId/target is null");
//			return null;
//		}
//		try {
//			JsonDocument doc = bucket.get(docId);
//			return target.cast(doc.content());
//		} catch (Exception e) {
//			LOGGER.warn("Error retrieving {} from bucket {}", docId, bucket.name());
//			e.printStackTrace();
//			return null;
//		}
	}

	/**
	 * Delete a document using a query.
	 *
	 * @param bucket {@link Bucket}
	 * @param query  the query
	 * @return true, if successful
	 */
	public boolean delete(Bucket bucket, Statement query) {
		int tries = 0;
		if (bucket == null || query == null) {
			LOGGER.info("bucket/object is null");
			return false;
		}
		while (true) {
			tries++;
			try {
				bucket.query(query);
				CouchbaseQueryUtil.logQuery(query);
				return true;
			} catch (Exception e) {
				LOGGER.info("Retrying...... : Attempt - " + tries);
				sleep();
				if (tries >= MAX_TRIES) {
					LOGGER.warn("Delete failed | {}", e.getMessage());
					return false;
				}
			}
		}
//		if (bucket == null || query == null) {
//			LOGGER.info("bucket/object is null");
//			return false;
//		}
//		try {
//			bucket.query(query);
//			CouchbaseQueryUtil.logQuery(query);
//		} catch (Exception e) {
//			LOGGER.warn("Delete failed | {}", e.getMessage());
//			return false;
//		}
//		return true;
	}

	/**
	 * Insert document to the bucket.
	 *
	 * @param bucket {@link Bucket}
	 * @param obj    Object to be inserted to the bucket
	 * @return true, if successful
	 */
	public boolean insert(Bucket bucket, Object obj) {
		int tries = 0;
		if (bucket == null || obj == null)
			return false;
		while (true) {
			tries++;
			try {
				CouchbaseQueryUtil.setTypeForObject(obj);
				final String DOCUMENT_ID = String.valueOf(PropertyUtils.getProperty(obj, ID));
				bucket.insert(convertEntityToDocument(DOCUMENT_ID, obj));
				CouchbaseQueryUtil.success(DOCUMENT_ID);
				return true;
			} catch (Exception e) {
				LOGGER.info("Retrying...... : Attempt - " + tries);
				sleep();
				if (tries >= MAX_TRIES) {
					LOGGER.warn("Insert failed | {}", e.getMessage());
					return false;
				}
			}
		}
//		if (bucket == null || obj == null)
//			return false;
//		try {
//			CouchbaseQueryUtil.setTypeForObject(obj);
//			final String DOCUMENT_ID = String.valueOf(PropertyUtils.getProperty(obj, ID));
//			bucket.insert(convertEntityToDocument(DOCUMENT_ID, obj));
//			CouchbaseQueryUtil.success(DOCUMENT_ID);
//		} catch (Exception e) {
//			LOGGER.warn("Insert failed | {}", e.getMessage());
//			return false;
//		}
//		return true;
	}

	/**
	 * Update document in the bucket given the Document ID. Else, if the document
	 * does not exist it will be inserted instead.
	 *
	 * @param bucket {@link Bucket}
	 * @param obj    Object to be inserted to the bucket
	 * @return true, if successful
	 */
	public boolean upsert(Bucket bucket, Object obj) {
		int tries = 0;
		if (bucket == null || obj == null)
			return false;
		while (true) {
			tries++;
			try {
				CouchbaseQueryUtil.setTypeForObject(obj);
				final String DOCUMENT_ID = String.valueOf(PropertyUtils.getProperty(obj, ID));
				bucket.upsert(convertEntityToDocument(DOCUMENT_ID, obj));
				CouchbaseQueryUtil.success(DOCUMENT_ID);
				return true;
			} catch (Exception e) {
				LOGGER.info("Retrying...... : Attempt - " + tries);
				sleep();
				if (tries >= MAX_TRIES) {
					LOGGER.warn("Upsert failed | {}", e.getMessage());
					return false;
				}
			}
		}
//		if (bucket == null || obj == null)
//			return false;
//		try {
//			CouchbaseQueryUtil.setTypeForObject(obj);
//			final String DOCUMENT_ID = String.valueOf(PropertyUtils.getProperty(obj, ID));
//			bucket.upsert(convertEntityToDocument(DOCUMENT_ID, obj));
//			CouchbaseQueryUtil.success(DOCUMENT_ID);
//		} catch (Exception e) {
//			LOGGER.warn("Upsert failed | {}", e.getMessage());
//			return false;
//		}
//		return true;
	}

	/**
	 * Replace a document in the bucket given the Document ID. It will throw an
	 * error if the document does not exist. Use {@link #upsert} instead to prevent
	 * errors on updating a non-existing document.
	 *
	 * @param bucket {@link Bucket}
	 * @param obj    Object to be inserted to the bucket
	 * @return true, if successful
	 */
	public boolean replace(Bucket bucket, Object obj) {
		int tries = 0;
		if (bucket == null || obj == null)
			return false;
		while (true) {
			tries++;
			try {
				CouchbaseQueryUtil.setTypeForObject(obj);
				final String DOCUMENT_ID = String.valueOf(PropertyUtils.getProperty(obj, ID));
				bucket.replace(convertEntityToDocument(DOCUMENT_ID, obj));
				CouchbaseQueryUtil.success(DOCUMENT_ID);
				return true;
			} catch (Exception e) {
				LOGGER.info("Retrying...... : Attempt - " + tries);
				sleep();
				if (tries >= MAX_TRIES) {
					LOGGER.warn("Replace failed | {}", e.getMessage());
					return false;
				}
			}
		}
//		if (bucket == null || obj == null)
//			return false;
//		try {
//			CouchbaseQueryUtil.setTypeForObject(obj);
//			final String DOCUMENT_ID = String.valueOf(PropertyUtils.getProperty(obj, ID));
//			bucket.replace(convertEntityToDocument(DOCUMENT_ID, obj));
//			CouchbaseQueryUtil.success(DOCUMENT_ID);
//		} catch (Exception e) {
//			LOGGER.warn("Replace failed | {}", e.getMessage());
//			return false;
//		}
//		return true;
	}

	/**
	 * Extracts the result using the query provided and returns a List of the given
	 * type.
	 *
	 * @param        <T> Generic Type
	 * @param bucket Bucket to extract the query from
	 * @param query  Query to be executed on the Bucket
	 * @param type   Type of Object that the method will return
	 * @return List of data with the given type
	 */
	public <T> List<T> extractResult(Bucket bucket, Statement query, Class<T> type) {
		int tries = 0;
		while (true) {
			tries++;
			N1qlQueryResult result = bucket.query(N1qlQuery.simple(query));
			CouchbaseQueryUtil.logQuery(query);
			if (result.finalSuccess() && result.parseSuccess()) {
				ObjectMapper mapper = new ObjectMapper();
				List<T> content = new ArrayList<>();
				for (N1qlQueryRow row : result) {
					try {
						content.add(mapper.readValue(row.value().toString(), type));
					} catch (IOException e) {
						LOGGER.error(e.getMessage(), e);
						e.printStackTrace();
					}
				}
				return content;
			} else {
				LOGGER.info("Retrying...... : Attempt - " + tries);
				sleep();
				if (tries >= MAX_TRIES) {
					LOGGER.warn("Extract result failed | {}", result.errors());
					return null;
				}
			}
		}
//		N1qlQueryResult result = bucket.query(N1qlQuery.simple(query));
//		if (!result.finalSuccess()) {
//			LOGGER.warn("Query returned with errors: {}", result.errors());
//		}
//		ObjectMapper mapper = new ObjectMapper();
//		List<T> content = new ArrayList<>();
//		for (N1qlQueryRow row : result) {
//			try {
//				content.add(mapper.readValue(row.value().toString(), type));
//			} catch (IOException e) {
//				LOGGER.error(e.getMessage(), e);
//				e.printStackTrace();
//			}
//		}
//		return content;
	}

	public <T> List<T> extractResult(Bucket bucket, String query, Class<T> type) {
		int tries = 0;
		while (true) {
			tries++;
			N1qlQueryResult result = bucket.query(N1qlQuery.simple(query));
			CouchbaseQueryUtil.logQuery(query);
			if (result.finalSuccess() && result.parseSuccess()) {
				ObjectMapper mapper = new ObjectMapper();
				List<T> content = new ArrayList<>();
				for (N1qlQueryRow row : result) {
					try {
						content.add(mapper.readValue(row.value().toString(), type));
					} catch (IOException e) {
						LOGGER.error(e.getMessage(), e);
						e.printStackTrace();
					}
				}
				return content;
			} else {
				LOGGER.info("Retrying...... : Attempt - " + tries);
				sleep();
				if (tries >= MAX_TRIES) {
					LOGGER.warn("Extract result failed | {}", result.errors());
					return null;
				}
			}
		}
//		N1qlQueryResult result = bucket.query(N1qlQuery.simple(query));
//		if (!result.finalSuccess()) {
//			LOGGER.warn("Query returned with errors: {}", result.errors());
//		}
//		ObjectMapper mapper = new ObjectMapper();
//		List<T> content = new ArrayList<>();
//		for (N1qlQueryRow row : result) {
//			try {
//				content.add(mapper.readValue(row.value().toString(), type));
//			} catch (IOException e) {
//				LOGGER.error(e.getMessage(), e);
//				e.printStackTrace();
//			}
//		}
//		return content;
	}

	/**
	 * Extracts the result using the query provided and returns a List of the given
	 * type. Prints the query if user sets logQuery boolean to true
	 *
	 * @param          <T> Generic Type
	 * @param bucket   Bucket to extract the query from
	 * @param query    Query to be executed on the Bucket
	 * @param type     Type of Object that the method will return
	 * @param logQuery Logs the query given if the value is true
	 * @return List of data with the given type
	 */
	public <T> List<T> extractResult(Bucket bucket, Statement query, Class<T> type, boolean logQuery) {
		int tries = 0;
		if (logQuery)
			CouchbaseQueryUtil.logQuery(query);
		while (true) {
			tries++;
			N1qlQueryResult result = bucket.query(N1qlQuery.simple(query));
			CouchbaseQueryUtil.logQuery(query);
			if (result.finalSuccess() && result.parseSuccess()) {
				ObjectMapper mapper = new ObjectMapper();
				List<T> content = new ArrayList<>();
				for (N1qlQueryRow row : result) {
					try {
						content.add(mapper.readValue(row.value().toString(), type));
					} catch (IOException e) {
						LOGGER.error(e.getMessage(), e);
						e.printStackTrace();
					}
				}
				return content;
			} else {
				LOGGER.info("Retrying...... : Attempt - " + tries);
				sleep();
				if (tries >= MAX_TRIES) {
					LOGGER.warn("Extract result failed | {}", result.errors());
					return null;
				}
			}
		}
//		if (logQuery)
//			CouchbaseQueryUtil.logQuery(query);
//		N1qlQueryResult result = bucket.query(N1qlQuery.simple(query));
//		if (!result.finalSuccess()) {
//			LOGGER.warn("Query returned with errors: {}", result.errors());
//		}
//		ObjectMapper mapper = new ObjectMapper();
//		List<T> content = new ArrayList<>();
//		for (N1qlQueryRow row : result) {
//			try {
//				content.add(mapper.readValue(row.value().toString(), type));
//			} catch (IOException e) {
//				LOGGER.error(e.getMessage(), e);
//				e.printStackTrace();
//			}
//		}
//		return content;
	}

	public <T> List<T> extractResult(Bucket bucket, String query, Class<T> type, boolean logQuery) {
		int tries = 0;
		if (logQuery)
			CouchbaseQueryUtil.logQuery(query);
		while (true) {
			tries++;
			N1qlQueryResult result = bucket.query(N1qlQuery.simple(query));
			CouchbaseQueryUtil.logQuery(query);
			if (result.finalSuccess() && result.parseSuccess()) {
				ObjectMapper mapper = new ObjectMapper();
				List<T> content = new ArrayList<>();
				for (N1qlQueryRow row : result) {
					try {
						content.add(mapper.readValue(row.value().toString(), type));
					} catch (IOException e) {
						LOGGER.error(e.getMessage(), e);
						e.printStackTrace();
					}
				}
				return content;
			} else {
				LOGGER.info("Retrying...... : Attempt - " + tries);
				sleep();
				if (tries >= MAX_TRIES) {
					LOGGER.warn("Extract result failed | {}", result.errors());
					return null;
				}
			}
		}
//		if (logQuery)
//			CouchbaseQueryUtil.logQuery(query);
//		N1qlQueryResult result = bucket.query(N1qlQuery.simple(query));
//		if (!result.finalSuccess()) {
//			LOGGER.warn("Query returned with errors: {}", result.errors());
//		}
//		ObjectMapper mapper = new ObjectMapper();
//		List<T> content = new ArrayList<>();
//		for (N1qlQueryRow row : result) {
//			try {
//				content.add(mapper.readValue(row.value().toString(), type));
//			} catch (IOException e) {
//				LOGGER.error(e.getMessage(), e);
//				e.printStackTrace();
//			}
//		}
//		return content;
	}

	/**
	 * Convert entity to document.
	 *
	 * @param docId the Document Id
	 * @param obj   the Object
	 * @return the {@link JsonDocument}
	 */
	public JsonDocument convertEntityToDocument(String docId, Object obj) {
		Gson gson = initializeGson();
		JsonObject json = JsonObject.fromJson(gson.toJson(obj));
		return JsonDocument.create(docId, json);
	}

	/**
	 * Convert entity to document with document expiry.
	 *
	 * @param docId  Document Id
	 * @param expiry Document Expiry
	 * @param obj    Object
	 * @return {@link JsonDocument}
	 */
	public JsonDocument convertEntityToDocument(String docId, int expiry, Object obj) {
		Gson gson = initializeGson();
		JsonObject json = JsonObject.fromJson(gson.toJson(obj));
		return JsonDocument.create(docId, expiry, json);
	}

	/**
	 * Initialize Gson with custom adapters.
	 *
	 * @return {@link Gson}
	 */
	private Gson initializeGson() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		// register type adapters
		gsonBuilder.registerTypeAdapter(DateTime.class, new DateTimeSerializer());
		gsonBuilder.registerTypeAdapter(Date.class, new DateSerializer());
		// end of registering type adapters
		return gsonBuilder.create();
	}

	// debangj
	public boolean executeQuery(String query, boolean logQuery) {
		int tries = 0;
		if (logQuery)
			CouchbaseQueryUtil.logQuery(query);
		while (true) {
			tries++;
			N1qlQueryResult result = bucket.query(N1qlQuery.simple(query));
			if (result.finalSuccess() && result.parseSuccess()) {
				return true;
			} else {
				LOGGER.info("Retrying...... : Attempt - " + tries);
				sleep();
				if (tries >= MAX_TRIES) {
					LOGGER.warn("Query throws an exception: {}", result.errors());
					return false;
				}
			}

		}
//		try {
//			if (logQuery)
//				CouchbaseQueryUtil.logQuery(query);
//			N1qlQueryResult result = bucket.query(N1qlQuery.simple(query));
//			if (!result.finalSuccess())
//				LOGGER.warn("Query returned with errors: {}", result.errors());
//		} catch (Exception ex) {
//			LOGGER.warn("Query throws an exception: {}", ex.getMessage());
//			return false;
//		}
//		return true;
	}

	// debangj
	public boolean executeQuery(Statement query, boolean logQuery) {
		int tries = 0;
		if (logQuery)
			CouchbaseQueryUtil.logQuery(query);
		while (true) {
			tries++;
			N1qlQueryResult result = bucket.query(N1qlQuery.simple(query));
			if (result.finalSuccess() && result.parseSuccess()) {
				return true;
			} else {
				LOGGER.info("Retrying...... : Attempt - " + tries);
				sleep();
				if (tries >= MAX_TRIES) {
					LOGGER.warn("Query throws an exception: {}", result.errors());
					return false;
				}
			}

		}
//		try {
//			if (logQuery)
//				CouchbaseQueryUtil.logQuery(query);
//			N1qlQueryResult result = bucket.query(N1qlQuery.simple(query));
//			if (!result.finalSuccess())
//				LOGGER.warn("Query returned with errors: {}", result.errors());
//		} catch (Exception ex) {
//			LOGGER.warn("Query throws an exception: {}", ex.getMessage());
//			return false;
//		}
//		return true;
	}

	// debangj
	public boolean exists(String id, boolean logQuery) {
		int tries = 0;
		if (logQuery)
			CouchbaseQueryUtil.logQuery(id);
		while (true) {
			tries++;
			try {
				boolean result = bucket.exists(id);
				if (!result)
					LOGGER.warn("Query returned with errors: {}", result);
				return true;
			} catch (Exception ex) {
				LOGGER.info("Retrying...... : Attempt - " + tries);
				sleep();
				if (tries >= MAX_TRIES) {
					LOGGER.warn("Query throws an exception: {}", ex.getMessage());
					return false;
				}
			}
		}
//		try {
//			if (logQuery)
//				CouchbaseQueryUtil.logQuery(id);
//			boolean result = bucket.exists(id);
//			if (!result)
//				LOGGER.warn("Query returned with errors: {}", result);
//		} catch (Exception ex) {
//			LOGGER.warn("Query throws an exception: {}", ex.getMessage());
//			return false;
//		}
//		return true;
	}

	private void sleep() {
		try {
			Thread.sleep(MAX_DELAY);
		} catch (InterruptedException e) {
			LOGGER.warn("Exception: {}", e.getMessage());
		}
	}
	
	public int executeQueryCount(Statement query, boolean logQuery) {
		int tries = 0;
		int count = 0;
		if (logQuery)
			CouchbaseQueryUtil.logQuery(query);
		while (true) {
			tries++;
			N1qlQueryResult result = bucket.query(N1qlQuery.simple(query));
			if (result.finalSuccess() && result.parseSuccess()) {
				LOGGER.info("RESULT {}", result.toString());
				count = (int) result.allRows().get(0).value().get("$1");
				LOGGER.info("RESULT {}", count);
				return count;
			} else {
				LOGGER.info("Retrying...... : Attempt - " + tries);
				sleep();
				if (tries >= MAX_TRIES) {
					LOGGER.warn("Extract result failed | {}", result.errors());
					return 0;
				}
			}
		}
	}

}
