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
import com.rakuten.ecommerce.web.entities.CategoryDetails;
import com.rakuten.ecommerce.web.swagger.ApiDocumentationConstants;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
/**
 * @author Kshitiz Garg
 */
@Api(value = ApiDocumentationConstants.CATEGORY)
@RestController
@RequestMapping(ApiDocumentationConstants.CATEGORY_API)
public class CategoryResource {
 
	private static final Logger logger = LoggerFactory.getLogger(CategoryResource.class);

	@Autowired
	private CategoryService categoryService;
	
	@ApiOperation(value =  ApiDocumentationConstants.CATEGORY_POST , httpMethod = ApiDocumentationConstants.POST, notes = ApiDocumentationConstants.CATEGORY_POST_NOTES)
	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes= MediaType.APPLICATION_JSON)
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