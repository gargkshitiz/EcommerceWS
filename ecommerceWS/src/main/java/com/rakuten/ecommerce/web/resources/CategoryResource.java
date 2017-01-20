package com.rakuten.ecommerce.web.resources;
 
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
import com.rakuten.ecommerce.service.CategoryService;
import com.rakuten.ecommerce.service.exception.InvalidClientRequestException;
import com.rakuten.ecommerce.web.entities.CategoryDetails;
import com.rakuten.ecommerce.web.util.ApiDocumentation;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
/**
 * @author Kshitiz Garg
 */
@Api(value = ApiDocumentation.CATEGORY, description = ApiDocumentation.CATEGORY_API_DESCRIPTION)
@Path(ApiDocumentation.CATEGORY_API)
public class CategoryResource {
 
	private static final Logger logger = LoggerFactory.getLogger(CategoryResource.class);

	@Autowired
	private CategoryService categoryService;
	
	@ApiOperation(value =  ApiDocumentation.CATEGORY_POST , httpMethod = ApiDocumentation.POST, notes = ApiDocumentation.CATEGORY_POST_NOTES)
	@POST
    @Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
    public Response save(CategoryDetails categoryDetails){
		try {
			categoryService.persist(categoryDetails);
			return Response.ok().build();
		}
		catch (InvalidClientRequestException e) {
			logger.error(e.getMessage());
			logger.error("Sending back HTTP {} to the caller", e.getHttpStatusCode());
			return Response.status(e.getHttpStatusCode().value()).entity(new Gson().toJson(e.getMessage())).build();
		} 
		catch (Exception e) {
			String msg = "Error persisting CATEGORY : "+categoryDetails;
			logger.error(msg, e);
			return Response.serverError().entity(new Gson().toJson(msg)).build();
		}
    }

}