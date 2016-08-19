package controllers;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Recommendation {

	
private String create_date;	
public String getCreate_date() {
	return create_date;
}

public void setCreate_date(String create_date) {
	this.create_date = create_date;
}
private  String    date_Edited;
public String getDate_Edited() {
	return date_Edited;
	
}

public void setDate_Edited(String date_Edited) {
	this.date_Edited = date_Edited;
}
private  String  lender;
private  String  product;
private  String  mortgageType;
private  String  term;
private  String  maximumAmortization;
private  String  interestRate;
private  String  mortgageAmount;
private  String  payment;
private  String  cashbackPercent;
private  String  position;
private  String  productID;
private  String  initialforApproval;



public String getInitialforApproval() {
	return initialforApproval;
}
public void setInitialforApproval(String initialforApproval) {
	this.initialforApproval = initialforApproval;
}
public String getLender() {
	return lender;
}
public void setLender(String lender) {
	this.lender = lender;
}
public String getProduct() {
	return product;
}
public void setProduct(String product) {
	this.product = product;
}
public String getMortgageType() {
	return mortgageType;
}
public void setMortgageType(String mortgageType) {
	this.mortgageType = mortgageType;
}
public String getTerm() {
	return term;
}
public void setTerm(String term) {
	this.term = term;
}
public String getMaximumAmortization() {
	return maximumAmortization;
}
public void setMaximumAmortization(String maximumAmortization) {
	this.maximumAmortization = maximumAmortization;
}
public String getInterestRate() {
	return interestRate;
}
public void setInterestRate(String interestRate) {
	this.interestRate = interestRate;
}
public String getMortgageAmount() {
	return mortgageAmount;
}
public void setMortgageAmount(String mortgageAmount) {
	this.mortgageAmount = mortgageAmount;
}
public String getPayment() {
	return payment;
}
public void setPayment(String payment) {
	this.payment = payment;
}
public String getCashbackPercent() {
	return cashbackPercent;
}
public void setCashbackPercent(String cashbackPercent) {
	this.cashbackPercent = cashbackPercent;
}
public String getPosition() {
	return position;
}
public void setPosition(String position) {
	this.position = position;
}
public String getProductID() {
	return productID;
}
public void setProductID(String productID) {
	this.productID = productID;
}

/*@Override
public int compare(Recommendation o1, Recommendation o2) {
	return	o1.getTerm().compareTo(o2.getTerm());
	
	
}*/



}
