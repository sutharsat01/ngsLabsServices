package com.ocr.computervision.model;

import java.util.List;

import org.springframework.data.annotation.Id;

public class EntityResult {
	@Id
	public String id ;
    public String correlatingId;
    public List<Entity> entities;
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
	public List<Entity> getEntities() {
		return entities;
	}
	public void setEntities(List<Entity> entities) {
		this.entities = entities;
	}
    
}