package com.ocr.computervision.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.ocr.computervision.model.Claims;
import com.ocr.computervision.model.Search;

@Repository
public interface ISearchRepository extends MongoRepository<Search, Long>{

}
