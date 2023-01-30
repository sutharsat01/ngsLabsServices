package com.ocr.computervision.model;

import java.util.List;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Document(collection = "PIIResult")
//@EntityScan
public class PIIEntity {
	//@Id
	// public String id;
	public String text;

    public String category;

  
    public double confidenceScore;


	public String getText() {
		return text;
	}


	public void setText(String text) {
		this.text = text;
	}


	public String getCategory() {
		return category;
	}


	public void setCategory(String category) {
		this.category = category;
	}


	public double getConfidenceScore() {
		return confidenceScore;
	}


	public void setConfidenceScore(double confidenceScore) {
		this.confidenceScore = confidenceScore;
	}
          
    
  //  public List<Integer> BoundingBox;

	//public String getId() {
	//	return id;
//	}

	//public void setId(String id) {
	//	this.id = id;
	//}

	

	/*
	 * public List<Integer> getBoundingBox() { return BoundingBox; }
	 * 
	 * public void setBoundingBox(List<Integer> boundingBox) { BoundingBox =
	 * boundingBox; }
	 */
    
}
