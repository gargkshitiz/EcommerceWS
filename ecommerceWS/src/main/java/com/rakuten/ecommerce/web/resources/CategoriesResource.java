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
import com.rakuten.ecommerce.web.entities.Categories;
import com.rakuten.ecommerce.web.util.ApiDocumentation;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
/**
 * @author Kshitiz Garg
 */
@Api(value = ApiDocumentation.CATEGORIES, description = ApiDocumentation.CATEGORIES_API_DESCRIPTION)
@Path(ApiDocumentation.CATEGORIES_API)
public class CategoriesResource {
 
	private static final Logger logger = LoggerFactory.getLogger(CategoriesResource.class);

	@Autowired
	private CategoryService categoryService;
	
	@ApiOperation(value =  ApiDocumentation.CATEGORIES_POST , httpMethod = ApiDocumentation.POST, notes = ApiDocumentation.CATEGORIES_POST_NOTES)
	@POST
    @Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
    public Response save(Categories categories){
		try {
			categoryService.persist(categories);
			return Response.ok().build();
		}
		catch (InvalidClientRequestException e) {
			logger.error(e.getMessage());
			logger.error("Sending back HTTP {} to the caller", e.getHttpStatusCode());
			return Response.status(e.getHttpStatusCode().value()).entity(new Gson().toJson(e.getMessage())).build();
		} 
		catch (Exception e) {
			String msg = "Error persisting categories : "+categories;
			logger.error(msg, e);
			return Response.serverError().entity(new Gson().toJson(msg)).build();
		}
    }

}