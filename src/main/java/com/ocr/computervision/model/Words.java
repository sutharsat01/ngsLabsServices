package com.ocr.computervision.model;

import java.util.List;

public class Words {
	public List<Integer> boundingBox;
    public String text;
    public double confidence;
	public List<Integer> getBoundingBox() {
		return boundingBox;
	}
	public void setBoundingBox(List<Integer> boundingBox) {
		this.boundingBox = boundingBox;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public double getConfidence() {
		return confidence;
	}
	public void setConfidence(double confidence) {
		this.confidence = confidence;
	}
}
