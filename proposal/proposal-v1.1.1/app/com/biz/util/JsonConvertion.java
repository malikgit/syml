package com.biz.util;

import java.io.IOException;


import org.codehaus.jettison.json.JSONObject;

import play.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


import controllers.MaximumPurchase;
import controllers.OrignalDetails;
import controllers.RecommendDetails;

public class JsonConvertion{
	private static org.slf4j.Logger logger = Logger.underlying();
	public static  OrignalDetails fromJsonforOriginalDetails(String json) {
		OrignalDetails recdetails=null;
		try {
			recdetails = new ObjectMapper().readValue(json, OrignalDetails.class);
		} catch (JsonParseException e) {
			logger.info("Json Parse Excettion : "+e.getMessage());
		} catch (JsonMappingException e) {
			logger.info("Json Mapping  Excettion : "+e.getMessage());
		} catch (IOException e) {
			logger.info("IO  Excettion : "+e.getMessage());
		}
		

		return recdetails;
}
	
	public static   RecommendDetails fromJsonforRecommendDetails(String json) 
    {
		
		RecommendDetails recdetails = null;
		try {
			recdetails = new ObjectMapper().readValue(json, RecommendDetails.class);
		} catch (JsonParseException e) {
			
			logger.info("Json Parse Excettion : "+e.getMessage());
		} catch (JsonMappingException e) {
			logger.info("Json Parse Excettion : "+e.getMessage());
		} catch (IOException e) {
			logger.info("Json Parse Excettion : "+e.getMessage());
		}
		

		return recdetails;
}
	public static   MaximumPurchase fromJsontoMaximumPurchase(String json) 
    {
		
		MaximumPurchase maxpurchase = null;
		try {
			
			maxpurchase = new ObjectMapper().readValue(json, MaximumPurchase.class);
		} catch (JsonParseException e) {
			
			logger.info("Json Parse Excettion : "+e.getMessage());
		} catch (JsonMappingException e) {
			logger.info("Json Parse Excettion : "+e.getMessage());
		} catch (IOException e) {
			logger.info("Json Parse Excettion : "+e.getMessage());
		}
		

		return maxpurchase;
}
	public static   JSONObject getJsonObject(String json) 
    {
		JSONObject obj=null;
		try{
		obj= new JSONObject(json);
		
		}catch(Exception js){
			logger.error("JsonException occured while parsing from String :  "+js.getMessage());
		}
		return obj;
		
		
		
		

		
}



}
