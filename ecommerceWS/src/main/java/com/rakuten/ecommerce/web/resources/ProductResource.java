package com.rakuten.ecommerce.web.resources;
 
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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

import com.google.gson.Gson;
import com.rakuten.ecommerce.service.ProductService;
import com.rakuten.ecommerce.service.exception.DataNotFoundException;
import com.rakuten.ecommerce.service.exception.InvalidClientRequestException;
import com.rakuten.ecommerce.service.exception.ThirdPartyRequestFailedException;
import com.rakuten.ecommerce.web.entities.ProductDetails;
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
	
	@ApiOperation(value =  ApiDocumentationConstants.PRODUCT_POST , httpMethod = ApiDocumentationConstants.POST, notes = ApiDocumentationConstants.PRODUCT_POST_NOTES)
	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes= MediaType.APPLICATION_JSON)
    public Response save(ProductDetails productDetails){
		try {
			productService.persist(productDetails);
			return Response.ok().build();
		}
		catch (InvalidClientRequestException e) {
			logger.error(e.getMessage());
			logger.error("Sending back HTTP {} to the caller", e.getHttpStatusCode());
			return Response.status(e.getHttpStatusCode().value()).entity(new Gson().toJson(e.getMessage())).build();
		} 
		catch (Exception e) {
			String msg = "Error persisting product : "+productDetails;
			logger.error(msg, e);
			return Response.serverError().entity(new Gson().toJson(msg)).build();
		}
    }

	@ApiOperation(value =  ApiDocumentationConstants.PRODUCT_GET , httpMethod = ApiDocumentationConstants.GET, notes = ApiDocumentationConstants.PRODUCT_GET_NOTES)
	@RequestMapping(value = "/{productId}" ,method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<?> get(@PathVariable(name="productId") long productId, @RequestParam("desiredPriceCurrency") String desiredPriceCurrency){
		try {
			return new ResponseEntity<>(productService.get(productId, desiredPriceCurrency), HttpStatus.OK);
		}
		catch (InvalidClientRequestException | DataNotFoundException e) {
			logger.error(e.getMessage());
			logger.error("Sending back HTTP {} to the caller", e.getHttpStatusCode());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} 
		catch (ThirdPartyRequestFailedException e) {
			logger.error(e.getMessage());
			logger.error("Sending back 503 to the caller");
			return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
		}
		catch (Exception e) {
			String msg = "Error fetching product details for id:{} "+productId;
			logger.error(msg, e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
}