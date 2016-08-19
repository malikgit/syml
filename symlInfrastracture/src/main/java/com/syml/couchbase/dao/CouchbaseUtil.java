package com.syml.couchbase.dao;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.env.CouchbaseEnvironment;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;

public class CouchbaseUtil {

	Logger logger=LoggerFactory.getLogger(CouchbaseUtil.class);
	private final String COUHBASE_HOST_ONE = "couchBaseUrl3";
	private final String COUHBASE_HOST_TWO = "couchBaseUrl1";
	private final String COUHBASE_HOST_THREE = "couchBaseUrl2";
	private final String COUHBASE_BUCKET_NAME = "couchBaseBucketName";
	private final String COUHBASE_BUCKET_PASSWORD = "couchBaseBucketPassword";
	private final int MAX_RETRY_CONNECTION = 3;

	/**
	 * @throws CouchbaseServiceException
	 * 
	 */
	public synchronized Bucket getCouchbaseClusterConnection()
			throws CouchbaseServiceException {

		

		Bucket bucket=null;
		Properties properties = ConfigurationFileReader.readConfigfile();
		if (properties == null)
			throw new CouchbaseServiceException(
					"configuration file loading is failed or not exist ");

	 List<String> hostList = Arrays.asList(
				properties.getProperty(COUHBASE_HOST_ONE),
				properties.getProperty(COUHBASE_HOST_TWO),
				properties.getProperty(COUHBASE_HOST_THREE));

		CouchbaseEnvironment environment = DefaultCouchbaseEnvironment
				.builder().connectTimeout(8 * 1000) // 8 Seconds in milliseconds
				.keepAliveInterval(3600 * 1000) // 3600 Seconds in milliseconds
				.build();

		//to get connection by passing all host list
	 bucket = getConnectionOfCouhbaseByGivenHostList(environment,hostList, properties);
	if(bucket==null)
		reTryForcouhbaseConnection(bucket, environment, hostList, properties);
	 
		return bucket;
	}

	/**
	 * retrying for couchbase connection if node1 fails goto node 2 if node2 fails goto node node 3 if node 3 fails  throwing exception 
	 * @param bucket
	 * @param environment
	 * @param hostList
	 * @param properties
	 * @return return bucket
	 * @throws CouchbaseServiceException
	 */
	private Bucket reTryForcouhbaseConnection(Bucket bucket,CouchbaseEnvironment environment, List<String> hostList,
			Properties properties) throws CouchbaseServiceException{
	
			 //TRy for connection with host one 
			 hostList = Arrays.asList(properties.getProperty(COUHBASE_HOST_ONE));
			 bucket = getConnectionOfCouhbaseByGivenHostList(environment,hostList, properties);
			 if(bucket==null){
				 //TRy for connection with host two 

				 hostList = Arrays.asList(properties.getProperty(COUHBASE_HOST_TWO));
				 bucket = getConnectionOfCouhbaseByGivenHostList(environment,hostList, properties);
				 if(bucket==null){
					 //try for connection with host three
					 hostList = Arrays.asList(properties.getProperty(COUHBASE_HOST_THREE));
					 bucket = getConnectionOfCouhbaseByGivenHostList(environment,hostList, properties);
					 if(bucket==null)
						 throw new CouchbaseServiceException("Error in connecting couhcbase Cluster please check the hosts running or not ");
				 }//end if
			 }//end if
		 
		 return bucket;
	}

	
	/**
	 *  get connection of couchbase by given host 
	 * @param environment
	 * @param hostList
	 * @param properties
	 * @return bucket
	 */
	private Bucket getConnectionOfCouhbaseByGivenHostList(
			CouchbaseEnvironment environment, List<String> hostList,
			Properties properties) {
		Bucket bucket = null;
		int count = 0;
		Cluster cuCluster = null;
		while (count < MAX_RETRY_CONNECTION) {
			logger.info("couchbase connection try for the host  "+hostList);
		
			try {
				if(cuCluster==null)
					cuCluster = CouchbaseCluster.create(environment,hostList);

				bucket = cuCluster.openBucket(
						properties.getProperty(COUHBASE_BUCKET_NAME),
						properties.getProperty(COUHBASE_BUCKET_PASSWORD));

			} catch (Exception e) {
				bucket=null;
				logger.error("error in connection couchbase connection with hostLsit= "
						+ hostList);
			}
			count++;
			
			if (bucket != null)
				break;
		}
		return bucket;
	}

	
	
}
