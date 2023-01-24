package com.ocr.computervision.controller;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.azure.ai.textanalytics.TextAnalyticsClient;
import com.azure.ai.textanalytics.TextAnalyticsClientBuilder;
import com.azure.ai.textanalytics.models.AnalyzeHealthcareEntitiesOperationDetail;
import com.azure.ai.textanalytics.models.AnalyzeHealthcareEntitiesOptions;
import com.azure.ai.textanalytics.models.TextDocumentInput;
import com.azure.ai.textanalytics.util.AnalyzeHealthcareEntitiesPagedIterable;
import com.azure.core.credential.AzureKeyCredential;
import com.azure.core.util.Context;
import com.azure.core.util.polling.SyncPoller;
import com.ctc.wstx.shaded.msv_core.util.Uri;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ocr.computervision.model.Claims;
import com.ocr.computervision.model.HealthEntity;
import com.ocr.computervision.model.HealthEntityResult;
import com.ocr.computervision.model.Lines;
import com.ocr.computervision.model.ReadResult;
import com.ocr.computervision.ocrconstants.ocrConstants;
import com.ocr.computervision.service.ComputerVisionService;

@RestController
public class OCRController {
	static String ocrapisubscriptionKey;
	static String ocrAPIEndpoint;
	static String ocrAPIURI;

	static String healthApiEndpoint;
	static String subscrriptionKey;
	static String healthapisubscriptionKey;
	static String endpoint;
	static String credentialType;
	private static URI healthApiEndpointURI;
	private String healthApiEndpointURI2;
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
		
		byte[] bytes = null;

		if (!file.isEmpty()) {
			bytes = file.getBytes();
			jsonString = invokeHttpClient(bytes, ocrAPIURI);
		Claims claimResult = new Claims();
		ObjectMapper objectMapper = new ObjectMapper();
		claimResult = objectMapper.readValue(jsonString, Claims.class);
	
	claimResult.claimimage = bytes;
	
	for (ReadResult rr:claimResult.getAnalyzeResult().getReadResults()){
		for (Lines lines:rr.getLines()){
			ocrText = ocrText + " " + lines.getText();
		}
	}
		String claimId = service.SaveClaim(claimResult);
		
		
		}
		return ResponseEntity.ok(ocrText);

	}
	
	@PostMapping(value = "/healthEntity")
    public void getHealthEntityResult(@RequestBody String document) {
		
		// Health Related Info Analytic API
		healthapisubscriptionKey = service.getCredential("ANALYTICAPI").subscriptionKey.toString();
		//healthApiCredential = new AzureKeyCredential(healthapisubscriptionKey);
		healthApiEndpoint = service.getCredential("ANALYTICAPI").endpoint.toString();
		//healthApiEndpointURI = new Uri(healthApiEndpoint);
		//healthApiEndpoint = service.getCredential("ANALYTICAPI").endpoint.toString();

		

		healthApiEndpointURI2 = healthApiEndpoint + ocrConstants.HEALTH_API_ENDPOINT_URI2;
		TextAnalyticsClient healthAPIClient =  authenticateClient(healthapisubscriptionKey, healthApiEndpoint);
        ExtractSaveHealthRelatedInfo(healthAPIClient,document);
	}
 private void ExtractSaveHealthRelatedInfo(TextAnalyticsClient healthAPIClient, String document) {
	 List<HealthEntity> result = new ArrayList<HealthEntity>();
	HealthEntityResult  newHealthEntityResult = new HealthEntityResult();
	List<TextDocumentInput> td = Arrays.asList( new TextDocumentInput("0", document)) ;
	AnalyzeHealthcareEntitiesOptions options = new AnalyzeHealthcareEntitiesOptions().setIncludeStatistics(true);

    SyncPoller<AnalyzeHealthcareEntitiesOperationDetail, AnalyzeHealthcareEntitiesPagedIterable>
            syncPoller = healthAPIClient.beginAnalyzeHealthcareEntities(td, options, Context.NONE); }
	
 
 
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
	 // Method to authenticate the client object with your key and endpoint
    private static TextAnalyticsClient authenticateClient(String healthapisubscriptionKey, String healthApiEndpoint) {
        return new TextAnalyticsClientBuilder()
                .credential(new AzureKeyCredential(healthapisubscriptionKey))
                .endpoint(healthApiEndpoint)
                .buildClient();
    }
}