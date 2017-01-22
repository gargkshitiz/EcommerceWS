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
import com.rakuten.ecommerce.service.CategoryService;
import com.rakuten.ecommerce.service.exception.InvalidClientRequestException;
import com.rakuten.ecommerce.web.entities.Categories;
import com.rakuten.ecommerce.web.util.ApiDocumentationConstants;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
/**
 * @author Kshitiz Garg
 */
@Api(value = ApiDocumentationConstants.CATEGORIES)
@RequestMapping(ApiDocumentationConstants.CATEGORIES_API)
@RestController
public class CategoriesResource {
 
	private static final Logger logger = LoggerFactory.getLogger(CategoriesResource.class);

	@Autowired
	private CategoryService categoryService;
	
	@ApiOperation(value =  ApiDocumentationConstants.CATEGORIES_POST , httpMethod = ApiDocumentationConstants.POST, notes = ApiDocumentationConstants.CATEGORIES_POST_NOTES)
	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes= MediaType.APPLICATION_JSON)
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