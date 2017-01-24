package com.rakuten.ecommerce.web.swagger;
/**
 * @author Kshitiz Garg
 */
public interface ApiDocumentationConstants {

	String CACHE_CONTROL_PARAM = "Cache-Control";

	String CACHE_CONTROL_PARAM_VALUE = "no-cache, no-store, must-revalidate";

	String PRAGMA_PARAM = "Pragma";

	String PRAGMA_PARAM_VALUE = "no-cache";

	String EXPIRES_PARAM = "Expires";

	String EXPIRES_PARAM_VALUE = "0";

	String GET = "GET";
	String POST = "POST";
	String PUT = "PUT";
	String DELETE = "DELETE";

	String PRODUCT = "product";
	String PRODUCT_API = "/"+PRODUCT;
	String PRODUCT_API_DESC = "API to fetch, update or delete a product";
	String PRODUCT_GET = "Fetches an existing product from ecommerceWS";
	String PRODUCT_GET_NOTES = "Here client could also request to see the price of the product in a desiredPriceCurrency (alongwith prices in EURO, which comes back by default) using a query parameter. Here associated categories are being returned as well (which are lazily loaded in the backend)";
	String PRODUCT_PUT = "Updates a product's details in ecommerceWS";
	String PRODUCT_PUT_NOTES = "We could change productDesc, productCurrency, price, catgeoryIds, unitsInStock, productCode, productType etc. New price in a new currency can also be given here";
	String PRODUCT_DELETE = "Deletes a product and all of its category mappings from ecommerceWS";
	String PRODUCT_DELETE_NOTES = "Categories would not be impacted, only the relavant productCategory mappings would be wiped off";
	
	String PRODUCTS = "products";
	String PRODUCTS_API = "/"+PRODUCTS;
	String PRODUCTS_API_DESC = "API to create a new product in ecommerceWS";
	String PRODUCTS_POST = "Creates a new product in  ecommerceWS";
	String PRODUCTS_POST_NOTES = "Input catgeoryIds could be valid category id(s) OR absent (no category for product at the moment). Check 'location' header for the create productId";
	
	String CATEGORY = "category";
	String CATEGORY_API = "/"+CATEGORY;
	String CATEGORY_API_DESCRIPTION = "API to fetch, update or delete a category";
	String CATEGORY_GET = "Fetches an existing category from ecommerceWS";
	String CATEGORY_GET_NOTES = "In the response body, -1 parentCategoryId signifies no parent category exists for this category";
	String CATEGORY_PUT = "Updates a category details in ecommerceWS";
	String CATEGORY_PUT_NOTES = "Here we could change categoryDesc, categoryName, parentCategoryId. parentCategoryId should be a valid categoryId. -1 is reserved for no parent and hence is allowed too";
	String CATEGORY_DELETE = "Deletes a category and all of its product mappings from ecommerceWS";
	String CATEGORY_DELETE_NOTES = "Products would not be impacted, only the relavant productCategory mappings would be wiped off";
	
	String CATEGORIES = "categories";
	String CATEGORIES_API = "/"+CATEGORIES;
	String CATEGORIES_API_DESCRIPTION = "This API is used for modal pop(s). Use POST for fetching modal pop ups(s) fulfiling the configured condition(s). Creation (PUT) and deletion (DELETE) of modal pop up(s) are also possible";
	String CATEGORIES_POST = "Creates a new category in  ecommerceWS";
	String CATEGORIES_POST_NOTES = "From the request, parent category Id could be absent (no parent) OR -1 (no parent) or a valid category id (valid parent)";

}