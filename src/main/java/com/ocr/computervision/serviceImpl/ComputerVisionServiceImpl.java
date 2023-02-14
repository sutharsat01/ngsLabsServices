package com.ocr.computervision.serviceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.ocr.computervision.model.Claims;
import com.ocr.computervision.model.Credential;
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
	public HealthEntityResult saveHealthEntityResult(HealthEntityResult healthEntityResult) {
		HealthEntityResult saveEntityResult = healthEntityRepository.save(healthEntityResult);
		return saveEntityResult;
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
	@Override
	public Search createSearch(Search search) {
		Search searchEntity = searchRepository.save(search);
		return searchEntity;
	}

	@Override
	public Search updateSearch(Search search, String id) {
		Search  updateSearchEntity = searchRepository.findById(id).orElseThrow(()-> 
		new ResourceNotFoundException("Claim doesn't exist with the given Id " + id));
		updateSearchEntity.setPerson(search.getPerson());
		updateSearchEntity.setEmail(search.getEmail());
		updateSearchEntity.setAddress(search.getAddress());
		updateSearchEntity.setDateTime(search.getDateTime());
	updateSearchEntity.setOrganization(search.getOrganization());
	updateSearchEntity.setPhoneNumber(search.getPhoneNumber());
		return searchRepository.save(updateSearchEntity) ;
	}
	
	
	
		
	
	
}
