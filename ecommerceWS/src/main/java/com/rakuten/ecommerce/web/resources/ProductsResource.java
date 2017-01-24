package com.rakuten.ecommerce.web.resources;
 
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.rakuten.ecommerce.service.ProductService;
import com.rakuten.ecommerce.service.exception.CurrencyNotSupportedException;
import com.rakuten.ecommerce.service.exception.InvalidClientRequestException;
import com.rakuten.ecommerce.service.exception.ThirdPartyRequestFailedException;
import com.rakuten.ecommerce.web.entities.ProductFromWeb;
import com.rakuten.ecommerce.web.swagger.ApiDocumentationConstants;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
/**
 * @author Kshitiz Garg
 */
@Api(value = ApiDocumentationConstants.PRODUCTS)
@RestController
@RequestMapping(ApiDocumentationConstants.PRODUCTS_API)
public class ProductsResource {
 
	private static final String LOCATION = ApiDocumentationConstants.PRODUCT_API + "/{id}";
	
	private static final Logger logger = LoggerFactory.getLogger(ProductsResource.class);

	@Autowired
	private ProductService productService;
	
	@ApiOperation(value =  ApiDocumentationConstants.PRODUCTS_POST , httpMethod = ApiDocumentationConstants.POST, notes = ApiDocumentationConstants.PRODUCTS_POST_NOTES)
	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes= MediaType.APPLICATION_JSON)
    public @ResponseBody ResponseEntity<String> create(UriComponentsBuilder uriBuilder, @RequestBody @ApiParam ProductFromWeb productDetails){
		try {
			long productId = productService.createProduct(productDetails);
			UriComponents uriComponents = uriBuilder.path(LOCATION).buildAndExpand(productId);
			return ResponseEntity.created(uriComponents.toUri()).build();
		}
		catch (InvalidClientRequestException e) {
			logger.error(e.getMessage());
			logger.error("Sending back HTTP {} to the caller", e.getHttpStatusCode());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} 
		catch (ThirdPartyRequestFailedException e) {
			logger.error(e.getMessage());
			logger.error("Sending back 503 to the caller");
			return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
		}
		catch (CurrencyNotSupportedException e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
		}
		catch (Exception e) {
			logger.error("Error creating product for details:{} ",productDetails, e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }

	// TODO GET with offset
}