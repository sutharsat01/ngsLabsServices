package com.ocr.computervision.service;

import org.springframework.stereotype.Service;

import com.ocr.computervision.model.Claims;
import com.ocr.computervision.model.Credential;
import com.ocr.computervision.model.HealthEntity;
import com.ocr.computervision.model.HealthEntityResult;
import com.ocr.computervision.model.PIIEntityResult;


public interface ComputerVisionService {
  public Credential getCredential(String Key);
  
  public String SaveClaim(Claims claims); 
  public String saveHealthEntityResult(HealthEntityResult healthEntityResult);
  public String savePIIEntityResult(PIIEntityResult piiEntityResult);
	  
  PIIEntityResult findById(String id);
}
