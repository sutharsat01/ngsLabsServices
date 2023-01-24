package com.ocr.computervision.serviceImpl;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.azure.core.credential.AzureKeyCredential;
import com.ocr.computervision.model.Claims;
import com.ocr.computervision.model.Credential;
import com.ocr.computervision.repository.ICredentialRepository;
import com.ocr.computervision.repository.IHealthEntityRepository;
import com.ocr.computervision.repository.IOCRRepository;
import com.ocr.computervision.repository.IPIIEntityRepository;
import com.ocr.computervision.repository.ISearchRepository;
import com.ocr.computervision.service.ComputerVisionService;
@Service
public class ComputerVisionServiceImpl implements ComputerVisionService {
	
	@Autowired
	private  IOCRRepository ocrRepository;
	@Autowired
	private  IHealthEntityRepository healthRepository;
	@Autowired
    private  IPIIEntityRepository piiRepository;
	@Autowired
    private  ISearchRepository searchRepository;
	@Autowired
	private  ICredentialRepository credentialRepository;
	
	@Override
	public Credential getCredential(String Key) {
		Optional<Credential> credentialOptional= credentialRepository.findByCredentialType(Key);
		return credentialOptional.isPresent()?credentialOptional.get():null;//ternary
	}
	@Override
	public String SaveClaim(Claims claims) {
		Claims entry = ocrRepository.save(claims);
		return entry.getId();
	}
}
