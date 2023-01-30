package com.ocr.computervision.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.azure.ai.textanalytics.TextAnalyticsAsyncClient;
import com.azure.ai.textanalytics.TextAnalyticsClient;
import com.azure.ai.textanalytics.TextAnalyticsClientBuilder;
import com.azure.ai.textanalytics.models.PiiEntity;
import com.azure.ai.textanalytics.models.PiiEntityCollection;
import com.azure.ai.textanalytics.models.TextDocumentInput;
import com.azure.core.credential.AzureKeyCredential;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.ocr.computervision.model.Claims;
import com.ocr.computervision.model.HealthEntityResult;
import com.ocr.computervision.model.Lines;
import com.ocr.computervision.model.PIIEntity;
import com.ocr.computervision.model.PIIEntityResult;
import com.ocr.computervision.model.ReadResult;
import com.ocr.computervision.model.Search;
import com.ocr.computervision.ocrconstants.NgsServicesConstants;
import com.ocr.computervision.service.ComputerVisionService;
import com.ocr.computervision.utility.NgsServicesUtils;

@RestController
public class OCRController extends NgsServicesUtils {
	static String ocrapisubscriptionKey;
	static String ocrAPIEndpoint;
	static String ocrAPIURI;
	static String healthApiURI;
	static String healthApiEndpoint;
	static String subscrriptionKey;
	static String healthapisubscriptionKey;
	static String endpoint;
	static String credentialType;

	private String piiapisubscriptionKey;
	static String piiApiEndpoint;

	private static AzureKeyCredential healthApiCredential;

	@Autowired
	private ComputerVisionService service;

	// @Autowired
	// private NgsServicesUtils ngsServicesUtil;

	@Autowired
	private static NgsServicesConstants ngsServicesConsts;

	@PostMapping(value = "/extractText", consumes = "multipart/*", produces = "application/json;charset=utf-8")
	public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file)
			throws IOException, URISyntaxException {

		ocrapisubscriptionKey = service.getCredential("OCRAPI").subscriptionKey.toString();
		ocrAPIEndpoint = service.getCredential("OCRAPI").endpoint.toString();

		ocrAPIURI = ocrAPIEndpoint + ngsServicesConsts.OCR_API_URI;

		String jsonString = "";
		String ocrText = "";

		byte[] bytes = null;

		if (!file.isEmpty()) {
			bytes = file.getBytes();
			jsonString = NgsServicesUtils.invokeHttpClient(bytes, ocrAPIURI, ocrapisubscriptionKey);

			Claims claimResult = new Claims();
			ObjectMapper objectMapper = new ObjectMapper();
			claimResult = objectMapper.readValue(jsonString, Claims.class);

			claimResult.claimimage = bytes;

			for (ReadResult rr : claimResult.getAnalyzeResult().getReadResults()) {
				for (Lines lines : rr.getLines()) {
					ocrText = ocrText + " " + lines.getText();
				}
			}
			service.SaveClaim(claimResult);

		}
		return ResponseEntity.ok(jsonString);

	}

	@PostMapping(value = "/classifyPiiEntity")
	public ResponseEntity<String> getPIIEntityResult(@RequestBody String extractedText) {
		// PII(Personal Info) Detection API
		piiapisubscriptionKey = service.getCredential("PIIAPI").subscriptionKey.toString();

		piiApiEndpoint = service.getCredential("PIIAPI").endpoint.toString();

		TextAnalyticsClient piiAPIClient = NgsServicesUtils.authenticatepiiClient(piiapisubscriptionKey,
				piiApiEndpoint);

		String response = NgsServicesUtils.ExtractSavePIIRelatedInfo(piiAPIClient, extractedText, service);

		return ResponseEntity.ok(response);
	}

	@PostMapping(value = "/classifyHealthEntity")
	public ResponseEntity<String> getHealthEntityResult(@RequestBody String extractedText) {

		// Health Related Info Analytic API
		healthapisubscriptionKey = service.getCredential("ANALYTICAPI").subscriptionKey.toString();
		healthApiCredential = new AzureKeyCredential(healthapisubscriptionKey);
		healthApiEndpoint = service.getCredential("ANALYTICAPI").endpoint.toString();
		// healthApiEndpointURI = new Uri(healthApiEndpoint);

		healthApiURI = healthApiEndpoint + "/language/analyze-text/jobs";

		TextAnalyticsAsyncClient healthAPIClient = NgsServicesUtils.authenticateClient(healthapisubscriptionKey,
				healthApiEndpoint);

		String response = NgsServicesUtils.ExtractSaveHealthRelatedInfo(healthAPIClient, extractedText);
		return ResponseEntity.ok(response);
	}

	@GetMapping(value = "/piiEntity/{id}")
	public ResponseEntity<PIIEntityResult> getPIIEntityById(@PathVariable String id) {
		return ResponseEntity.ok().body(service.findById(id));
	}

	@GetMapping(value = "/healthEntity/{id}")
	public ResponseEntity<HealthEntityResult> getHealthEntityById(@PathVariable String id) {
		HealthEntityResult healthEntityResult = new HealthEntityResult();
		//healthEntityResult = service.findHealthEntityById(id);
		//return ResponseEntity.ok(healthEntityResult);
		 return ResponseEntity.ok().body(service.findHealthEntityById(id));
	}

	@GetMapping("/search/{id}")
	public ResponseEntity<Search> searchById(@PathVariable String id) {
		Search search = new Search();
		search = service.searchDocumentById(id);
		return ResponseEntity.ok(search);
	}
	//@GetMapping("/search")
	

}