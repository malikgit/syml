package controllers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class MaximumPurchase {
	
	private String debtReduction;
	private String largestDownpaymentHouse;
	private String maximumMortgageNoCondo;
	private String insureAmountMaxMortgage;
	private String variableMortHouseNoDebtRepayValue;
	private String fixedMortHouseNoDebtRepayValue;
	private String key;
	
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getDebtReduction() {
		return debtReduction;
	}
	public void setDebtReduction(String debtReduction) {
		this.debtReduction = debtReduction;
	}
	public String getLargestDownpaymentHouse() {
		return largestDownpaymentHouse;
	}
	public void setLargestDownpaymentHouse(String largestDownpaymentHouse) {
		this.largestDownpaymentHouse = largestDownpaymentHouse;
	}
	public String getMaximumMortgageNoCondo() {
		return maximumMortgageNoCondo;
	}
	public void setMaximumMortgageNoCondo(String maximumMortgageNoCondo) {
		this.maximumMortgageNoCondo = maximumMortgageNoCondo;
	}
	public String getInsureAmountMaxMortgage() {
		return insureAmountMaxMortgage;
	}
	public void setInsureAmountMaxMortgage(String insureAmountMaxMortgage) {
		this.insureAmountMaxMortgage = insureAmountMaxMortgage;
	}
	public String getVariableMortHouseNoDebtRepayValue() {
		return variableMortHouseNoDebtRepayValue;
	}
	public void setVariableMortHouseNoDebtRepayValue(
			String variableMortHouseNoDebtRepayValue) {
		this.variableMortHouseNoDebtRepayValue = variableMortHouseNoDebtRepayValue;
	}
	public String getFixedMortHouseNoDebtRepayValue() {
		return fixedMortHouseNoDebtRepayValue;
	}
	public void setFixedMortHouseNoDebtRepayValue(
			String fixedMortHouseNoDebtRepayValue) {
		this.fixedMortHouseNoDebtRepayValue = fixedMortHouseNoDebtRepayValue;
	}
	
	
}
