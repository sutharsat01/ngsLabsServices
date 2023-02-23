package com.ocr.computervision.controller;

import java.io.IOException;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.azure.ai.textanalytics.TextAnalyticsAsyncClient;
import com.azure.ai.textanalytics.TextAnalyticsClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ocr.computervision.model.Claims;
import com.ocr.computervision.model.HealthEntityResult;
import com.ocr.computervision.model.Lines;
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
	public static String ocrAPIURI;
	static String healthApiURI;
	static String healthApiEndpointURI;
	static String healthApiEndpoint;
	static String subscrriptionKey;
	static String healthapisubscriptionKey;
	static String endpoint;
	static String credentialType;

	private String piiapisubscriptionKey;
	static String piiApiEndpoint;



	@Autowired
	private ComputerVisionService service;

	@Autowired
	private NgsServicesUtils ngsServicesUtil;

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
		HttpHeaders httpHeaders = new HttpHeaders();
		// PII(Personal Info) Detection API
		piiapisubscriptionKey = service.getCredential("PIIAPI").subscriptionKey.toString();

		piiApiEndpoint = service.getCredential("PIIAPI").endpoint.toString();

		TextAnalyticsClient piiAPIClient = NgsServicesUtils.authenticatepiiClient(piiapisubscriptionKey,
				piiApiEndpoint);

		String response = ngsServicesUtil.ExtractSavePIIRelatedInfo(piiAPIClient, extractedText);
         
		httpHeaders.add("Content-Type", "application/json");
		return new ResponseEntity<> (response, httpHeaders, HttpStatus.OK);
	}

	@PostMapping(value = "/classifyHealthEntity")
	public ResponseEntity<String> getHealthEntityResult(@RequestBody String extractedText) {
		HttpHeaders httpHeaders = new HttpHeaders();
		// Health Related Info Analytic API
		healthapisubscriptionKey = service.getCredential("ANALYTICAPI").subscriptionKey.toString();
		
		healthApiEndpoint = service.getCredential("ANALYTICAPI").endpoint.toString();
		
		healthApiEndpointURI = healthApiEndpoint + ngsServicesConsts.HEALTH_API_ENDPOINT_URI2;

		TextAnalyticsClient healthAPIClient = NgsServicesUtils.authenticateClient(healthapisubscriptionKey,
				healthApiEndpoint);

		String response = ngsServicesUtil.ExtractSaveHealthRelatedInfo(healthAPIClient, extractedText);
		httpHeaders.add("Content-Type", "application/json");
		return new ResponseEntity<> (response, httpHeaders, HttpStatus.OK);
	}

	@GetMapping(value = "/piiEntity/{id}")
	public ResponseEntity<PIIEntityResult> getPIIEntityById(@PathVariable String id) {
		PIIEntityResult piiEntityResult = new PIIEntityResult();
		piiEntityResult = service.findById(id);
		return ResponseEntity.ok().body(piiEntityResult);
	}

	@GetMapping(value = "/healthEntity/{id}")
	public ResponseEntity<HealthEntityResult> getHealthEntityById(@PathVariable String id) {
		HealthEntityResult healthEntityResult = new HealthEntityResult();
		healthEntityResult = service.findHealthEntityById(id);
		return ResponseEntity.ok().body(healthEntityResult);
	}

	@GetMapping("/search/{id}")
	public ResponseEntity<Search> searchById(@PathVariable String id) {
		Search search = new Search();
		search = service.searchDocumentById(id);
		return ResponseEntity.ok(search);
	}

	@PostMapping("/search")
	public ResponseEntity<String> createSearch(@RequestBody Search search) {
		Search searchResult = new Search();

		ObjectMapper obj = new ObjectMapper();
		String searchResponse = "";
		try {
			searchResult = service.createSearch(search);
			searchResponse = obj.writeValueAsString(searchResult);
		} catch (Exception e) {
			searchResponse = "Exception occured while saving search result" + e.getMessage();
		}
		return ResponseEntity.ok().body(searchResponse);

	}

	@PutMapping("/search/{id}")
	public ResponseEntity<Search> updateSearch(@PathVariable String id, @RequestBody Search search) {

		return ResponseEntity.ok().body(service.updateSearch(search, id));
	}

}