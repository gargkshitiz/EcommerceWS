package com.rakuten.ecommerce.web.entities;

import java.util.List;
/**
 * @author Kshitiz Garg
 */
public class BulkCategoryResponse {

	private boolean hasMore;
	private long nextCategoryId = -1;
	private int count;
	private List<CategoryResponse> items;
	public boolean isHasMore() {
		return hasMore;
	}
	public void setHasMore(boolean hasMore) {
		this.hasMore = hasMore;
	}

	public List<CategoryResponse> getItems() {
		return items;
	}
	public void setItems(List<CategoryResponse> items) {
		this.items = items;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getCount() {
		return count;
	}
	public long getNextCategoryId() {
		return nextCategoryId;
	}
	public void setNextCategoryId(long nextCategoryId) {
		this.nextCategoryId = nextCategoryId;
	}
	
}
