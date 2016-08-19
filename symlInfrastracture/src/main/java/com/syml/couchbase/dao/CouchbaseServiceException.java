package com.syml.couchbase.dao;

public class CouchbaseServiceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6011398114146739040L;

	public CouchbaseServiceException() {
		super();
		
	}

	public CouchbaseServiceException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public CouchbaseServiceException(String message, Throwable cause) {
		super(message, cause);
		
	}

	public CouchbaseServiceException(String message) {
		super(message);
		
	}

	public CouchbaseServiceException(Throwable cause) {
		super(cause);
		
	}

}
