package com.biz.syml;

import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;

import java.util.concurrent.TimeUnit;

import org.codehaus.jettison.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.couchbase.client.CouchbaseClient;


//import com.syml.helper.GenericHelperClass;

public class CouchBaseOperation  {
	Logger logger = LoggerFactory.getLogger(CouchBaseOperation.class);
	private static org.slf4j.Logger log = play.Logger.underlying();

	CouchbaseClient client1 = null;
 	//static Logger log = LoggerFactory.getLogger(CouchBaseOperation.class);

 	public static void main(String[] args) {
 		
 		CouchBaseOperation co = new  CouchBaseOperation();
 		CouchbaseClient   cl= co.getConnectionToCouchBase();
 		if(null!=cl){
 			
 			log.info("connection succes ");
 		}
 	/*	
	System.out.println(	new CouchBaseOperation().getConnectionToCouchBase());
	
	System.out.println("Connection  Success");
	*/}
 	
 	
 	
	public CouchbaseClient getConnectionToCouchBase() {
		String url=null;
		String bucket=null;
		String pass=null;
		int maximumRetry=0;
		
		Properties prop = new Properties();
		ArrayList<URI> nodes = new ArrayList<URI>();
		try {

			// getting connection parameter
			prop.load(CouchBaseOperation.class.getClassLoader()
					.getResourceAsStream("underwrite.properties"));

		} catch (Exception e) {
			log.error("error in getting the property file" + e);
		}
		
		
		try {
			
			log.info("inside getConnectionToCouchBase method of CouchBaseOperation class");
		//	url = prop.getProperty("couchBaseUrl");
			 url= prop.getProperty("couchBaseUrl3");
			 bucket = prop.getProperty("couchBaseBucketName");
			 pass =prop.getProperty("couchBaseBucketPassword");
			 try{
			 maximumRetry=new Integer(prop.getProperty("maximumRetry"));
			 }catch(Exception e){
				 
			 }
			 // 1. Add one or more nodes of your cluster (exchange the IP with
			// yours)
			nodes.add(URI.create(url));
			log.debug("connecting .....");
			
			client1 = getCouchbaseConnectionOne(nodes, bucket, pass, maximumRetry);
			if(client1==null){
				 url= prop.getProperty("couchBaseUrl1");
				 bucket = prop.getProperty("couchBaseBucketName2");
				 pass =prop.getProperty("couchBaseBucketPassword2");
				 ArrayList<URI> nodes1 = new ArrayList<URI>();
				nodes1.add(URI.create(url));

				client1=getCouchbaseConnectionTwo(nodes1,bucket, pass, maximumRetry);
				 if(client1==null) {
					 url= prop.getProperty("couchBaseUrl2");
					 bucket = prop.getProperty("couchBaseBucketName2");
					 pass =prop.getProperty("couchBaseBucketPassword2");
			 nodes1 = new ArrayList<URI>();
					nodes1.add(URI.create(url));

					client1=getCouchbaseConnectionThree(nodes1,bucket, pass, maximumRetry);
				
					if(client1==null){
					 //Send mail error in connecting couchbase
						log.error("Error in Connecting Couhbase");
					}
					}
			}else{
				log.info("connection to couchbase successfully  :   ");
			}
		} catch (Exception e) {
			log.error("error while connecting to couchbase " + e);
			
			}
	
		
		return client1;
	}

	public CouchbaseClient getCouchbaseConnectionOne(ArrayList<URI> nodes,String bucketName,String password,int maximumRetry){
		
		
		log.debug("inside GetcoonectionOne  method of couchbase");
		int retry=1;
		CouchbaseClient client=null;
		while(retry<=maximumRetry && client==null){
		try{
			client=new CouchbaseClient(nodes, bucketName, password);
		}catch(Exception e){
			client=null;
			log.error(" getCouchbaseConnectionOne  Retry..    "+retry);
			retry+=1;
			log.error("error in connecting Couchbase one"+e);
		}
		
		}
		return client;
	}
	
public CouchbaseClient getCouchbaseConnectionTwo(ArrayList<URI> nodes,String bucketName,String password,int maximumRetry){
	log.debug("inside GetCouchbaseConectionTwo  method of couchbase");
		int retry=1;
		CouchbaseClient client=null;
		while(retry<=maximumRetry && client==null){
		try{
			log.debug(nodes +""+ bucketName +""+password );
			client=new CouchbaseClient(nodes, bucketName, password);
		}catch(Exception e){
			client=null;
			log.error(" getCouchbaseConnectionTwo Retry..   "+retry);
			retry+=1;
			log.error("error in connecting Couchbase two"+e);
		}
		
		}
		return client;
	}


public CouchbaseClient getCouchbaseConnectionThree(ArrayList<URI> nodes,String bucketName,String password,int maximumRetry){
	log.debug("inside GetCouchbaseConectionThree  method of couchbase");
		int retry=1;
		CouchbaseClient client=null;
		while(retry<=maximumRetry && client==null){
		try{
			log.info(nodes +""+ bucketName +""+password );
			client=new CouchbaseClient(nodes, bucketName, password);
			
		}catch(Exception e){
			client=null;
			log.error(" getCouchbaseConnectionThree Retry..   "+retry);
			retry+=1;
			log.error("error in connecting Couchbase three"+e);
		}
		
		}
		return client;
	}
 	
 	
 	/*-----------------------------------------------------------*/
 	
 	
 	
