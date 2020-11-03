package com.andrewbrianputosa.bikerentalshop.util;


import static com.couchbase.client.java.query.dsl.Expression.x;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.*;

import com.andrewbrianputosa.bikerentalshop.util.CouchbaseConstant;
import com.couchbase.client.java.query.Statement;


public class CouchbaseQueryUtil {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(CouchbaseQueryUtil.class);

	/** The Constant DELIMITER. */
	private static final String DELIMITER = ", ";

	/**
	 * Instantiates a new CouchbaseQueryUtil.
	 */
	private CouchbaseQueryUtil() {
		throw new IllegalStateException("CouchbaseQueryUtil Class");
	}

	/**
	 * Logs the query.
	 *
	 * @param query the query
	 */
	public static void logQuery(Statement query) {
		LOGGER.info("Running Query : {}", query);
	}

	public static void logQuery(String query) {
		LOGGER.info("Running Query : {}", query);
	}

	/**
	 * Logs success message and prints out the reference id of the document.
	 *
	 * @param docId the reference id of the document
	 */
	public static void success(String docId) {
		LOGGER.info("Transaction Success | Reference ID : {}", docId);
	}

	/**
	 * Sets the type for object.
	 *
	 * @param obj the Object
	 */
	public static void setTypeForObject(Object obj) {
		try {
			PropertyUtils.setProperty(obj, CouchbaseConstant.JAVA_CLASS.value(), obj.getClass().getSimpleName());
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			LOGGER.warn("ERROR ON setTypeForObject : {}", e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Collate the list and join it by a delimiter ", ".
	 *
	 * @param couchbaseConstant the couchbase constant
	 * @return the string
	 */
	public static String collate(CouchbaseConstant... couchbaseConstant) {
		List<String> collateList = new ArrayList<>();
		for (CouchbaseConstant constant : couchbaseConstant) {
			collateList.add(constant.value());
		}
		return collateList.stream().collect(Collectors.joining(DELIMITER));
	}

	/**
	 * Collate the list and join it by a delimiter ", " adding an identifier on what
	 * bucket will the field will get from.
	 *
	 * @param from              the from
	 * @param couchbaseConstant the couchbase constant
	 * @return the string
	 */
	public static String collateFrom(String from, CouchbaseConstant... couchbaseConstant) {
		List<String> collateList = new ArrayList<>();
		for (CouchbaseConstant constant : couchbaseConstant) {
			collateList.add(constant.from(from));
		}
		return collateList.stream().collect(Collectors.joining(DELIMITER));
	}

	// debangj
	public static <T> String selectAll(String alias, Class<T> type) {
		List<String> collateList = new ArrayList<>();
		Field[] dfields = type.getDeclaredFields();
		Field[] efields = type.getSuperclass().getDeclaredFields();
		for (Field field : dfields) {
//			if(!collateList.contains(String.format("%s.`%s`", alias, field.getName()))) {
//				collateList.add(String.format("%s.`%s`", alias, field.getName()));
//			}
			// FIX for null Document ID
			if(field.getName().equals("id")){
				if(!collateList.contains(String.format("meta(%s).`%s`", alias, field.getName()))) {
					collateList.add(String.format("meta(%s).`%s`", alias, field.getName()));
				}
				
			}
			else {
				if(!collateList.contains(String.format("%s.`%s`", alias, field.getName()))) {
					collateList.add(String.format("%s.`%s`", alias, field.getName()));
				}
			}
		}
		for (Field field : efields) {
			if(!collateList.contains(String.format("%s.`%s`", alias, field.getName()))) {
				collateList.add(String.format("%s.`%s`", alias, field.getName()));
			}
				
		}
		return collateList.stream().collect(Collectors.joining(DELIMITER));
	}
	
	public static <T> String selectAllWithoutJavaClass(String alias, Class<T> type) {
		List<String> collateList = new ArrayList<>();
		Field[] dfields = type.getDeclaredFields();
		for (Field field : dfields) {
			// FIX for null Document ID
			if(field.getName().equals("id")){
				if(!collateList.contains(String.format("meta(%s).`%s`", alias, field.getName()))) {
					collateList.add(String.format("meta(%s).`%s`", alias, field.getName()));
				}
				
			}
			else {
				if(!collateList.contains(String.format("%s.`%s`", alias, field.getName()))) {
					collateList.add(String.format("%s.`%s`", alias, field.getName()));
				}
			}
		}
		return collateList.stream().collect(Collectors.joining(DELIMITER));
	}

	public static String millisToTZ(String expression) {
		return "MILLIS_TO_TZ(" + expression + ", 'Asia/Manila', '1111-11-11')";
	}
	
	public static String count(String expression) {
		return "COUNT(" + expression + ")";
	}

}

