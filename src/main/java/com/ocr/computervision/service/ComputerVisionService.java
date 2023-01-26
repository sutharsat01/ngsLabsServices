package com.ocr.computervision.service;

import com.ocr.computervision.model.Claims;
import com.ocr.computervision.model.Credential;
import com.ocr.computervision.model.HealthEntity;
import com.ocr.computervision.model.HealthEntityResult;
import com.ocr.computervision.model.PIIEntityResult;
import com.ocr.computervision.model.Search;


public interface ComputerVisionService {
  public Credential getCredential(String Key);
  
  public String SaveClaim(Claims claims); 
  public String saveHealthEntityResult(HealthEntityResult healthEntityResult);
  public String savePIIEntityResult(PIIEntityResult piiEntityResult);
  public Search searchDocumentById(String id); 
  public PIIEntityResult findById(String id);
}
