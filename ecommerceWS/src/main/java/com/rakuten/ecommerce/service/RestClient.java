package com.rakuten.ecommerce.service;

import org.springframework.http.ResponseEntity;

import com.rakuten.ecommerce.service.exception.RequestFailedException;

/**
 * @author Kshitiz Garg
 */
public interface RestClient {

	/**
	 * Performs HTTP GET on service at passed resource. It has inbuilt retries (max {@value #MAX_ATTEMPTS} times) for HTTP_SERVER_ERRORS, i.e. for 5xx status code series
	 * @param resourcePath
	 * @return ResponseEntity<String>
	 * @throws RequestFailedException
	 */
	ResponseEntity<String> get(String resourcePath) throws RequestFailedException;

	/**
	 * Performs HTTP POST on service at passed resource with the passed jsonRequestBody. It has inbuilt retries (max {@value #MAX_ATTEMPTS} times) for HTTP_SERVER_ERRORS, i.e. for 5xx status code series
	 * @param resourcePath
	 * @param jsonRequestBody
	 * @return ResponseEntity<String>
	 * @throws RequestFailedException
	 */
	ResponseEntity<String> post(String resourcePath, String jsonRequestBody) throws RequestFailedException;
	/**
	 * Performs HTTP POST on given postUrl with the passed requestBody and set the given content-Type and character set
	 * @param postUrl
	 * @param requestBody
	 * @param shouldRetry
	 * @return ResponseEntity<String>
	 * @throws RequestFailedException 
	 */
	 ResponseEntity<String> post(String resourcePath, String jsonRequestBody, boolean shouldRetry) throws RequestFailedException;
	
	/**
	 * Performs HTTP POST on given postUrl with the passed requestBody and set the given content-Type and character set
	 * @param postUrl
	 * @param requestBody
	 * @param contentType
	 * @param charSet
	 * @return ResponseEntity<String>
	 * @throws RequestFailedException 
	 */
	ResponseEntity<String> post(String postUrl, String requestBody, String contentType, String charSet) throws RequestFailedException;


	/**
	 * Performs HTTP PUT on service at passed resource with the passed jsonRequestBody. It has inbuilt retries (max {@value #MAX_ATTEMPTS} times) for HTTP_SERVER_ERRORS, i.e. for 5xx status code series
	 * @param resourcePath
	 * @param jsonRequestBody
	 * @return ResponseEntity<String>
	 * @throws RequestFailedException
	 */
	ResponseEntity<String> put(String resourcePath, String jsonRequestBody) throws RequestFailedException;

	/**
	 * Use it very carefully. Performs HTTP DELETE on eloqua at passed resource. It has inbuilt retries (max {@value #MAX_ATTEMPTS} times) for HTTP_SERVER_ERRORS, i.e. for 5xx status code series
	 * @param resourcePath
	 * @return ResponseEntity<String>
	 * @throws RequestFailedException
	 */
	ResponseEntity<String> delete(String resourcePath) throws RequestFailedException;
	
	/**
	 * Performs HTTP GET on service at passed resource. It has inbuilt retries (max {@value #MAX_ATTEMPTS} times) for HTTP_SERVER_ERRORS, i.e. for 5xx status code series
	 * @param resourcePath
	 * @return ResponseEntity<String>
	 * @throws RequestFailedException
	 */
	ResponseEntity<String> getOnBulkApi(String resourcePath) throws RequestFailedException;

	/**
	 * Performs HTTP POST on service at passed resource with the passed jsonRequestBody. It has inbuilt retries (max {@value #MAX_ATTEMPTS} times) for HTTP_SERVER_ERRORS, i.e. for 5xx status code series
	 * @param resourcePath
	 * @param jsonRequestBody
	 * @return ResponseEntity<String>
	 * @throws RequestFailedException
	 */
	ResponseEntity<String> postOnBulkApi(String resourcePath, String jsonRequestBody) throws RequestFailedException;	

	String SUCCESS = "success";

	String STATUS = "status";

	String URI = "uri";

	int MAX_ATTEMPTS = 3;

}
