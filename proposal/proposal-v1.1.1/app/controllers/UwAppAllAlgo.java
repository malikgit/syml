package controllers;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
public class UwAppAllAlgo {
	private String 	algo;

	private String opportunityName;

	private int opportunityID;
	private String address;
	
	private  String city;
	private String province;
	private  String  expirayDate;
	
	public String getExpirayDate() {
		return expirayDate;
	}
	public void setExpirayDate(String expirayDate) {
		this.expirayDate = expirayDate;
	}
	private  Set<ApplicantsID> applicantsID;
	private  Set<ApplicantsNames> applicantsNames;
	private  Set<MarketingNotes> marketingNotes;
	private  Set<Recommendation> recommendations;
	private Set<Liability> liability;
	
	
	public Set<Liability> getLiability() {
		return liability;
	}
	public void setLiability(Set<Liability> liability) {
		this.liability = liability;
	}
	private  Set<CombinedRecommendation> combinedRecommendation;
	
	public Set<CombinedRecommendation> getCombinedRecommendation() {
		return combinedRecommendation;
	}
	public void setCombinedRecommendation(
			Set<CombinedRecommendation> combinedRecommendation) {
		this.combinedRecommendation = combinedRecommendation;
	}
	private  Set<DesiredProduct> desiredProduct;
	private  Set<RecommendedProduct> recommendedProduct;
	private  Set<MaximumPurchase> MaxMortgageTable;
	
	public Set<MaximumPurchase> getMaxMortgageTable() {
		return MaxMortgageTable;
	}
	public void setMaxMortgageTable(Set<MaximumPurchase> maxMortgageTable) {
		MaxMortgageTable = maxMortgageTable;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getAlgo() {
		return algo;
	}
	public void setAlgo(String algo) {
		this.algo = algo;
	}
	public String getOpportunityName() {
		return opportunityName;
	}
	public void setOpportunityName(String opportunityName) {
		this.opportunityName = opportunityName;
	}
/*	public String getOpportunityID() {
		return opportunityID;
	}
	public void setOpportunityID(String opportunityID) {
		this.opportunityID = opportunityID;
	}*/
	
	
	public Set<ApplicantsID> getApplicantsID() {
		return applicantsID;
	}
	public int getOpportunityID() {
		return opportunityID;
	}
	public void setOpportunityID(int opportunityID) {
		this.opportunityID = opportunityID;
	}
	public void setApplicantsID(Set<ApplicantsID> applicantsID) {
		this.applicantsID = applicantsID;
	}
	public Set<ApplicantsNames> getApplicantsNames() {
		return applicantsNames;
	}
	public void setApplicantsNames(Set<ApplicantsNames> applicantsNames) {
		this.applicantsNames = applicantsNames;
	}
	public Set<MarketingNotes> getMarketingNotes() {
		return marketingNotes;
	}
	public void setMarketingNotes(Set<MarketingNotes> marketingNotes) {
		this.marketingNotes = marketingNotes;
	}
	public Set<Recommendation> getRecommendations() {
		return recommendations;
	}
	public void setRecommendations(Set<Recommendation> recommendations) {
		this.recommendations = recommendations;
	}
	public Set<DesiredProduct> getDesiredProduct() {
		return desiredProduct;
	}
	public void setDesiredProduct(Set<DesiredProduct> desiredProduct) {
		this.desiredProduct = desiredProduct;
	}
	public Set<RecommendedProduct> getRecommendedProduct() {
		return recommendedProduct;
	}
	public void setRecommendedProduct(Set<RecommendedProduct> recommendedProduct) {
		this.recommendedProduct = recommendedProduct;
	}
	
	
}
