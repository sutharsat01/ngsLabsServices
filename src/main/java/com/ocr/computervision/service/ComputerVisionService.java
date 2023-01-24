package com.ocr.computervision.service;

import org.springframework.stereotype.Service;

import com.ocr.computervision.model.Claims;
import com.ocr.computervision.model.Credential;


public interface ComputerVisionService {
  public Credential getCredential(String Key);
  
  public String SaveClaim(Claims claims); 
	  
  
}
