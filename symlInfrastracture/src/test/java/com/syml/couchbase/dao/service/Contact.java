package com.syml.couchbase.dao.service;


public class Contact {


	private int id;
	
	

	private String name;
	
	private String last_name;
	
	private String email;
	
	private boolean notification_email_send;

	public boolean isNotification_email_send() {
		return notification_email_send;
	}

	public void setNotification_email_send(boolean notification_email_send) {
		this.notification_email_send = notification_email_send;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "Contact [id=" + id + ", name=" + name + ", last_name="
				+ last_name + ", email=" + email + "]";
	}
	
	
	
	

}
