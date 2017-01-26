package com.rakuten.ecommerce.web.resources;
 
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.rakuten.ecommerce.service.ProductService;
import com.rakuten.ecommerce.service.exception.CurrencyNotSupportedException;
import com.rakuten.ecommerce.service.exception.DataNotFoundException;
import com.rakuten.ecommerce.service.exception.InvalidClientRequestException;
import com.rakuten.ecommerce.service.exception.ThirdPartyRequestFailedException;
import com.rakuten.ecommerce.web.entities.BulkProductResponse;
import com.rakuten.ecommerce.web.entities.ProductRequest;
import com.rakuten.ecommerce.web.swagger.ApiDocumentationConstants;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
/**
 * @author Kshitiz Garg
 */
@Api(value = ApiDocumentationConstants.PRODUCTS)
@RestController
@RequestMapping(ApiDocumentationConstants.PRODUCTS_API)
public class ProductsResource {
 
	static final String LOCATION = ApiDocumentationConstants.PRODUCT_API + "/{id}";
	
	private static final Logger logger = LoggerFactory.getLogger(ProductsResource.class);

	@Autowired
	private ProductService productService;
	
	@ResponseStatus
	@ApiResponses(value = {
        @ApiResponse(code = 201, message = "Product created successfully"),
        @ApiResponse(code = 400, message = "Bad request"),
        @ApiResponse(code = 412, message = "Currency not supported"),
        @ApiResponse(code = 503, message = "Currency conversion service not available")
	})
	@ApiOperation(value =  ApiDocumentationConstants.PRODUCTS_POST , httpMethod = ApiDocumentationConstants.POST, notes = ApiDocumentationConstants.PRODUCTS_POST_NOTES)
	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes= MediaType.APPLICATION_JSON)
    public @ResponseBody ResponseEntity<String> create(UriComponentsBuilder uriBuilder, @RequestBody @ApiParam ProductRequest productRequest){
		try {
			long productId = productService.createProduct(productRequest);
			UriComponents uriComponents = uriBuilder.path(LOCATION).buildAndExpand(productId);
			return ResponseEntity.created(uriComponents.toUri()).build();
		}
		catch (InvalidClientRequestException e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} 
		catch (ThirdPartyRequestFailedException e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
		}
		catch (CurrencyNotSupportedException e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
		}
		catch (Exception e) {
			logger.error("Error creating product for details:{} ",productRequest, e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }

	@ResponseStatus
	@ApiResponses(value = {
        @ApiResponse(code = 200, message = "Products fetched successfully"),
        @ApiResponse(code = 400, message = "Bad request"),
        @ApiResponse(code = 404, message = "Entity not found"),
        @ApiResponse(code = 412, message = "Currency not supported"),
        @ApiResponse(code = 503, message = "Currency conversion service not available")
	})
	@ApiOperation(value =  ApiDocumentationConstants.PRODUCTS_GET , httpMethod = ApiDocumentationConstants.GET, notes = ApiDocumentationConstants.PRODUCTS_GET_NOTES)
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<?> get(@RequestParam(value = "startingProductId", required=false) long startingProductId, @RequestParam(value = "desiredPriceCurrency", required=false) String desiredPriceCurrency){
		try {
			return new ResponseEntity<>(productService.getProducts(startingProductId, desiredPriceCurrency), noCacheHeaders(), HttpStatus.OK);
		}
		catch (DataNotFoundException e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(noProducts(), noCacheHeaders(), HttpStatus.OK);
		} 
		catch (InvalidClientRequestException e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), e.getHttpStatusCode());
		} 
		catch (ThirdPartyRequestFailedException e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
		}
		catch (CurrencyNotSupportedException e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
		}
		catch (Exception e) {
			logger.error("Error fetching products starting with id: {}", startingProductId, e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
	
	private BulkProductResponse noProducts() {
		BulkProductResponse bulkProductResponse = new BulkProductResponse();
		bulkProductResponse.setHasMore(false);
		return bulkProductResponse;
	}

	private HttpHeaders noCacheHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.add(ApiDocumentationConstants.EXPIRES_PARAM, ApiDocumentationConstants.EXPIRES_PARAM_VALUE);
		headers.add(ApiDocumentationConstants.CACHE_CONTROL_PARAM, ApiDocumentationConstants.CACHE_CONTROL_PARAM_VALUE);
		headers.add(ApiDocumentationConstants.PRAGMA_PARAM, ApiDocumentationConstants.PRAGMA_PARAM_VALUE);
		return headers;
	}

}