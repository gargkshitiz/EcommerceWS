package com.rakuten.ecommerce.web.resources;
 
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rakuten.ecommerce.service.ProductService;
import com.rakuten.ecommerce.service.exception.CurrencyNotSupportedException;
import com.rakuten.ecommerce.service.exception.DataNotFoundException;
import com.rakuten.ecommerce.service.exception.InvalidClientRequestException;
import com.rakuten.ecommerce.service.exception.ThirdPartyRequestFailedException;
import com.rakuten.ecommerce.web.swagger.ApiDocumentationConstants;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
@Api(value = ApiDocumentationConstants.PRODUCT)
@RestController
@RequestMapping(ApiDocumentationConstants.PRODUCT_API)
/**
 * @author Kshitiz Garg
 */
public class ProductResource {
 
	private static final Logger logger = LoggerFactory.getLogger(ProductResource.class);

	@Autowired
	private ProductService productService;
	
	@ApiOperation(value =  ApiDocumentationConstants.PRODUCT_GET , httpMethod = ApiDocumentationConstants.GET, notes = ApiDocumentationConstants.PRODUCT_GET_NOTES)
	@RequestMapping(value = "/{productId}" ,method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<?> get(@PathVariable(name="productId") long productId, @RequestParam("desiredPriceCurrency") String desiredPriceCurrency){
		try {
			return new ResponseEntity<>(productService.get(productId, desiredPriceCurrency), HttpStatus.OK);
		}
		catch (InvalidClientRequestException | DataNotFoundException e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
			logger.error("Error fetching product details for id:{} ", productId, e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
}