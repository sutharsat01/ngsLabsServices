package com.ocr.computervision.serviceImpl;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.azure.core.credential.AzureKeyCredential;

import com.ocr.computervision.model.Credential;
import com.ocr.computervision.repository.ICredentialRepository;
import com.ocr.computervision.repository.IHealthRepository;
import com.ocr.computervision.repository.IOCRRepository;
import com.ocr.computervision.repository.IPIIRepository;
import com.ocr.computervision.repository.ISearchRepository;
import com.ocr.computervision.service.ComputerVisionService;
@Service
public class ComputerVisionServiceImpl implements ComputerVisionService {
	
	@Autowired
	private  IOCRRepository OCRRepository;
	@Autowired
	private  IHealthRepository HealthRepository;
	@Autowired
    private  IPIIRepository PIIRepository;
	@Autowired
    private  ISearchRepository SearchRepository;
	@Autowired
	private  ICredentialRepository credentialRepository;
	//@Autowired
   // private  AzureKeyCredential healthApiCredential;
	//@Autowired
    //private AzureKeyCredential piiApiCredential;
	//@Autowired
   // private  URI healthApiEndpointURI;
	//@Autowired
   // private  URI piiApiEndpointURI;
	//@Autowired
   // private String healthApiEndpointURI2;
	//@Autowired
   // private String piiapisubscriptionKey;
	//@Autowired
   // private String piiApiEndpoint;
	@Override
	public Credential getCredential(String Key) {
		Optional<Credential> credentialOptional= credentialRepository.findByCredentialType(Key);
		return credentialOptional.isPresent()?credentialOptional.get():null;//ternary
	}
}
