package controllers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplicantsID {
	private  ApplicantsNames applicantName;
	private  Integer applicantID;
	
	public ApplicantsNames getApplicantName() {
		return applicantName;
	}
	public void setApplicantName(ApplicantsNames applicantName2) {
		this.applicantName = applicantName2;
	}
	public Integer getApplicantID() {
		return applicantID;
	}
	public void setApplicantID(Integer applicantID) {
		this.applicantID = applicantID;
	}

}
