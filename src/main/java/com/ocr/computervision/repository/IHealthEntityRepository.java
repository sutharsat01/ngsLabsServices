package com.ocr.computervision.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.ocr.computervision.model.Claims;
import com.ocr.computervision.model.HealthEntityResult;
import com.ocr.computervision.model.HealthEntity;

@Repository
public interface IHealthEntityRepository extends MongoRepository<HealthEntityResult, String>{

	//Optional<HealthEntityResult> findHealthEntityById(String id);

}
