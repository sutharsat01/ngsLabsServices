package com.ocr.computervision.serviceImpl;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.azure.core.credential.AzureKeyCredential;
import com.ocr.computervision.model.Claims;
import com.ocr.computervision.model.Credential;
import com.ocr.computervision.model.HealthEntity;
import com.ocr.computervision.model.HealthEntityResult;
import com.ocr.computervision.model.PIIEntityResult;
import com.ocr.computervision.model.Search;
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
	private  IHealthEntityRepository healthEntityRepository;
	@Autowired
    private  IPIIEntityRepository piiEntityRepository;
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
	@Override
	public String saveHealthEntityResult(HealthEntityResult healthEntityResult) {
		HealthEntityResult saveEntityResult = healthEntityRepository.save(healthEntityResult);
		return saveEntityResult.getId();
	}
	@Override
	public PIIEntityResult savePIIEntityResult(PIIEntityResult piiEntityResult) {
		PIIEntityResult savePIIResult = piiEntityRepository.save(piiEntityResult);
		return savePIIResult;
	}
	@Override
	public PIIEntityResult findById(String id) {
		Optional<PIIEntityResult> piiEntityResultOptional= piiEntityRepository.findById(id);
		return piiEntityResultOptional.isPresent()?piiEntityResultOptional.get():null;
	}
		@Override
		public HealthEntityResult findHealthEntityById(String id) {
			Optional<HealthEntityResult> healthEntityResultOptional = healthEntityRepository.findById(id);
			return healthEntityResultOptional.isPresent()?healthEntityResultOptional.get():null;
		}
	
	@Override
	public Search searchDocumentById(String id) {
		Optional<Search> search = searchRepository.findById(id);
		return search.isPresent()?search.get():null;
	}
	
	
		
	
	
}
