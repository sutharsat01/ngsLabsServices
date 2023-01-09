package com.ocr.computervision.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ocr.computervision.model.Claims;

public interface IOCRRepository extends MongoRepository<Claims, Long> {

}
