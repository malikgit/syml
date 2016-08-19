package controllers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

public class OrignalDetails {
private String propertyValue;
private String downpaymentEquity;
private String mortgageAmount;
private String insuranceAmount;
private String totalMortgage;
private String amortization;
private String mortgageType;
private String mortgageTerm;
private String paymentAmount;
private  String  interestRate;

public String getInterestRate() {
	return interestRate;
}
public void setInterestRate(String interestRate) {
	this.interestRate = interestRate;
}
private String lender;
private String productID;
public String getPropertyValue() {
	return propertyValue;
}
public void setPropertyValue(String propertyValue) {
	this.propertyValue = propertyValue;
}
public String getDownpaymentEquity() {
	return downpaymentEquity;
}
public void setDownpaymentEquity(String downpaymentEquity) {
	this.downpaymentEquity = downpaymentEquity;
}
public String getMortgageAmount() {
	return mortgageAmount;
}
public void setMortgageAmount(String mortgageAmount) {
	this.mortgageAmount = mortgageAmount;
}
public String getInsuranceAmount() {
	return insuranceAmount;
}
public void setInsuranceAmount(String insuranceAmount) {
	this.insuranceAmount = insuranceAmount;
}
public String getTotalMortgage() {
	return totalMortgage;
}
public void setTotalMortgage(String totalMortgage) {
	this.totalMortgage = totalMortgage;
}
public String getAmortization() {
	return amortization;
}
public void setAmortization(String amortization) {
	this.amortization = amortization;
}
public String getMortgageType() {
	return mortgageType;
}
public void setMortgageType(String mortgageType) {
	this.mortgageType = mortgageType;
}
public String getMortgageTerm() {
	return mortgageTerm;
}
public void setMortgageTerm(String mortgageTerm) {
	this.mortgageTerm = mortgageTerm;
}
public String getPaymentAmount() {
	return paymentAmount;
}
public void setPaymentAmount(String paymentAmount) {
	this.paymentAmount = paymentAmount;
}
public String getLender() {
	return lender;
}
public void setLender(String lender) {
	this.lender = lender;
}
public String getProductID() {
	return productID;
}
public void setProductID(String productID) {
	this.productID = productID;
}


}
