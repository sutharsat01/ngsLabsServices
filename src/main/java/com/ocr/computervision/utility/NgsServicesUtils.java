package com.ocr.computervision.utility;

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
import org.springframework.stereotype.Component;

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
import com.azure.ai.textanalytics.util.AnalyzeHealthcareEntitiesResultCollection;
import com.azure.core.credential.AzureKeyCredential;
import com.azure.core.http.rest.PagedResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ocr.computervision.model.HealthEntity;
import com.ocr.computervision.model.HealthEntityResult;
import com.ocr.computervision.model.PIIEntity;
import com.ocr.computervision.model.PIIEntityResult;
import com.ocr.computervision.service.ComputerVisionService;
@Component
public class NgsServicesUtils {

	@Autowired
	private  ComputerVisionService service;

	public NgsServicesUtils() {
		super();
	}

	// Method to authenticate the client object with your key and endpoint
	protected static TextAnalyticsAsyncClient authenticateClient(String healthapisubscriptionKey, String healthApiURI) {
		return new TextAnalyticsClientBuilder().credential(new AzureKeyCredential(healthapisubscriptionKey))
				.endpoint(healthApiURI).buildAsyncClient();
	}

	public static String ExtractSaveHealthRelatedInfo(TextAnalyticsAsyncClient healthAPIClient, String document) {
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

	
	
	  // Method to authenticate the client object with your key and endpoint
	  
	protected static  TextAnalyticsClient authenticatepiiClient(String piiapisubscriptionKey, String piiApiEndpoint) { return new
	  TextAnalyticsClientBuilder().credential(new
	  AzureKeyCredential(piiapisubscriptionKey))
	  .endpoint(piiApiEndpoint).buildClient();
	  
	  }
	 
	 

	public static void processAnalyzeHealthcareEntitiesResultCollection(
			PagedResponse<AnalyzeHealthcareEntitiesResultCollection> perPage,
			HealthEntityResult newHealthEntityResult) {

		List<HealthEntity> result = new ArrayList<HealthEntity>();

		for (AnalyzeHealthcareEntitiesResultCollection resultCollection : perPage.getElements()) {

			for (AnalyzeHealthcareEntitiesResult healthcareEntitiesResult : resultCollection) {
				System.out.println("Document ID = " + healthcareEntitiesResult.getId());
				System.out.println("Document entities: ");

				for (HealthcareEntity entity : healthcareEntitiesResult.getEntities()) {

					HealthEntity newEntity = new HealthEntity();
					newEntity.setEntityName(entity.getText());
					newEntity.setCategory(entity.getCategory().toString());
					newEntity.setConfidenceScore(entity.getConfidenceScore());
					result.add(newEntity);
				}
				newHealthEntityResult.setEntities(result);
			}
		}
	}

	protected static String invokeHttpClient(byte[] bytes, String clientUrl, String ocrapisubscriptionKey)
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

	public  String ExtractSavePIIRelatedInfo(TextAnalyticsClient piiAPIClient, String extractedText) {
		ObjectMapper obj = new ObjectMapper();
		
		String piiEntityResponse = "";
		List<PIIEntity> resultPII = new ArrayList<PIIEntity>();
		PIIEntityResult newPIIEntityResult = new PIIEntityResult();
		try {

			List<TextDocumentInput> td1 = Arrays.asList(new TextDocumentInput("0", extractedText));
			PiiEntityCollection piiEntityCollection = (piiAPIClient.recognizePiiEntities(extractedText));
			
			for (PiiEntity piiEntity : piiEntityCollection) {

				PIIEntity newEntity = new PIIEntity();
				newEntity.setText(piiEntity.getText());
				newEntity.setCategory(piiEntity.getCategory().toString());
				newEntity.setConfidenceScore(piiEntity.getConfidenceScore());
				resultPII.add(newEntity);
			}
			newPIIEntityResult.setPIIEntities(resultPII);

			PIIEntityResult piiEntityResultResponse = service.savePIIEntityResult(newPIIEntityResult);
			 piiEntityResponse = obj.writeValueAsString(piiEntityResultResponse);

		} catch (Exception e) {
			piiEntityResponse = "Exception occured while processing piiapi" + e.getMessage();
		}
		return piiEntityResponse;
	}

}