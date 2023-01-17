package com.ocr.computervision.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.ocr.computervision.model.Claims;
@Repository
public interface IOCRRepository extends MongoRepository<Claims, Long> {

}
