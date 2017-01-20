package com.rakuten.ecommerce.dao.entities;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.rakuten.ecommerce.dao.ProductDao;
/**
 * @author Kshitiz Garg
 */
@Entity
@NamedQueries({
	@NamedQuery(name=ProductDao.FETCH_PRODUCT_BY_ID, query="SELECT p FROM Product p where p.productId = :" + Product.PRODUCT_ID)
})
@Table(name="Product")

public class Product {

	public static final String PRODUCT_ID = "productId";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ProductId", unique = true, nullable = false)
	private long productId;

	@Access(AccessType.FIELD)
	@Column(name = "ProductDesc", nullable = false)
	private String productDesc;

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public String getProductDesc() {
		return productDesc;
	}

	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}

}