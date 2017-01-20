package com.rakuten.ecommerce.web.resources;
 
import javax.jws.WebService;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.rakuten.ecommerce.service.ProductService;
import com.rakuten.ecommerce.service.exception.InvalidClientRequestException;
import com.rakuten.ecommerce.web.entities.ProductDetails;
import com.rakuten.ecommerce.web.util.ApiDocumentation;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
/**
 * @author kgarg
 */
@Api(value = ApiDocumentation.PRODUCT, description = ApiDocumentation.PRODUCT_API_DESC)
@Path(ApiDocumentation.PRODUCT_API)
@WebService(name=ApiDocumentation.PRODUCT)
public class ProductResource {
 
	private static final Logger logger = LoggerFactory.getLogger(ProductResource.class);

	@Autowired
	private ProductService productService;
	
	@ApiOperation(value =  ApiDocumentation.PRODUCT_POST , httpMethod = ApiDocumentation.POST, notes = ApiDocumentation.PRODUCT_POST_NOTES)
	@POST
    @Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
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

}