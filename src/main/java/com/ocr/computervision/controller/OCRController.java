package com.ocr.computervision.controller;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

	static String piiApiEndpoint;

	@Autowired
	private ComputerVisionService service;

	@PostMapping(value = "/extractText", consumes = "multipart/*", produces = "application/json;charset=utf-8")
	public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file)
			throws IOException, URISyntaxException {

		ocrapisubscriptionKey = service.getCredential("OCRAPI").subscriptionKey.toString();
		ocrAPIEndpoint = service.getCredential("OCRAPI").endpoint.toString();
		ocrAPIURI = ocrAPIEndpoint + "vision/v3.2/read/syncAnalyze";
		String jsonString = "";
		byte[] bytes = null;

		if (!file.isEmpty()) {
			bytes = file.getBytes();
			jsonString = invokeHttpClient(bytes, ocrAPIURI);
		}
		return ResponseEntity.ok(jsonString);

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

}
