package com.ocr.computervision.model;

import java.util.List;

public class AnalyzeResult {
	public List<ReadResult> readResults;
    public String version ;
    public String modelVersion ;
	public List<ReadResult> getReadResults() {
		return readResults;
	}
	public void setReadResults(List<ReadResult> readResults) {
		this.readResults = readResults;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getModelVersion() {
		return modelVersion;
	}
	public void setModelVersion(String modelVersion) {
		this.modelVersion = modelVersion;
	}
}
