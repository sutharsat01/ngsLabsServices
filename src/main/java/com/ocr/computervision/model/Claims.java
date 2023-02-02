package com.ocr.computervision.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@Document("Claims" )
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
public class Claims {
	@Id
	
	public String id;

	public String status;

	public String createdDateTime;

	public String lastUpdatedDateTime;
	public AnalyzeResult analyzeResult;

	public byte[] claimimage;

	public AnalyzeResult getAnalyzeResult() {
		return analyzeResult;
	}

	public void setAnalyzeResult(AnalyzeResult analyzeResult) {
		this.analyzeResult = analyzeResult;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreatedDateTime() {
		return createdDateTime;
	}

	public void setCreatedDateTime(String createdDateTime) {
		this.createdDateTime = createdDateTime;
	}

	public String getLastUpdatedDateTime() {
		return lastUpdatedDateTime;
	}

	public void setLastUpdatedDateTime(String lastUpdatedDateTime) {
		this.lastUpdatedDateTime = lastUpdatedDateTime;
	}

	public byte[] getClaimimage() {
		return claimimage;
	}

	public void setClaimimage(byte[] claimimage) {
		this.claimimage = claimimage;
	}
}
