package com.ocr.computervision.controller;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.azure.core.credential.AzureKeyCredential;
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

	private static AzureKeyCredential healthApiCredential;

	private AzureKeyCredential piiApiCredential;
	private static URI healthApiEndpointURI;
	private String piiapisubscriptionKey;
	static String piiApiEndpoint;

	@Autowired
	private ComputerVisionService service;

	private static final String imageToAnalyze = "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png";

	@PostMapping(value = "/extractText", consumes = "multipart/*", produces = "application/json;charset=utf-8")
	public ResponseEntity<String> extractText(@RequestParam("file") MultipartFile file) throws IOException, URISyntaxException {

		ocrapisubscriptionKey = service.getCredential("OCRAPI").subscriptionKey.toString();
		ocrAPIEndpoint = service.getCredential("OCRAPI").endpoint.toString();
		ocrAPIURI = ocrAPIEndpoint + "vision/v3.2/read/syncAnalyze";
		
		String jsonString = invokeHttpClient(imageToAnalyze, ocrAPIURI);
		//store value in database
		return ResponseEntity.ok(jsonString);
	}

	public String invokeHttpClient(String imageUrl, String clientUrl) throws URISyntaxException, ClientProtocolException, IOException {
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();

		
			URIBuilder builder = new URIBuilder(clientUrl);
			String jsonString = new String();
			// Prepare the URI for the REST API method.
			URI uri = builder.build();
			HttpPost request = new HttpPost(uri);

			// Request headers.
			request.setHeader("Content-Type", "application/json");
			request.setHeader("Ocp-Apim-Subscription-Key", ocrapisubscriptionKey);

			// Request body.
			StringEntity requestEntity = new StringEntity("{\"url\":\"" + imageUrl + "\"}");
			request.setEntity(requestEntity);

			// Call the REST API method and get the response entity.
			HttpResponse response = httpClient.execute(request);
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				// Format and display the JSON response.
				jsonString	= EntityUtils.toString(entity);
				JSONObject json = new JSONObject(jsonString);
				System.out.println("REST Response:\n");
				System.out.println(json.toString(2));
			}
		return jsonString;
	}

}
