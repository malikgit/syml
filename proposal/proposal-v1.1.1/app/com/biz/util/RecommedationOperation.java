package com.biz.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import play.Logger;
import play.data.DynamicForm;

import com.biz.syml.RecommendationChainComparater;
import com.biz.syml.RecommendationTermComparator;

import controllers.Recommendation;

public class RecommedationOperation {
	private static org.slf4j.Logger logger = Logger.underlying();
	public static Map<String,List<Recommendation>> getRecList(Set<Recommendation> rec){
		
		Map<String,List<Recommendation>> mapofrec =new HashMap<>();
		List<Recommendation> arrayVariable=new ArrayList<>();
		List<Recommendation> arrayFixed=new ArrayList<>();
		List<Recommendation> arrayLoc=new ArrayList<>();
		try{
			//System.out.println("---inside rec ----"+rec.size());
		for(Recommendation recom:rec){
		
			
			if(recom.getMortgageType().equalsIgnoreCase("1")){
			
				arrayLoc.add(recom);
				//System.out.println("------arrayLoc------"+arrayLoc.size());
				
				mapofrec.put("arrayLoc",arrayLoc);
			}
			
		
			else if (recom.getMortgageType().equalsIgnoreCase("2")) {
				
				String term =recom.getTerm();
				String sterm =setTerm2(term);
				recom.setTerm(sterm);
				arrayVariable.add(recom);
				//System.out.println("------arrayVariable------"+arrayVariable.size());
				Collections.sort(arrayVariable, new RecommendationChainComparater(
		                new RecommendationTermComparator()
		                ));
				
				mapofrec.put("arrayVariable",arrayVariable);
				
			}
			else if (recom.getMortgageType().equalsIgnoreCase(
					"3")) {
				String term =recom.getTerm();
				String sterm =setTerm2(term);
				recom.setTerm(sterm);
				arrayFixed.add(recom);
				//System.out.println("------arrayFixed------"+arrayFixed.size());
				Collections.sort(arrayFixed, new RecommendationChainComparater(
		                new RecommendationTermComparator()
		                ));
				mapofrec.put("arrayFixed", arrayFixed);
		        		
			
			
		}
		
		
	}}catch(NullPointerException e){
	logger.error("null occured : "+e.getMessage());	
	}
		return mapofrec;	
	}	
	
	public static Recommendation getBeanofRecommendation(DynamicForm data){
	Recommendation rec = new Recommendation();
	//rec.setCashbackPercent(cashbackPercent);
			
		data.get("");
		return null;
		
	}
	
	
	private static String setTerm2(String term){
		Integer intterm = Integer.parseInt(term.substring(0));
		intterm=intterm-1; 
		String sterm = Integer.toString(intterm);
		
		
		return sterm;
	}
	

}
