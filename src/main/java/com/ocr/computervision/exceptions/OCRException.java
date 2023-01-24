package com.ocr.computervision.exceptions;

public class OCRException extends Exception {
	private String errorCodeString = "OCRException";

	public OCRException(String exc, String errorCode) {
		super(exc);
		this.errorCodeString = errorCode;
	}

	public OCRException(String exc) {
		super(exc);
	}

	public String getMessage() {
		return super.getMessage();
	}
}