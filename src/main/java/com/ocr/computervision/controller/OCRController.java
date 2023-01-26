package com.ocr.computervision.controller;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.azure.ai.textanalytics.TextAnalyticsAsyncClient;
import com.azure.ai.textanalytics.TextAnalyticsClient;
import com.azure.ai.textanalytics.TextAnalyticsClientBuilder;
import com.azure.ai.textanalytics.models.AnalyzeHealthcareEntitiesOperationDetail;
import com.azure.ai.textanalytics.models.AnalyzeHealthcareEntitiesOptions;
import com.azure.ai.textanalytics.models.AnalyzeHealthcareEntitiesResult;
import com.azure.ai.textanalytics.models.HealthcareEntity;
import com.azure.ai.textanalytics.models.PiiEntity;
import com.azure.ai.textanalytics.models.PiiEntityCollection;
import com.azure.ai.textanalytics.models.TextDocumentInput;

import com.azure.ai.textanalytics.util.AnalyzeHealthcareEntitiesPagedFlux;
import com.azure.ai.textanalytics.util.AnalyzeHealthcareEntitiesPagedIterable;
import com.azure.ai.textanalytics.util.AnalyzeHealthcareEntitiesResultCollection;
import com.azure.core.credential.AzureKeyCredential;
import com.azure.core.http.rest.PagedResponse;
import com.azure.core.util.Context;
import com.azure.core.util.polling.PollerFlux;
import com.azure.core.util.polling.SyncPoller;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.ocr.computervision.model.Claims;
import com.ocr.computervision.model.HealthEntity;
import com.ocr.computervision.model.HealthEntityResult;
import com.ocr.computervision.model.Lines;
import com.ocr.computervision.model.PIIEntity;
import com.ocr.computervision.model.PIIEntityResult;
import com.ocr.computervision.model.ReadResult;
import com.ocr.computervision.ocrconstants.ocrConstants;
import com.ocr.computervision.service.ComputerVisionService;

@RestController
public class OCRController {
	static String ocrapisubscriptionKey;
	static String ocrAPIEndpoint;
	static String ocrAPIURI;
	static String healthApiURI;
	static String healthApiEndpoint;
	static String subscrriptionKey;
	static String healthapisubscriptionKey;
	static String endpoint;
	static String credentialType;

	private static URI healthApiEndpointURI;
	private String healthApiEndpointURI2;
	private AzureKeyCredential piiApiCredential;
	private static URI piiApiEndpointURI;
	private String piiapisubscriptionKey;
	static String piiApiEndpoint;

	private static AzureKeyCredential healthApiCredential;

	@Autowired
	private ComputerVisionService service;

