package com.ocr.computervision.exceptions;

import java.net.MalformedURLException;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice

public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(org.springframework.data.rest.webmvc.ResourceNotFoundException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public ResponseEntity<ApiError> handleResourceNotFoundException(ResourceNotFoundException ex) {
		ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex);
		return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);

	}
	@ExceptionHandler(MalformedURLException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ResponseEntity<ApiError> handleMalformedURLException(MalformedURLException ex) {
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex);
		String message ="Please use valid URL" ;
		apiError.setMessage(message);
		return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);

	}
	@ExceptionHandler(Exception.class)
	protected ResponseEntity<Object> handleInternalServerError(Exception ex, WebRequest request) {
	    ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR);
	    apiError.setMessage("Internal server error occurred");
	    apiError.setDebugMessage(ex.getMessage());
	    return buildResponseEntity(apiError);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
		apiError.setMessage(String.format("The parameter '%s' of value '%s' could not be converted to type '%s'",
				ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName()));
		apiError.setDebugMessage(ex.getMessage());
		return buildResponseEntity(apiError);
	}

	private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
		return new ResponseEntity<>(apiError, apiError.getStatus());
	}


}
