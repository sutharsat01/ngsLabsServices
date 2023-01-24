package com.ocr.computervision.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;

import com.ocr.computervision.model.HealthEntity;
import com.ocr.computervision.repository.IHealthEntityRepository;
import com.ocr.computervision.service.HealthEntityService;

public class HealthEntitySearviceImpl implements HealthEntityService{
	@Autowired
    private  IHealthEntityRepository HealthEntityRepository;
	@Override
	public String saveHealthEntity(HealthEntity healthEntity) {
		HealthEntity entry = HealthEntityRepository.save(healthEntity); 
		return entry.getId();
	}

}
