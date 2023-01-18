package com.ocr.computervision.controller;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.tomcat.util.http.fileupload.FileUtils;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.azure.core.credential.AzureKeyCredential;
import com.azure.core.http.HttpClient;

import com.ocr.computervision.model.Claims;
import com.ocr.computervision.service.ComputerVisionService;

import jakarta.websocket.Decoder.Text;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;




// Annotation
@RestController

// Class

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
    private static URI piiApiEndpointURI;
    private String healthApiEndpointURI2;
    private String piiapisubscriptionKey;
    static String piiApiEndpoint;
    
    @Autowired
	private ComputerVisionService service; 
    
    @PostMapping(value = "/api/OCR")
    		//,consumes= {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE,})
 
    public void postOCRResponse(@RequestParam(value = "file",required = false) MultipartFile file) {
		ocrapisubscriptionKey = service.getCredential("OCRAPI").subscriptionKey.toString();
        ocrAPIEndpoint = service.getCredential("OCRAPI").endpoint.toString();
        ocrAPIURI = ocrAPIEndpoint + "vision/v3.2/read/syncAnalyze";
        
      //Health Related Info Analytic API
        healthapisubscriptionKey = service.getCredential("ANALYTICAPI").subscriptionKey.toString();
        healthApiCredential = new AzureKeyCredential(healthapisubscriptionKey);
        healthApiEndpoint = service.getCredential("ANALYTICAPI").endpoint.toString();
        try {
			healthApiEndpointURI = new URI(healthApiEndpoint);
		} catch (URISyntaxException e) {
			
			e.printStackTrace();
		}
        healthApiEndpointURI2 = healthApiEndpoint + "/language/analyze-text/jobs";

        //PII(Personal Info) Detection API
        piiapisubscriptionKey = service.getCredential("PIIAPI").subscriptionKey.toString();
        piiApiCredential = new AzureKeyCredential(piiapisubscriptionKey);
        piiApiEndpoint = service.getCredential("PIIAPI").endpoint.toString();
        try {
			piiApiEndpointURI = new URI(piiApiEndpoint);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        if (!file.isEmpty()) {
        try {
			byte[] bytearr =file.getBytes();
			String myJSONResult = ReadTextFromStream(bytearr);
			int x = 10;
		} catch (IOException e) {
			
			e.printStackTrace();
		}
        
        }
	}
    private String ReadTextFromStream(byte[] imageByte) {
     String result ="";
     OkHttpClient client = new OkHttpClient().newBuilder().build();

    		  
    	try {
    		MediaType mediaType = MediaType.parse("application/json");
    		RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)

    				  .addFormDataPart("","",

    				    RequestBody.create(MediaType.parse("application/octet-stream"),
                         imageByte )) .build();
    				  

    				 
    		// URIBuilder uriBuilder = new URIBuilder(ocrAPIURI);
    		// uriBuilder.setParameter("language", "en");
    		
    		 // Request parameters.
             //URI uri = uriBuilder.build();
    		String requestParameters = "language=en"; //&detectOrientation=true";
            String uri = ocrAPIURI + "?" + requestParameters;
             Request request = new Request.Builder()

            		  .url(uri)

            		  .method("POST", body)
            		  .addHeader("Ocp-Apim-Subscription-Key", ocrapisubscriptionKey)

            		  .addHeader("accept", "application/json")

            		  .addHeader("Content-Type", "application/json")

            		  .build();
            // HttpPost request = new HttpPost(uri);
          
             // Request headers.
            // request.addHeader("Ocp-Apim-Subscription-Key", ocrapisubscriptionKey);
           //  request.setEntity(new ByteArrayEntity(imageByte));
            // HttpResponse response = httpClient.execute(request);
             Response response = client.newCall(request).execute();
             ResponseBody entity = response.body();
            // HttpResponseMessage response;
             if (entity != null) {
                 // Format and display the JSON response.
                  result = entity.toString();
                // JSONObject json = new JSONObject(jsonString);
                 
             }
            
    	}
    	catch (Exception e)
        {
    		result= e.getMessage();
    		 System.out.println(e.getMessage());
        }
		return result;
    }
    public static void  OCRExample(Claims String)
    {
    	String ocrText = "";
    	
   }
    

		
	
}
