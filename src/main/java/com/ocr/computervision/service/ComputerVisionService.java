package com.ocr.computervision.service;

import org.springframework.stereotype.Service;

import com.ocr.computervision.model.Credential;


public interface ComputerVisionService {
  public Credential getCredential(String Key);
}
