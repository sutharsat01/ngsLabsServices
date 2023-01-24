package com.ocr.computervision.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.ocr.computervision.model.Claims;
import com.ocr.computervision.model.PIIEntity;
import com.ocr.computervision.model.PIIResult;

@Repository
public interface IPIIEntityRepository extends MongoRepository<PIIEntity, Long>{

}