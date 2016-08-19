package com.syml.couchbase.dao.service;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.json.JsonObject;
import com.syml.couchbase.dao.CouchbaseDaoServiceException;
import com.syml.couchbase.dao.CouchbaseUtil;


public class CouchbaseDaoServiceTest {

	public  final String REFERRAL_TRIGGER_DATA_COUCHBASE_KEY="ReferralTrigerData_";

	Logger logger=LoggerFactory.getLogger(CouchbaseDaoServiceTest.class);
	CouchBaseService couchbase = new CouchBaseService();
	CouchbaseUtil couchbaseUtil = new CouchbaseUtil();

	
	/**
	 * To testcouchbase connection is working or not 
	 * @throws CouchbaseDaoServiceException
	 */
	@Test
	public void testCouchbaseConnection() throws CouchbaseDaoServiceException {
		logger.info("testCouchbase Connection ");
		Bucket bucket = couchbaseUtil.getCouchbaseClusterConnection();
		Assert.assertNotNull("Bucket should not be null", bucket);

	}

	/**
	 * To test jsondata is stored in couchbase or not 
	 * @throws CouchbaseDaoServiceException
	 */
	@Test
	public void testCouchbseStoredData() throws CouchbaseDaoServiceException {
		logger.info("testCouchbaseStoredata  ");
		JsonObject jsonObject = JsonObject.empty().put("TestName", "TestNAme");
		couchbase.storeDataToCouchbase("TestKey", jsonObject);

		jsonObject = couchbase.getCouhbaseDataByKey("TestKey");
		logger.info("jsonobject from couchbase ", jsonObject);
		Assert.assertNotNull("json Object should not be null", jsonObject);
		String value = jsonObject.getString("TestName");
		logger.info("data from Couchbase ", value);
		Assert.assertEquals(
				"data get from couchbase should be equal  to given value  TestName== TestName ",
				value, "TestNAme");

	}

	/**
	 * To test by passing object and convert into json format and storing into couchbase 
	 * @throws CouchbaseDaoServiceException
	 */
	@Test
	public void testCouchbaseStoreObject() throws CouchbaseDaoServiceException {
		Contact contact = new Contact();
		contact.setId(120);
		contact.setEmail("test@gmail.com");
		contact.setName("TestName");

		couchbase.storeDataToCouchbase(contact.getId() + "", contact);

		JsonObject jsonObject = couchbase.getCouhbaseDataByKey(contact.getId()
				+ "");
		logger.info("jsonobject from couchbase ", jsonObject);
		Assert.assertNotNull("json Object should not be null", jsonObject);
		String value = jsonObject.getString("email");

		logger.info("data from Couchbase ", value);
		Assert.assertEquals(
				"the value get from couchbase should be equal to test@gmail.com ",
				value, "test@gmail.com");

	}

	/**
	 * To test get ReferralDetails from couchbase  for Postive Test case
	 * @throws CouchbaseDaoServiceException
	 */
	@Test
	public void testGetReferralDataFromCouchbase()
			throws CouchbaseDaoServiceException {
		logger.debug("inside testGetReferralDataFromCouchbase method ");
		List<String> referralDetailsList = couchbase
				.getReferralTriggerDataFromCouhbase(REFERRAL_TRIGGER_DATA_COUCHBASE_KEY + 231);
		Assert.assertNotNull(
				"Referral trigger data from Couchbase should not be null ",
				referralDetailsList);
		Assert.assertTrue("the size list should four ",
				referralDetailsList.size() == 4);

	}
	
	/**
	 * To test get ReferralDetails from couchbase  for negative  Test case
	 * @throws CouchbaseDaoServiceException
	 */
	@Test
	public void testGetReferralDataFromCouchbaseForNegativeTestcase()
			throws CouchbaseDaoServiceException {
		logger.debug("inside testGetReferralDataFromCouchbaseForNegativeTestcase method ");
		List<String> referralDetailsList = couchbase
				.getReferralTriggerDataFromCouhbase(REFERRAL_TRIGGER_DATA_COUCHBASE_KEY + 2311);
		Assert.assertTrue(
				"Referral trigger data from Couchbase should be empty ",
				referralDetailsList.isEmpty());
	
	}
}
