package com.syml.couchbase.dao.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.syml.couchbase.dao.CouchbaseServiceException;
import com.syml.couchbase.dao.CouchbaseUtil;

public class CouchBaseService {
	static	Logger logger=LoggerFactory.getLogger(CouchBaseService.class);

	public void storeDataToCouchbase(String key, Object object)
			throws CouchbaseServiceException {
		try {
			logger.info("inside storeDataToCouchbase method of CouchBaseService class");
			CouchbaseUtil couchbaseUtil = new CouchbaseUtil();
			Bucket bucket = couchbaseUtil.getCouchbaseClusterConnection();

			ObjectMapper objectMapper = new ObjectMapper();
			String jsonData;

			jsonData = objectMapper.writeValueAsString(object);

			JsonObject jsonObject = JsonObject.fromJson(jsonData);
			JsonDocument jsonDocument = JsonDocument.create(key, jsonObject);
			bucket.upsert(jsonDocument);
			logger.debug("storing   data...  in couchbase done with couchbase id :"
					+ key);
		} catch (JsonProcessingException | CouchbaseServiceException e) {
			throw new CouchbaseServiceException(
					"error in storing data into couchbase  " + e);
		}
	}
	
	public void storeDataToCouchbase(String key, JsonObject jsonObject)
			throws CouchbaseServiceException {
		try {
			logger.info("inside storeDataToCouchbase method of CouchBaseService class");
			CouchbaseUtil couchbaseUtil = new CouchbaseUtil();
			Bucket bucket = couchbaseUtil.getCouchbaseClusterConnection();

			
			JsonDocument jsonDocument = JsonDocument.create(key, jsonObject);
			bucket.upsert(jsonDocument);
			logger.debug("storing   data...  in couchbase done with couchbase id :"
					+ key);
		} catch (CouchbaseServiceException e) {
			throw new CouchbaseServiceException(
					"error in storing data into couchbase  " + e);
		}
	}

	public List<String> getReferralTriggerDataFromCouhbase(String key)
			throws CouchbaseServiceException {
		logger.info("inside getReferralTriggerDataFromCouhbase method of CouchBaseService class");
		ArrayList<String> list = new ArrayList<String>();
		String email = "";
		String firstName = "";
		String LastName = "";
		String phoneNumber = "";
		CouchbaseUtil couchbaseUtil = new CouchbaseUtil();
		Bucket bucket = couchbaseUtil.getCouchbaseClusterConnection();

		JsonDocument jsonDocument = bucket.get(key);
		try {
			JsonObject jsonObject = jsonDocument.content();

			phoneNumber = (String) jsonObject.get("partner_mobile");
			email = (String) jsonObject.get("email_from");
			String name = (String) jsonObject.get("name");
			try {
				String arString[] = name.split("_");
				firstName = arString[0];
				LastName = arString[1];
			} catch (Exception e) {

			}
			list.add(phoneNumber);
			list.add(email);
			list.add(firstName);
			list.add(LastName);
		} catch (Exception e) {
			logger.error("error in getting the referralTriggerData  from couhbase for given key ="
					+ key);
		}
return list;
	}
	
	public JsonObject getCouhbaseDataByKey(String key) throws CouchbaseServiceException{
		logger.info("inside getCouhbaseDataByKey method of CouchBaseService class");
		CouchbaseUtil couchbaseUtil = new CouchbaseUtil();
		Bucket bucket = couchbaseUtil.getCouchbaseClusterConnection();

		JsonDocument jsonDocument = bucket.get(key);
		JsonObject jsonObject = jsonDocument.content();
		return jsonObject;
	}

}
