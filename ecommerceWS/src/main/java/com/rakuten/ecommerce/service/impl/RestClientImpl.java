package com.rakuten.ecommerce.service.impl;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.rakuten.ecommerce.service.RestClient;
import com.rakuten.ecommerce.service.exception.RequestFailedException;
/**
 * @author Kshitiz Garg
 */
@Service
public class RestClientImpl implements RestClient {
	
	private static final Logger logger = LoggerFactory.getLogger(RestClientImpl.class);

	@Autowired
	private RestTemplate restTemplate;
	
	private HttpHeaders headers;

	@Value("${service-url}")
	private String serviceUrl;
	
	@Value("${interval-between-retrials-in-secs}")
	private long retryIntervalInSecs;
	
	@PostConstruct
	public void setAuthHeader(){
		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
	}

	@Override
	public ResponseEntity<String> get(String resourcePath) throws RequestFailedException{
		HttpEntity<String> httpEntity = new HttpEntity<String>(headers);
		return call(serviceUrl, HttpMethod.GET, httpEntity, true);
	}

	@Override
	public ResponseEntity<String> delete(String resourcePath) throws RequestFailedException{
		HttpEntity<String> httpEntity = new HttpEntity<String>(headers);
		return call(serviceUrl, HttpMethod.DELETE, httpEntity, true);
	}
	
	@Override
	public ResponseEntity<String> post(String resourcePath, String jsonRequestBody) throws RequestFailedException{
		HttpEntity<String> httpEntity = new HttpEntity<String>(jsonRequestBody, headers);
		return call(serviceUrl, HttpMethod.POST, httpEntity, true);
	}
	
	@Override
	public ResponseEntity<String> post(String resourcePath, String jsonRequestBody, boolean shouldRetry) throws RequestFailedException{
		HttpEntity<String> httpEntity = new HttpEntity<String>(jsonRequestBody, headers);
		return call(serviceUrl, HttpMethod.POST, httpEntity, shouldRetry);
	}
	
	@Override
	public ResponseEntity<String> put(String resourcePath, String jsonRequestBody) throws RequestFailedException{
		HttpEntity<String> httpEntity = new HttpEntity<String>(jsonRequestBody, headers);
		return call(serviceUrl, HttpMethod.PUT, httpEntity, true);
	}
	
	@Override
	public ResponseEntity<String> getOnBulkApi(String resourcePath) throws RequestFailedException {
		HttpEntity<String> httpEntity = new HttpEntity<String>(headers);
		return call(serviceUrl, HttpMethod.GET, httpEntity, true);
	}
	
	@Override
	public ResponseEntity<String> postOnBulkApi(String resourcePath, String jsonRequestBody) throws RequestFailedException {
		HttpEntity<String> httpEntity = new HttpEntity<String>(jsonRequestBody, headers);
		return call(serviceUrl, HttpMethod.POST, httpEntity, true);
	}
	
	@Override
	public ResponseEntity<String> post(String postUrl, String requestBody, String contentType, String charSet) throws RequestFailedException {
		HttpHeaders customHeaders = new HttpHeaders();
		List<Charset> acceptableCharsets = new ArrayList<Charset>();
	    acceptableCharsets.add(Charset.forName(charSet));
	    customHeaders.setContentType(MediaType.valueOf(contentType));
	    customHeaders.setAcceptCharset(acceptableCharsets);
		HttpEntity<String> httpEntity = new HttpEntity<String>(requestBody, customHeaders);
		return call(postUrl, HttpMethod.POST, httpEntity, true);
	}
	
	private ResponseEntity<String> call(String url, HttpMethod httpMethod,	HttpEntity<String> httpEntity, boolean shouldRetry) throws RequestFailedException {
		int attemptCount = 1;
		while (attemptCount <= MAX_ATTEMPTS) {
			try{
				ResponseEntity<String> responseEntity = restTemplate.exchange(url, httpMethod, httpEntity, String.class);
				HttpStatus.Series responseSeries = responseEntity.getStatusCode().series();
				if(!HttpStatus.Series.SERVER_ERROR.equals(responseSeries)){
					return responseEntity;
				}
				logger.error("Try: ".concat(String.valueOf(attemptCount)).concat(". Got a server error while calling Eloqua at:").concat(url));
				attemptCount++;
			}
			catch(HttpClientErrorException e){
				logger.error("Received HttpClientErrorException while calling Eloqua at url: {}",url, e);
				logger.error("Response received from Eloqua: {} ", e.getResponseBodyAsString());
				RequestFailedException RequestFailedException = new RequestFailedException(e.getMessage() + ". Response - " + e.getResponseBodyAsString());
				RequestFailedException.setHttpStatusCode(e.getStatusCode());
				throw RequestFailedException;
			}
			catch (RestClientException e) {
				logger.error("Try: ".concat(String.valueOf(attemptCount)).concat(". Got a RestClientException while calling Eloqua at:").concat(url), e);
				attemptCount++;
			}
			catch (Exception e) {
				logger.error("Received Generic Exception while calling Eloqua at url: {}",url, e);
				throw new RequestFailedException(e.getMessage());
			}
			if(!shouldRetry){
				String errorMessage = "Retry is not enabled for this particular call: ".concat(httpMethod.name()).concat(" at URL: ").concat(url);
				throw new RequestFailedException(errorMessage);
			}
			if(attemptCount <= MAX_ATTEMPTS){
				try {
					sleep();
				} 
				catch (InterruptedException e) {
					logger.error("Could not wait between successive retrials. Moving ahead");
				}
			}
		}
		String errorMessage = "Max retry attempts exhausted while calling ".concat(httpMethod.name()).concat(" at URL: ").concat(url);
		throw new RequestFailedException(errorMessage);
	}

	void sleep() throws InterruptedException {
		Thread.sleep(retryIntervalInSecs*1000l);
	}

}