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
	String PATCH = "PATCH";

	String PRODUCT = "product";
	String PRODUCT_API = "/"+PRODUCT;
	String PRODUCT_API_DESC = "Use this API to submit a user activity or reset isShown flags associated with modal popup and user activities";
	String PRODUCT_POST = "POSTs a user activity in personalizationWS";
	String PRODUCT_POST_NOTES = "This API is used for recording user activities, for e.g. to update the latest page view count for a user";
	String PRODUCT_GET = "POSTs a user activity in personalizationWS";
	String PRODUCT_GET_NOTES = "This API is used for recording user activities, for e.g. to update the latest page view count for a user";
	String PRODUCT_PUT = "Persists all modal popup(s) and corresponding condition(s) associated with the published page";
	String PRODUCT_PUT_NOTES = "Provide all info required from the point of view of modal popup(s) at page publishing time, within request body. It persists all modal popup(s) and corresponding condition(s) associated with the published page";
	String PRODUCT_DELETE = "Deletes modal pop up, its conditions and all its counts from this system";
	String PRODUCT_DELETE_NOTES = "Deletes modal pop up, its conditions and all its counts from personalizatioWS. All history for the modal pop would also be wiped off with this call";

	String PRODUCTS = "products";
	String PRODUCTS_API = "/"+PRODUCTS;
	String PRODUCTS_API_DESC = "Use this API to submit a user activity or reset isShown flags associated with modal popup and user activities";
	String PRODUCTS_POST = "POSTs a user activity in personalizationWS";
	String PRODUCTS_POST_NOTES = "This API is used for recording user activities, for e.g. to update the latest page view count for a user";
	String PRODUCTS_GET = "POSTs a user activity in personalizationWS";
	String PRODUCTS_GET_NOTES = "This API is used for recording user activities, for e.g. to update the latest page view count for a user";
	String PRODUCTS_PUT = "Persists all modal popup(s) and corresponding condition(s) associated with the published page";
	String PRODUCTS_PUT_NOTES = "Provide all info required from the point of view of modal popup(s) at page publishing time, within request body. It persists all modal popup(s) and corresponding condition(s) associated with the published page";
	
	String CATEGORY = "category";
	String CATEGORY_API = "/"+CATEGORY;
	String CATEGORY_API_DESCRIPTION = "This API is used for modal pop(s). Use POST for fetching modal pop ups(s) fulfiling the configured condition(s). Creation (PUT) and deletion (DELETE) of modal pop up(s) are also possible";
	String CATEGORY_POST = "Fetches modal pop ups(s) fulfiling the configured condition(s)";
	String CATEGORY_POST_NOTES = "Provide basic info like page path, user role, access type etc in request body and this API would fetch modal pop ups(s) fulfiling the configured condition(s)";
	String CATEGORY_PUT = "Persists all modal popup(s) and corresponding condition(s) associated with the published page";
	String CATEGORY_PUT_NOTES = "Provide all info required from the point of view of modal popup(s) at page publishing time, within request body. It persists all modal popup(s) and corresponding condition(s) associated with the published page";
	String CATEGORY_DELETE = "Deletes modal pop up, its conditions and all its counts from this system";
	String CATEGORY_DELETE_NOTES = "Deletes modal pop up, its conditions and all its counts from personalizatioWS. All history for the modal pop would also be wiped off with this call";
	
	String CATEGORIES = "categories";
	String CATEGORIES_API = "/"+CATEGORIES;
	String CATEGORIES_API_DESCRIPTION = "This API is used for modal pop(s). Use POST for fetching modal pop ups(s) fulfiling the configured condition(s). Creation (PUT) and deletion (DELETE) of modal pop up(s) are also possible";
	String CATEGORIES_POST = "Fetches modal pop ups(s) fulfiling the configured condition(s)";
	String CATEGORIES_POST_NOTES = "Provide basic info like page path, user role, access type etc in request body and this API would fetch modal pop ups(s) fulfiling the configured condition(s)";
	String CATEGORIES_PUT = "Persists all modal popup(s) and corresponding condition(s) associated with the published page";
	String CATEGORIES_PUT_NOTES = "Provide all info required from the point of view of modal popup(s) at page publishing time, within request body. It persists all modal popup(s) and corresponding condition(s) associated with the published page";

}