package com.rakuten.ecommerce.web.resources;
 
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.rakuten.ecommerce.service.CategoryService;
import com.rakuten.ecommerce.service.exception.DataNotFoundException;
import com.rakuten.ecommerce.web.entities.CategoryFromWeb;
import com.rakuten.ecommerce.web.swagger.ApiDocumentationConstants;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
/**
 * @author Kshitiz Garg
 */
@Api(value = ApiDocumentationConstants.CATEGORY)
@RestController
@RequestMapping(ApiDocumentationConstants.CATEGORY_API+"/{categoryId}")
public class CategoryResource {
 
	private static final String CATEGORY_ID = "categoryId";

	private static final Logger logger = LoggerFactory.getLogger(CategoryResource.class);

	@Autowired
	private CategoryService categoryService;
	
	@ApiOperation(value =  ApiDocumentationConstants.CATEGORY_GET , httpMethod = ApiDocumentationConstants.GET, notes = ApiDocumentationConstants.CATEGORY_GET_NOTES)
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<?> get(@PathVariable(name=CATEGORY_ID) long categoryId){
		try {
			return new ResponseEntity<>(categoryService.getCategory(categoryId), noCacheHeaders(), HttpStatus.OK);
		}
		catch (DataNotFoundException e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(e.getHttpStatusCode());
		} 
		catch (Exception e) {
			logger.error("Error fetching catgeory details for id:{} ", categoryId, e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
	
	@ApiOperation(value =  ApiDocumentationConstants.CATEGORY_PUT , httpMethod = ApiDocumentationConstants.PUT, notes = ApiDocumentationConstants.CATEGORY_PUT_NOTES)
	@RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON)
    public ResponseEntity<?> put(@PathVariable(name=CATEGORY_ID) long categoryId, @RequestBody @ApiParam CategoryFromWeb categoryFromWeb){
		try {
			categoryService.updateCategory(categoryId, categoryFromWeb);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		catch (DataNotFoundException e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(e.getHttpStatusCode());
		}
		catch (Exception e) {
			logger.error("Error updating category details for id:{} ", categoryId, e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
	
	@ApiOperation(value =  ApiDocumentationConstants.CATEGORY_PATCH , httpMethod = ApiDocumentationConstants.PATCH, notes = ApiDocumentationConstants.CATEGORY_PATCH_NOTES)
	@RequestMapping(method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON)
    public ResponseEntity<?> patch(@PathVariable(name=CATEGORY_ID) long categoryId, @RequestBody @ApiParam CategoryFromWeb categoryFromWeb){
		try {
			categoryService.patchCategory(categoryId, categoryFromWeb);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		catch (DataNotFoundException e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(e.getHttpStatusCode());
		}
		catch (Exception e) {
			logger.error("Error updating category details for id:{} ", categoryId, e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
	
	@ApiOperation(value =  ApiDocumentationConstants.CATEGORY_DELETE , httpMethod = ApiDocumentationConstants.DELETE, notes = ApiDocumentationConstants.CATEGORY_DELETE_NOTES)
	@RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable(name=CATEGORY_ID) long categoryId){
		try {
			categoryService.deleteCategory(categoryId);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		catch (DataNotFoundException e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(e.getHttpStatusCode());
		} 
		catch (Exception e) {
			logger.error("Error deleting category details for id:{} ", categoryId, e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
	
	private HttpHeaders noCacheHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.add(ApiDocumentationConstants.EXPIRES_PARAM, ApiDocumentationConstants.EXPIRES_PARAM_VALUE);
		headers.add(ApiDocumentationConstants.CACHE_CONTROL_PARAM, ApiDocumentationConstants.CACHE_CONTROL_PARAM_VALUE);
		headers.add(ApiDocumentationConstants.PRAGMA_PARAM, ApiDocumentationConstants.PRAGMA_PARAM_VALUE);
		return headers;
	}

}