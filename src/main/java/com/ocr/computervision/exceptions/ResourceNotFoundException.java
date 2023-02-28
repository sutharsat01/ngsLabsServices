package com.ocr.computervision.exceptions;

import org.springframework.data.mongodb.core.messaging.Message;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


public class ResourceNotFoundException extends RuntimeException{

	private String resourceName;
	private String fieldName;
	private String fieldValue;
	public ResourceNotFoundException(String resourceName, String fieldName, String fieldValue) {
		super(String.format(fieldName, null));
		this.resourceName = resourceName;
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getFieldValue() {
		return fieldValue;
	}
	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}
	 private static final long serialVersionUID = 1L;

	    public ResourceNotFoundException(String message) {
	        super(message);
	    }

	    public ResourceNotFoundException(String message, Throwable cause) {
	        super(message, cause);
	    }
	
}
