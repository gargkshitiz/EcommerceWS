package com.testorg.ecommerce.web.entities;

import java.util.List;
/**
 * @author Kshitiz Garg
 */
public class BulkProductResponse {

	private boolean hasMore;
	private long nextProductId = -1;
	private int count;
	private List<ProductResponse> items;
	public boolean isHasMore() {
		return hasMore;
	}
	public void setHasMore(boolean hasMore) {
		this.hasMore = hasMore;
	}
	public List<ProductResponse> getItems() {
		return items;
	}
	public void setItems(List<ProductResponse> items) {
		this.items = items;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getCount() {
		return count;
	}
	public long getNextProductId() {
		return nextProductId;
	}
	public void setNextProductId(long nextProductId) {
		this.nextProductId = nextProductId;
	}
}