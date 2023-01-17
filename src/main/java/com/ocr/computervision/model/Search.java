package com.ocr.computervision.model;

import org.springframework.data.annotation.Id;

public class Search {
	@Id
	public String id;

    
    public byte[] searchImageValue;

  
    public String person;

    public String dateTime; 
    public String phoneNumber;

    public String email;
    public String organization;
    public String address ;
    public String claimId ;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public byte[] getSearchImageValue() {
		return searchImageValue;
	}
	public void setSearchImageValue(byte[] searchImageValue) {
		this.searchImageValue = searchImageValue;
	}
	public String getPerson() {
		return person;
	}
	public void setPerson(String person) {
		this.person = person;
	}
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getOrganization() {
		return organization;
	}
	public void setOrganization(String organization) {
		this.organization = organization;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getClaimId() {
		return claimId;
	}
	public void setClaimId(String claimId) {
		this.claimId = claimId;
	}
    
}
