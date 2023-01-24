package com.ocr.computervision.model;

import java.util.List;

import org.springframework.data.annotation.Id;

public class PIIResult {
	@Id
	public String id;
    public String correlatingId;
    public List<PIIEntity> PIIEntities;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCorrelatingId() {
		return correlatingId;
	}
	public void setCorrelatingId(String correlatingId) {
		this.correlatingId = correlatingId;
	}
	public List<PIIEntity> getPIIEntities() {
		return PIIEntities;
	}
	public void setPIIEntities(List<PIIEntity> pIIEntities) {
		PIIEntities = pIIEntities;
	}
    
}
