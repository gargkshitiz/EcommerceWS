package com.rakuten.ecommerce.service;

import org.springframework.http.ResponseEntity;

import com.rakuten.ecommerce.service.exception.RestRequestFailedException;

/**
 * @author Kshitiz Garg
 */
public interface RestClient {

	/**
	 * Performs HTTP GET on service at passed resource. It has inbuilt retries (max {@value #MAX_ATTEMPTS} times) for HTTP_SERVER_ERRORS, i.e. for 5xx status code series
	 * @param resourcePath
	 * @return ResponseEntity<String>
	 * @throws RestRequestFailedException
	 */
	ResponseEntity<String> get(String resourcePath) throws RestRequestFailedException;

	/**
	 * Performs HTTP POST on service at passed resource with the passed jsonRequestBody. It has inbuilt retries (max {@value #MAX_ATTEMPTS} times) for HTTP_SERVER_ERRORS, i.e. for 5xx status code series
	 * @param resourcePath
	 * @param jsonRequestBody
	 * @return ResponseEntity<String>
	 * @throws RestRequestFailedException
	 */
	ResponseEntity<String> post(String resourcePath, String jsonRequestBody) throws RestRequestFailedException;
	/**
	 * Performs HTTP POST on given postUrl with the passed requestBody and set the given content-Type and character set
	 * @param postUrl
	 * @param requestBody
	 * @param shouldRetry
	 * @return ResponseEntity<String>
	 * @throws RestRequestFailedException 
	 */
	 ResponseEntity<String> post(String resourcePath, String jsonRequestBody, boolean shouldRetry) throws RestRequestFailedException;
	
	/**
	 * Performs HTTP POST on given postUrl with the passed requestBody and set the given content-Type and character set
	 * @param postUrl
	 * @param requestBody
	 * @param contentType
	 * @param charSet
	 * @return ResponseEntity<String>
	 * @throws RestRequestFailedException 
	 */
	ResponseEntity<String> post(String postUrl, String requestBody, String contentType, String charSet) throws RestRequestFailedException;


	/**
	 * Performs HTTP PUT on service at passed resource with the passed jsonRequestBody. It has inbuilt retries (max {@value #MAX_ATTEMPTS} times) for HTTP_SERVER_ERRORS, i.e. for 5xx status code series
	 * @param resourcePath
	 * @param jsonRequestBody
	 * @return ResponseEntity<String>
	 * @throws RestRequestFailedException
	 */
	ResponseEntity<String> put(String resourcePath, String jsonRequestBody) throws RestRequestFailedException;

	/**
	 * Use it very carefully. Performs HTTP DELETE on eloqua at passed resource. It has inbuilt retries (max {@value #MAX_ATTEMPTS} times) for HTTP_SERVER_ERRORS, i.e. for 5xx status code series
	 * @param resourcePath
	 * @return ResponseEntity<String>
	 * @throws RestRequestFailedException
	 */
	ResponseEntity<String> delete(String resourcePath) throws RestRequestFailedException;
	
	/**
	 * Performs HTTP GET on service at passed resource. It has inbuilt retries (max {@value #MAX_ATTEMPTS} times) for HTTP_SERVER_ERRORS, i.e. for 5xx status code series
	 * @param resourcePath
	 * @return ResponseEntity<String>
	 * @throws RestRequestFailedException
	 */
	ResponseEntity<String> getOnBulkApi(String resourcePath) throws RestRequestFailedException;

	/**
	 * Performs HTTP POST on service at passed resource with the passed jsonRequestBody. It has inbuilt retries (max {@value #MAX_ATTEMPTS} times) for HTTP_SERVER_ERRORS, i.e. for 5xx status code series
	 * @param resourcePath
	 * @param jsonRequestBody
	 * @return ResponseEntity<String>
	 * @throws RestRequestFailedException
	 */
	ResponseEntity<String> postOnBulkApi(String resourcePath, String jsonRequestBody) throws RestRequestFailedException;	

	String SUCCESS = "success";

	String STATUS = "status";

	String URI = "uri";

	int MAX_ATTEMPTS = 3;

	String SYMBOLS_Q_PARAM = "?symbols=";

	String RATES = "rates";
	
	String getFixerBaseUri();

}
