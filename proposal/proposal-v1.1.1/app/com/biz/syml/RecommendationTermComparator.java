package com.biz.syml;

import java.util.Comparator;

import controllers.Recommendation;

public class RecommendationTermComparator implements Comparator<Recommendation> {

	@Override
	public int compare(Recommendation rec1, Recommendation rec2) {
		
		String test1 = rec1.getTerm();
		String test2 = rec2.getTerm();
		
		
		Integer one;
		
		Integer forint = Integer.parseInt(test1.substring(0));
		//System.out.println("forint"+forint);
		if(forint==0){
			one=0;
		}
		else{
		 one  = forint-1;
		}
		//System.out.println("one here "+one);
		
		
		Integer forint2 = Integer.parseInt(test2.substring(0));
		//System.out.println("forint2"+forint2);
		Integer two;
		if(forint2==0){
			two=0;
		}else{
			two=forint2-1;
		}
		//System.out.println("tow here "+two);
		
		
		//int num1 = Integer.parseInt(rec1.substring(0, rec1.indexOf(" ") - 1));
		
		return one.compareTo(two);
		}

}
