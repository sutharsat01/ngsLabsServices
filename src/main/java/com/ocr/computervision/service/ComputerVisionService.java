package com.ocr.computervision.service;

import com.ocr.computervision.model.Claims;
import com.ocr.computervision.model.Credential;
import com.ocr.computervision.model.HealthEntityResult;
import com.ocr.computervision.model.PIIEntityResult;
import com.ocr.computervision.model.Search;


public interface ComputerVisionService {
  public Credential getCredential(String Key);
  
  public String SaveClaim(Claims claims); 
  public HealthEntityResult saveHealthEntityResult(HealthEntityResult healthEntityResult);
  public PIIEntityResult savePIIEntityResult(PIIEntityResult piiEntityResult);
  public HealthEntityResult findHealthEntityById(String id);
  public Search searchDocumentById(String id); 
  public PIIEntityResult findById(String id);
  public Search createSearch(Search search);
  public Search updateSearch(Search search,String id );


}
