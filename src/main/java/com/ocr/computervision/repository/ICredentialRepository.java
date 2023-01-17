package com.ocr.computervision.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ocr.computervision.model.Claims;
import com.ocr.computervision.model.Credential;

public interface ICredentialRepository extends MongoRepository<Credential, Long>{
    Optional<Credential> findByCredentialType(String Key);
}
