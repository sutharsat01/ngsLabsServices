package com.ocr.computervision.model;

import java.util.List;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Document(collection = "PIIResult")
//@EntityScan
public class PIIEntity {
	@Id
	 public String id;
	public String Text;

    public String Category;

  
    public double ConfidenceScore;
          
    public List<Integer> BoundingBox;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return Text;
	}

	public void setText(String text) {
		Text = text;
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

	public List<Integer> getBoundingBox() {
		return BoundingBox;
	}

	public void setBoundingBox(List<Integer> boundingBox) {
		BoundingBox = boundingBox;
	}
    
}