	@PostMapping(value = "/extractText", consumes = "multipart/*", produces = "application/json;charset=utf-8")
	public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file)
			throws IOException, URISyntaxException {

		ocrapisubscriptionKey = service.getCredential("OCRAPI").subscriptionKey.toString();
		ocrAPIEndpoint = service.getCredential("OCRAPI").endpoint.toString();

		ocrAPIURI = ocrAPIEndpoint + ocrConstants.OCR_API_URI;

		String jsonString = "";
		String ocrText = "";

		ocrAPIURI = ocrAPIEndpoint + "vision/v3.2/read/syncAnalyze";

		byte[] bytes = null;

		if (!file.isEmpty()) {
			bytes = file.getBytes();
			jsonString = invokeHttpClient(bytes, ocrAPIURI);

			Claims claimResult = new Claims();
			ObjectMapper objectMapper = new ObjectMapper();
			claimResult = objectMapper.readValue(jsonString, Claims.class);

			claimResult.claimimage = bytes;

			for (ReadResult rr : claimResult.getAnalyzeResult().getReadResults()) {
				for (Lines lines : rr.getLines()) {
					ocrText = ocrText + " " + lines.getText();
				}
			}
			String claimId = service.SaveClaim(claimResult);

		}
		return ResponseEntity.ok(ocrText);

	}

	public String invokeHttpClient(byte[] bytes, String clientUrl)
			throws URISyntaxException, ClientProtocolException, IOException {
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();

		URIBuilder builder = new URIBuilder(clientUrl);
		String jsonString = new String();
		// Prepare the URI for the REST API method.
		URI uri = builder.build();
		HttpPost request = new HttpPost(uri);

		// Request headers.
		request.setHeader("Ocp-Apim-Subscription-Key", ocrapisubscriptionKey);
		request.setHeader("Content-type", "application/octet-stream");

		// Request file bytes
		request.setEntity(new ByteArrayEntity(bytes));

		// Call the REST API method and get the response entity.
		HttpResponse response = httpClient.execute(request);
		HttpEntity entity = response.getEntity();

		if (entity != null) {
			// Format and display the JSON response.
			jsonString = EntityUtils.toString(entity);
			JSONObject json = new JSONObject(jsonString);
			System.out.println("REST Response:\n");
			System.out.println(json.toString(2));
		}

		return jsonString;
	}

	@PostMapping(value = "/piiEntity")
	public ResponseEntity<String> getPIIEntityResult(@RequestBody String document) {
		// PII(Personal Info) Detection API
		piiapisubscriptionKey = service.getCredential("PIIAPI").subscriptionKey.toString();
		// piiApiCredential= new AzureKeyCredential(piiapisubscriptionKey);
		piiApiEndpoint = service.getCredential("PIIAPI").endpoint.toString();

		TextAnalyticsClient piiAPIClient = authenticatepiiClient(piiapisubscriptionKey, piiApiEndpoint);
		String response = ExtractSavePIIRelatedInfo(piiAPIClient, document);

		return ResponseEntity.ok(response);
	}

	// Method to authenticate the client object with your key and endpoint
	private static TextAnalyticsClient authenticatepiiClient(String piiapisubscriptionKey, String piiApiEndpoint) {
		return new TextAnalyticsClientBuilder().credential(new AzureKeyCredential(piiapisubscriptionKey))
				.endpoint(piiApiEndpoint).buildClient();

	}

	private String ExtractSavePIIRelatedInfo(TextAnalyticsClient piiAPIClient, String document) {
		String piiEntityResponse = "";
		List<PIIEntity> resultPII = new ArrayList<PIIEntity>();
		PIIEntityResult newPIIEntityResult = new PIIEntityResult();
		try {

			List<TextDocumentInput> td1 = Arrays.asList(new TextDocumentInput("0", document));
			PiiEntityCollection piiEntityCollection = (piiAPIClient.recognizePiiEntities(document));
			piiEntityCollection.forEach(entity -> System.out.printf(
					"Recognized Personally Identifiable Information entity: %s, entity category: %s, entity subcategory: %s,"
							+ " confidence score: %f.%n",
					entity.getText(), entity.getCategory(), entity.getSubcategory(), entity.getConfidenceScore()));
			for (PiiEntity piiEntity : piiEntityCollection) {

				PIIEntity newEntity = new PIIEntity();
				newEntity.setText(piiEntity.getText());
				newEntity.setCategory(piiEntity.getCategory().toString());
				newEntity.setConfidenceScore(piiEntity.getConfidenceScore());
				resultPII.add(newEntity);
			}
			newPIIEntityResult.setPIIEntities(resultPII);

			piiEntityResponse = service.savePIIEntityResult(newPIIEntityResult);

		} catch (Exception e) {
			piiEntityResponse = "Exception occured while processing healthapi" + e.getMessage();
		}
		return piiEntityResponse;
	}

	@GetMapping(value = "/piiEntity/{id}")
	public ResponseEntity<PIIEntityResult> getPIIEntityById(@PathVariable String id) {

		return ResponseEntity.ok().body(service.findById(id));

	}

	@PostMapping(value = "/healthEntity")
	public ResponseEntity<String> getHealthEntityResult(@RequestBody String document) {

		// Health Related Info Analytic API
		healthapisubscriptionKey = service.getCredential("ANALYTICAPI").subscriptionKey.toString();
		healthApiCredential = new AzureKeyCredential(healthapisubscriptionKey);
		healthApiEndpoint = service.getCredential("ANALYTICAPI").endpoint.toString();
		// healthApiEndpointURI = new Uri(healthApiEndpoint);

		healthApiURI = healthApiEndpoint + "/language/analyze-text/jobs";

		TextAnalyticsAsyncClient healthAPIClient = authenticateClient(healthapisubscriptionKey, healthApiEndpoint);

		String response = ExtractSaveHealthRelatedInfo(healthAPIClient, document);
		return ResponseEntity.ok(response);
	}

	// Method to authenticate the client object with your key and endpoint
	private static TextAnalyticsAsyncClient authenticateClient(String healthapisubscriptionKey, String healthApiURI) {
		return new TextAnalyticsClientBuilder().credential(new AzureKeyCredential(healthapisubscriptionKey))
				.endpoint(healthApiURI).buildAsyncClient();
	}

	private String ExtractSaveHealthRelatedInfo(TextAnalyticsAsyncClient healthAPIClient, String document) {
		String healthEntityResponse = "";
		HealthEntityResult newHealthEntityResult = new HealthEntityResult();
		try {

			List<TextDocumentInput> td = Arrays.asList(new TextDocumentInput("0", document));
			AnalyzeHealthcareEntitiesOptions options = new AnalyzeHealthcareEntitiesOptions()

					.setIncludeStatistics(true);

			healthAPIClient.beginAnalyzeHealthcareEntities(td, options).flatMap(pollResult -> {
				AnalyzeHealthcareEntitiesOperationDetail operationResult = pollResult.getValue();
				System.out.printf("Operation created time: %s, expiration time: %s.%n", operationResult.getCreatedAt(),
						operationResult.getExpiresAt());
				return pollResult.getFinalResult();
			}).flatMap(analyzeHealthcareEntitiesPagedFlux -> analyzeHealthcareEntitiesPagedFlux.byPage()).subscribe(
					perPage -> processAnalyzeHealthcareEntitiesResultCollection(perPage, newHealthEntityResult),
					ex -> System.out.println("Error listing pages: " + ex.getMessage()),
					() -> System.out.println("Successfully listed all pages"));

			System.out.println("new updated entity result" + newHealthEntityResult.getEntities());

			// healthEntityResponse = service.saveHealthEntityResult(newHealthEntityResult);
		} catch (Exception e) {
			healthEntityResponse = "Exception occured while processing healthapi" + e.getMessage();
		}
		return healthEntityResponse;
	}

	private static void processAnalyzeHealthcareEntitiesResultCollection(
			PagedResponse<AnalyzeHealthcareEntitiesResultCollection> perPage,
			HealthEntityResult newHealthEntityResult) {

		List<HealthEntity> result = new ArrayList<HealthEntity>();

		for (AnalyzeHealthcareEntitiesResultCollection resultCollection : perPage.getElements()) {

			for (AnalyzeHealthcareEntitiesResult healthcareEntitiesResult : resultCollection) {
				System.out.println("Document ID = " + healthcareEntitiesResult.getId());
				System.out.println("Document entities: ");

				for (HealthcareEntity entity : healthcareEntitiesResult.getEntities()) {

					HealthEntity newEntity = new HealthEntity();
					newEntity.setText(entity.getText());
					newEntity.setCategory(entity.getCategory().toString());
					newEntity.setConfidenceScore(entity.getConfidenceScore());
					result.add(newEntity);
				}
				newHealthEntityResult.setEntities(result);
			}
		}
	}

}