 	/*public CouchbaseClient getConnectionToCouchBase() {
		String url=null;
		String bucket=null;
		String pass=null;
		int maximumRetry=0;
		InputStream inputStream;
		
		Properties prop=new Properties();
		
		ArrayList<URI> nodes = new ArrayList<URI>();
		
		
		
		try {
			String propFileName = "underwrite.properties";
			 
			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
 
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}
			log.info("inside getConnectionToCouchBase method of CouchBaseOperation class");
			url = prop.getProperty("couchBaseUrl3");
			System.out.println("URL  Is"+url);
		//	 url="http://52.4.161.145:8091/pools";
			// bucket ="symldev";

			bucket= prop.getProperty("couchBaseBucketName3");
			System.out.println("Bucket   Is"+bucket);

			  
			pass= prop.getProperty("couchBaseBucketPassword3");
			System.out.println("Pasword    Is"+pass);

			 //pass ="symL@0115";
					 //prop.getProperty("couchBaseBucketPassword");
		
			nodes.add(URI.create(url));
			log.debug("connecting .....");

		

			client1 = new CouchbaseClient(nodes, bucket, pass);
		} catch (IOException e) {
			log.error("error while connecting to couchbase" + e);
			
		}
		return client1;
	}*/
 	public static Properties readConfigfile() {

		Properties prop = new Properties();
		try {
			/*prop.load(CouchBaseOperation.class.getClassLoader().getResourceAsStream(
					"config.properties"));*/
		} catch (Exception e) {
		}
		return prop;
	}


	public CouchbaseClient getConnectionToCouchBaseForDev() {
		String url=null;
		String bucket=null;
		String pass=null;
		int maximumRetry=0;
		
		//Properties prop=CouchBaseOperation.readConfigfile();
		ArrayList<URI> nodes = new ArrayList<URI>();
		
		
		try {
			
			log.info("inside getConnectionToCouchBase method of CouchBaseOperation class");
		//	url = prop.getProperty("couchBaseUrl");
			 url="http://52.4.161.145:8091/pools";
					 //prop.getProperty("couchBaseUrl1");
			 bucket ="symldev";
					 //prop.getProperty("couchBaseBucketName");
			 pass ="symL@0115";
					 //prop.getProperty("couchBaseBucketPassword");
			 try{
			 //maximumRetry=new Integer(prop.getProperty("maximumRetry"));
			// System.out.println(url  +" "+bucket+""+  pass +"" +prop.getProperty("couchBaseBucketPassword2"));

			 }catch(Exception e){
				 e.printStackTrace();
			 }
			 // 1. Add one or more nodes of your cluster (exchange the IP with
			// yours)
			nodes.add(URI.create(url));
			log.debug("connecting .....");
			
		client1 = getCouchbaseConnectionOne(nodes, bucket, pass, maximumRetry);
			
		} catch (Exception e) {
			log.error("error while connecting to couchbase " + e);
			
			}
	
		
		return client1;
	}

	/*public CouchbaseClient getCouchbaseConnectionOne(ArrayList<URI> nodes,String bucketName,String password,int maximumRetry){
		
		
		log.debug("inside GetcoonectionOne  method of couchbase");
		int retry=1;
		CouchbaseClient client=null;
		while(retry<=maximumRetry && client==null){
			log.debug("inside whilw GetcoonectionOne  method of couchbase");

		try{
		client=new CouchbaseClient(nodes, bucketName, password);
		}catch(Exception e){
			client=null;
			log.error(" getCouchbaseConnectionOne  Retry..    "+retry);
			retry+=1;
			log.error("error in connecting Couchbase one"+e);
		}
		
		}
		return client;
	}*/
	
/*public CouchbaseClient getCouchbaseConnectionTwo(ArrayList<URI> nodes,String bucketName,String password,int maximumRetry){
	log.debug("inside GetCouchbaseConectionTwo  method of couchbase");
		int retry=1;
		CouchbaseClient client=null;
		while(retry<=maximumRetry && client==null){
		try{
			System.out.println(nodes +""+ bucketName +""+password );
			client=new CouchbaseClient(nodes, bucketName, password);
		}catch(Exception e){
			client=null;
			log.error(" getCouchbaseConnectionTwo Retry..   "+retry);
			retry+=1;
			log.error("error in connecting Couchbase two"+e);
		}
		
		}
		return client;
	}*/
	

	public void storeDataInCouchBase(String key,JSONObject jsonData) {
		try {
			log.info("inside storeDataInCouchBase method of CouchBaseOperation class");
			client1 = getConnectionToCouchBase();
		
			// convert data into json
			
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");            //get current date time with Calendar()
            Calendar cal = Calendar.getInstance();
            String currentDateTime=(dateFormat.format(cal.getTime())); 
            jsonData.put("Submission_Date_Time1b",currentDateTime);
		

			log.debug("sending data...");
		Thread.sleep(2000);
			
			client1.set(key, jsonData.toString());
			System.out.println("sending data... done with id :"+key);
			log.debug("sending data... done with id :"+key);
	   	  closeCouchBaseConnection();

		} catch (Exception e) {
			log.error("error while storing data into couchbase : " + e);
		}

	}
	
	
	
	
	

	
	
	public void closeCouchBaseConnection() {
		log.debug("closing connection");
		client1.shutdown(900l, TimeUnit.MILLISECONDS);
	

	}
	
	
	
	
	
	public JSONObject getCouchBaseData(String key){
		log.debug("Inside Get couchbase data based on key");
		client1=getConnectionToCouchBase();
		String object=(String)client1.get(key);
		
		JSONObject jsonObject=null;
		return jsonObject;
	}
	


}

