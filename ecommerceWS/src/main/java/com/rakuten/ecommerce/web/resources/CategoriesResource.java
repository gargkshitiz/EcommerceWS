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

import com.rakuten.ecommerce.service.CategoryService;
import com.rakuten.ecommerce.web.entities.CategoryFromWeb;
import com.rakuten.ecommerce.web.swagger.ApiDocumentationConstants;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
	
	private static final String LOCATION = ApiDocumentationConstants.CATEGORY_API + "/{id}";
	
	@ApiOperation(value =  ApiDocumentationConstants.CATEGORIES_POST , httpMethod = ApiDocumentationConstants.POST, notes = ApiDocumentationConstants.CATEGORIES_POST_NOTES)
	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes= MediaType.APPLICATION_JSON)
    public @ResponseBody ResponseEntity<String> create(UriComponentsBuilder uriBuilder, @RequestBody @ApiParam CategoryFromWeb categoryFromWeb){
		try {
			long categoryId = categoryService.createCategory(categoryFromWeb);
			UriComponents uriComponents = uriBuilder.path(LOCATION).buildAndExpand(categoryId);
			return ResponseEntity.created(uriComponents.toUri()).build();
		}
		catch (Exception e) {
			logger.error("Error creating category for details:{} ",categoryFromWeb, e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }

}