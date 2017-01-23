package com.rakuten.ecommerce.dao.entities;

import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn ;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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

	static final String PRODUCT_ID_COL = "ProductId";
	
	public static final String PRODUCT_ID = "productId";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = PRODUCT_ID_COL, unique = true, nullable = false)
	private long productId;

	@Access(AccessType.FIELD)
	@Column(name = "ProductDesc", nullable = false)
	private String productDesc;

	@Access(AccessType.FIELD)
	@Column(name = "ProductCurrency", nullable = false)
	private String productCurrency;
	
	@Access(AccessType.FIELD)
	@Column(name = "Price", nullable = false)
	private String price;
	
	@Access(AccessType.FIELD)
	@ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name=Category.PRODUCT_CATEGORY_TABLE, joinColumns={@JoinColumn(name=PRODUCT_ID_COL)}, inverseJoinColumns={@JoinColumn(name=Category.CATEGORY_ID_COL)})
    private Set<Category> catgeories;
	
	@Access(AccessType.FIELD)
	@Column(name = "UnitsInStock", nullable = false)
	private long unitsInStock;
	
	@Access(AccessType.FIELD)
	@Column(name = "ProductCode", nullable = false)
    private String productCode;
	
	@Access(AccessType.FIELD)
	@Column(name = "ProductType", nullable = false)
    private String productType;

	@Access(AccessType.FIELD)
	@Column(name = "LastModifiedAt", nullable = false)
	private Timestamp lastModifiedAt;
	
	@Access(AccessType.FIELD)
	@Column(name = "CreatedAt", nullable = false)
	private Timestamp createdAt;
	
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

	public String getProductCurrency() {
		return productCurrency;
	}

	public void setProductCurrency(String productCurrency) {
		this.productCurrency = productCurrency;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public long getUnitsInStock() {
		return unitsInStock;
	}

	public void setUnitsInStock(long unitsInStock) {
		this.unitsInStock = unitsInStock;
	}

	public Set<Category> getCatgeories() {
		return catgeories;
	}

	public void setCatgeories(Set<Category> catgeories) {
		this.catgeories = catgeories;
	}

	public Timestamp getLastModifiedAt() {
		return lastModifiedAt;
	}

	public void setLastModifiedAt(Timestamp lastModifiedAt) {
		this.lastModifiedAt = lastModifiedAt;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

}