package com.rakuten.ecommerce.web.resources;
 
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.rakuten.ecommerce.service.ProductService;
import com.rakuten.ecommerce.service.exception.InvalidClientRequestException;
import com.rakuten.ecommerce.web.entities.ProductDetails;
import com.rakuten.ecommerce.web.util.ApiDocumentation;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
@Api(value = ApiDocumentation.PRODUCT)
@RestController
@RequestMapping(ApiDocumentation.PRODUCT_API)
/**
 * @author Kshitiz Garg
 */
public class ProductResource {
 
	private static final Logger logger = LoggerFactory.getLogger(ProductResource.class);

	@Autowired
	private ProductService productService;
	
	@ApiOperation(value =  ApiDocumentation.PRODUCT_POST , httpMethod = ApiDocumentation.POST, notes = ApiDocumentation.PRODUCT_POST_NOTES)
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

	@ApiOperation(value =  ApiDocumentation.PRODUCT_GET , httpMethod = ApiDocumentation.GET, notes = ApiDocumentation.PRODUCT_GET_NOTES)
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
    public Response get(){
		try {
			productService.get(1);
			return Response.ok().build();
		}
		catch (InvalidClientRequestException e) {
			logger.error(e.getMessage());
			logger.error("Sending back HTTP {} to the caller", e.getHttpStatusCode());
			return Response.status(e.getHttpStatusCode().value()).entity(new Gson().toJson(e.getMessage())).build();
		} 
		catch (Exception e) {
			String msg = "Error getting product : "+1;
			logger.error(msg, e);
			return Response.serverError().entity(new Gson().toJson(msg)).build();
		}
    }
}