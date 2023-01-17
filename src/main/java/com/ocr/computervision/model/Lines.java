package com.ocr.computervision.model;

import java.util.List;

public class Lines {
	 public List<Integer> boundingBox;
     public List<Words> words;
     public String text;
	public List<Integer> getBoundingBox() {
		return boundingBox;
	}
	public void setBoundingBox(List<Integer> boundingBox) {
		this.boundingBox = boundingBox;
	}
	public List<Words> getWords() {
		return words;
	}
	public void setWords(List<Words> words) {
		this.words = words;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
}
