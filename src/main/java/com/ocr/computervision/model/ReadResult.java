package com.ocr.computervision.model;

import java.util.List;

public class ReadResult {
	public int page;
    public int angle;
    public int width;
    public int height;
    public String unit;
    public String language;
    public List<Lines> lines;
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getAngle() {
		return angle;
	}
	public void setAngle(int angle) {
		this.angle = angle;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public List<Lines> getLines() {
		return lines;
	}
	public void setLines(List<Lines> lines) {
		this.lines = lines;
	}
}
