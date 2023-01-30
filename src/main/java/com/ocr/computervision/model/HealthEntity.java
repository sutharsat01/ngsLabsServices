package com.ocr.computervision.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;


//@Document(collection = "EntityResult")
@JsonAutoDetect(getterVisibility=Visibility.NONE)
public class HealthEntity {
	
	//@Id
	// public String id;
	public String EntityName;

    public String Category;

   
    public double ConfidenceScore;


	public String getEntityName() {
		return EntityName;
	}


	public void setEntityName(String entityName) {
		EntityName = entityName;
	}


	public String getCategory() {
		return Category;
	}


	public void setCategory(String category) {
		Category = category;
	}


	public double getConfidenceScore() {
		return ConfidenceScore;
	}


	public void setConfidenceScore(double confidenceScore) {
		ConfidenceScore = confidenceScore;
	}


	//public String getId() {
	//	return id;
	//}


	//public void setId(String id) {
	//	this.id = id;
	//}


	
   // public List<Integer> BoundingBox;

	
    
}
