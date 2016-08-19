package controllers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CombinedRecommendation {
	
	
	private String  underwriteCombined;
	private String  baseLender;
	private String  baseProduct;
	
	private String  baseMortgageType;
	private String  baseTerm;
	private String  baseAmortization;
	
	private String  baseInterestRate;
	private String  baseMortgageAmount;
	private String  basePayment;
	
	private String  baseCashbackPercent;
	private String  basePosition;
	private String  baseProductID;
	
	private String  additionalLender;
	private String  additionalProduct;
	private String  additionalMortgageType;
	
	private String  additionalTerm;
	private String  additionalAmortization;
	private String  additionalInterestRate;
	
	private String  additionalMortgageAmount;
	private String  additionalPayment;
	private String  additionalCashbackPercent;
	
	private String  additionalPosition;
	private String  additionalProductID;
	private String  totalMortgageAmount;
	
	private String  totalPayment;

	public String getUnderwriteCombined() {
		return underwriteCombined;
	}

	public void setUnderwriteCombined(String underwriteCombined) {
		this.underwriteCombined = underwriteCombined;
	}

	public String getBaseLender() {
		return baseLender;
	}

	public void setBaseLender(String baseLender) {
		this.baseLender = baseLender;
	}

	public String getBaseProduct() {
		return baseProduct;
	}

	public void setBaseProduct(String baseProduct) {
		this.baseProduct = baseProduct;
	}

	public String getBaseMortgageType() {
		return baseMortgageType;
	}

	public void setBaseMortgageType(String baseMortgageType) {
		this.baseMortgageType = baseMortgageType;
	}

	public String getBaseTerm() {
		return baseTerm;
	}

	public void setBaseTerm(String baseTerm) {
		this.baseTerm = baseTerm;
	}

	public String getBaseAmortization() {
		return baseAmortization;
	}

	public void setBaseAmortization(String baseAmortization) {
		this.baseAmortization = baseAmortization;
	}

	public String getBaseInterestRate() {
		return baseInterestRate;
	}

	public void setBaseInterestRate(String baseInterestRate) {
		this.baseInterestRate = baseInterestRate;
	}

	public String getBaseMortgageAmount() {
		return baseMortgageAmount;
	}

	public void setBaseMortgageAmount(String baseMortgageAmount) {
		this.baseMortgageAmount = baseMortgageAmount;
	}

	public String getBasePayment() {
		return basePayment;
	}

	public void setBasePayment(String basePayment) {
		this.basePayment = basePayment;
	}

	public String getBaseCashbackPercent() {
		return baseCashbackPercent;
	}

	public void setBaseCashbackPercent(String baseCashbackPercent) {
		this.baseCashbackPercent = baseCashbackPercent;
	}

	public String getBasePosition() {
		return basePosition;
	}

	public void setBasePosition(String basePosition) {
		this.basePosition = basePosition;
	}

	public String getBaseProductID() {
		return baseProductID;
	}

	public void setBaseProductID(String baseProductID) {
		this.baseProductID = baseProductID;
	}

	public String getAdditionalLender() {
		return additionalLender;
	}

	public void setAdditionalLender(String additionalLender) {
		this.additionalLender = additionalLender;
	}

	public String getAdditionalProduct() {
		return additionalProduct;
	}

	public void setAdditionalProduct(String additionalProduct) {
		this.additionalProduct = additionalProduct;
	}

	public String getAdditionalMortgageType() {
		return additionalMortgageType;
	}

	public void setAdditionalMortgageType(String additionalMortgageType) {
		this.additionalMortgageType = additionalMortgageType;
	}

	public String getAdditionalTerm() {
		return additionalTerm;
	}

	public void setAdditionalTerm(String additionalTerm) {
		this.additionalTerm = additionalTerm;
	}

	public String getAdditionalAmortization() {
		return additionalAmortization;
	}

	public void setAdditionalAmortization(String additionalAmortization) {
		this.additionalAmortization = additionalAmortization;
	}

	public String getAdditionalInterestRate() {
		return additionalInterestRate;
	}

	public void setAdditionalInterestRate(String additionalInterestRate) {
		this.additionalInterestRate = additionalInterestRate;
	}

	public String getAdditionalMortgageAmount() {
		return additionalMortgageAmount;
	}

	public void setAdditionalMortgageAmount(String additionalMortgageAmount) {
		this.additionalMortgageAmount = additionalMortgageAmount;
	}

	public String getAdditionalPayment() {
		return additionalPayment;
	}

	public void setAdditionalPayment(String additionalPayment) {
		this.additionalPayment = additionalPayment;
	}

	public String getAdditionalCashbackPercent() {
		return additionalCashbackPercent;
	}

	public void setAdditionalCashbackPercent(String additionalCashbackPercent) {
		this.additionalCashbackPercent = additionalCashbackPercent;
	}

	public String getAdditionalPosition() {
		return additionalPosition;
	}

	public void setAdditionalPosition(String additionalPosition) {
		this.additionalPosition = additionalPosition;
	}

	public String getAdditionalProductID() {
		return additionalProductID;
	}

	public void setAdditionalProductID(String additionalProductID) {
		this.additionalProductID = additionalProductID;
	}

	public String getTotalMortgageAmount() {
		return totalMortgageAmount;
	}

	public void setTotalMortgageAmount(String totalMortgageAmount) {
		this.totalMortgageAmount = totalMortgageAmount;
	}

	public String getTotalPayment() {
		return totalPayment;
	}

	public void setTotalPayment(String totalPayment) {
		this.totalPayment = totalPayment;
	}
	
	
	

}
