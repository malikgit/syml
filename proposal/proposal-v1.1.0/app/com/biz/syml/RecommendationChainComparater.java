package com.biz.syml;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import controllers.Recommendation;

public class RecommendationChainComparater implements Comparator<Recommendation> {
	
	private List<Comparator<Recommendation>> listComparators;
	 
    @SafeVarargs
    public RecommendationChainComparater(Comparator<Recommendation>... comparators) {
        this.listComparators = Arrays.asList(comparators);
    }

	@Override
	public int compare(Recommendation emp1, Recommendation emp2) {
		 for (Comparator<Recommendation> comparator : listComparators) {
	            int result = comparator.compare(emp1, emp2);
	            if (result != 0) {
	                return result;
	            }
	        }
	        return 0;
	}

}
