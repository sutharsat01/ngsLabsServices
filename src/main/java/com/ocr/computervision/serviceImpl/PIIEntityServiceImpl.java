package com.ocr.computervision.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;

import com.ocr.computervision.model.PIIEntity;
import com.ocr.computervision.repository.IPIIEntityRepository;
import com.ocr.computervision.service.PIIEntityService;

public class PIIEntityServiceImpl implements PIIEntityService {
	@Autowired
    private  IPIIEntityRepository PIIEntityRepository;
	@Override
	public String SavePIIEntity(PIIEntity piiEntity) {
		PIIEntity entry = PIIEntityRepository.save(piiEntity);
		return entry.getId();
	}
}