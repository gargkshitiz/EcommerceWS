package com.rakuten.ecommerce.web.resources;
 
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.rakuten.ecommerce.service.ProductService;
import com.rakuten.ecommerce.service.exception.CurrencyNotSupportedException;
import com.rakuten.ecommerce.service.exception.DataNotFoundException;
import com.rakuten.ecommerce.service.exception.InvalidClientRequestException;
import com.rakuten.ecommerce.service.exception.ThirdPartyRequestFailedException;
import com.rakuten.ecommerce.web.entities.ProductRequest;
import com.rakuten.ecommerce.web.swagger.ApiDocumentationConstants;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
@Api(value = ApiDocumentationConstants.PRODUCT)
@RestController
@RequestMapping(ApiDocumentationConstants.PRODUCT_API+"/{productId}")
/**
 * @author Kshitiz Garg
 */
public class ProductResource {
 
	private static final String PRODUCT_ID = "productId";

	private static final Logger logger = LoggerFactory.getLogger(ProductResource.class);

	@Autowired
	private ProductService productService;
	
	@ResponseStatus
	@ApiResponses(value = {
        @ApiResponse(code = 200, message = "Product fetched successfully"),
        @ApiResponse(code = 400, message = "Bad request"),
        @ApiResponse(code = 404, message = "Entity not found"),
        @ApiResponse(code = 412, message = "Currency not supported"),
        @ApiResponse(code = 503, message = "Currency conversion service not available")
	})
	@ApiOperation(value =  ApiDocumentationConstants.PRODUCT_GET , httpMethod = ApiDocumentationConstants.GET, notes = ApiDocumentationConstants.PRODUCT_GET_NOTES)
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<?> get(@PathVariable(name=PRODUCT_ID) long productId, @RequestParam(value = "desiredPriceCurrency", required=false) String desiredPriceCurrency){
		try {
			return new ResponseEntity<>(productService.getProductWithCategories(productId, desiredPriceCurrency), noCacheHeaders(), HttpStatus.OK);
		}
		catch (DataNotFoundException e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(e.getHttpStatusCode());
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
			logger.error("Error fetching product details against productId:{} ", productId, e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }

	private HttpHeaders noCacheHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.add(ApiDocumentationConstants.EXPIRES_PARAM, ApiDocumentationConstants.EXPIRES_PARAM_VALUE);
		headers.add(ApiDocumentationConstants.CACHE_CONTROL_PARAM, ApiDocumentationConstants.CACHE_CONTROL_PARAM_VALUE);
		headers.add(ApiDocumentationConstants.PRAGMA_PARAM, ApiDocumentationConstants.PRAGMA_PARAM_VALUE);
		return headers;
	}
	
	@ResponseStatus
	@ApiResponses(value = {
        @ApiResponse(code = 204, message = "Product deleted successfully"),
        @ApiResponse(code = 400, message = "Bad request"),
        @ApiResponse(code = 404, message = "Entity not found")
	})
	@ApiOperation(value =  ApiDocumentationConstants.PRODUCT_DELETE , httpMethod = ApiDocumentationConstants.DELETE, notes = ApiDocumentationConstants.PRODUCT_DELETE_NOTES)
	@RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable(name=PRODUCT_ID) long productId){
		try {
			productService.deleteProduct(productId);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		catch (DataNotFoundException e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(e.getHttpStatusCode());
		} 
		catch (Exception e) {
			logger.error("Error deleting product against productId:{} ", productId, e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
	
	@ResponseStatus
	@ApiResponses(value = {
        @ApiResponse(code = 204, message = "Product updated successfully"),
        @ApiResponse(code = 400, message = "Bad request"),
        @ApiResponse(code = 404, message = "Entity not found"),
        @ApiResponse(code = 412, message = "Currency not supported"),
        @ApiResponse(code = 503, message = "Currency conversion service not available")
	})
	@ApiOperation(value =  ApiDocumentationConstants.PRODUCT_PUT , httpMethod = ApiDocumentationConstants.PUT, notes = ApiDocumentationConstants.PRODUCT_PUT_NOTES)
	@RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<?> put(@PathVariable(name=PRODUCT_ID) long productId, @RequestBody @ApiParam ProductRequest productRequest ){
		try {
			productService.updateProduct(productId, productRequest);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		catch (DataNotFoundException e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(e.getHttpStatusCode());
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
			logger.error("Error updating product against productId:{} ", productId, e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
	
}