package com.ocr.computervision.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "EntityResult")
public class HealthEntityResult {
	@Id
	public String id ;
    public String correlatingId;
    public List<HealthEntity> entities;
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
	public List<HealthEntity> getEntities() {
		return entities;
	}
	public void setEntities(List<HealthEntity> entities) {
		this.entities = entities;
	}
	//@Override
	//public String toString() {
	//	return "HealthEntityResult [id=" + id + ", correlatingId=" + correlatingId + ", entities=" + entities + "]";
	//}
    
}
