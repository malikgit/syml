package com.biz.util;

import java.util.ArrayList;
import java.util.Set;

import org.codehaus.jettison.json.JSONObject;

import play.Logger;
import controllers.CombinedRecommendation;
import controllers.MarketingNotes;
import controllers.MaximumPurchase;

public class MarketingNotesOperation {
	private static org.slf4j.Logger logger = Logger.underlying();

	public static ArrayList<MaximumPurchase> getListofMax(
			Set<MarketingNotes> marketsnotes) {

		ArrayList<MaximumPurchase> listofmax = new ArrayList<MaximumPurchase>();

		for (MarketingNotes marketingNotes : marketsnotes) {
			if (marketingNotes.getNoteName().equalsIgnoreCase(
					"MaxMortgageTable")) {
				logger.debug("MaxMortgageTable inside :");

				try {
					JSONObject js = JsonConvertion.getJsonObject(marketingNotes
							.getNoteContent().toString());

					MaximumPurchase ps = JsonConvertion
							.fromJsontoMaximumPurchase(js.get(
									"variableMortgage(House)").toString());
					ps.setKey("variableMortgage(House)");
					listofmax.add(ps);

				} catch (Exception e) {
					logger.error("error on variableMortgage while getting from json : "
							+ e.getMessage());
				}

				try {
					JSONObject js = JsonConvertion.getJsonObject(marketingNotes
							.getNoteContent().toString());
					MaximumPurchase ps = JsonConvertion
							.fromJsontoMaximumPurchase(js.get(
									"fixedMortgageHouse").toString());
					ps.setKey("fixedMortgageHouse");
					listofmax.add(ps);

				} catch (Exception e1) {
					logger.error("error on variableMortgage while getting from json : "
							+ e1.getMessage());
				}

				try {
					JSONObject js = JsonConvertion.getJsonObject(marketingNotes
							.getNoteContent().toString());

					MaximumPurchase ps = JsonConvertion
							.fromJsontoMaximumPurchase(js.get(
									"VariableMortgageCondo").toString());
					ps.setKey("VariableMortgageCondo");
					listofmax.add(ps);

				} catch (Exception e1) {
					logger.error("error on variableMortgage while getting from json : "
							+ e1.getMessage());
				}
				try {

					JSONObject js = JsonConvertion.getJsonObject(marketingNotes
							.getNoteContent().toString());

					MaximumPurchase ps = JsonConvertion
							.fromJsontoMaximumPurchase(js.get(
									"FixedMortgageCondo").toString());
					ps.setKey("FixedMortgageCondo");
					listofmax.add(ps);

				} catch (Exception e1) {
					logger.error("error on variableMortgage while getting from json : "
							+ e1.getMessage());
				}
			}

		}
		return listofmax;
	}
	
	public static ArrayList<MarketingNotes> getListofCombineTabe(
			Set<MarketingNotes> marketsnotes) {

		ArrayList<MarketingNotes> listofcombinedTable = new ArrayList<MarketingNotes>();

		for (MarketingNotes marketingNotes : marketsnotes) {
			if (marketingNotes.getNoteName().equalsIgnoreCase(
					"CombinedTable")) {
				String marketing  = marketingNotes.getNoteContent();
				listofcombinedTable.add(marketingNotes);
				
				Logger.info("for mak0000000000"+marketing);
				
			}

		}
		return listofcombinedTable;
	}
	
	public static ArrayList<CombinedRecommendation> getlistofCombinedRecommendation(
			Set<CombinedRecommendation> combineRec) {

		ArrayList<CombinedRecommendation> listofcombinedRec = new ArrayList<CombinedRecommendation>();

		for (CombinedRecommendation combine : combineRec) {
			
				
			String ammor = 	combine.getAdditionalAmortization();
			Double doubletotal =Double.parseDouble(combine.getTotalPayment());
			
			logger.info("doubletotal : "+doubletotal);
			
			String com1 = String.format("%.2f",doubletotal );
			logger.info("com1 : "+com1);
			System.out.println(com1);
			combine.setTotalPayment(com1);
			
			listofcombinedRec.add(combine);
				
			
				
				//Logger.info("combine recommended : "+listofcombinedRec.size());
				
			}
		return listofcombinedRec;

		}
	
}