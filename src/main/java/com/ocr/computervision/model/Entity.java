package com.ocr.computervision.model;

import java.util.List;

public class Entity {
	public String Text;

    public String Category;

   
    public double ConfidenceScore;
   
    public List<Integer> BoundingBox;

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
