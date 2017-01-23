package com.rakuten.ecommerce.dao.entities;

import java.math.BigDecimal;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.JoinColumn ;
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

	@Access(AccessType.FIELD)
	@Column(name = "ProductCurrency", nullable = false)
	private String productCurrency;
	
	@Access(AccessType.FIELD)
	@Column(name = "Price", nullable = false)
	private String price;
	
	@Access(AccessType.FIELD)
	@ManyToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name="Product_Category", joinColumns={@JoinColumn(name="ProductId")}, inverseJoinColumns={@JoinColumn(name="CategoryId")})
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

	/**priceInDesiredCurrency and priceInEuro fields are just for retrieval purpose
	 * and NOT to be persisted at all, hence are marked transient*/
	@Transient
	private transient BigDecimal priceInDesiredCurrency;
	
	@Transient
	private transient BigDecimal priceInEuro;
	
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

	public BigDecimal getPriceInDesiredCurrency() {
		return priceInDesiredCurrency;
	}

	public void setPriceInDesiredCurrency(BigDecimal priceInDesiredCurrency) {
		this.priceInDesiredCurrency = priceInDesiredCurrency;
	}

	public BigDecimal getPriceInEuro() {
		return priceInEuro;
	}

	public void setPriceInEuro(BigDecimal priceInEuro) {
		this.priceInEuro = priceInEuro;
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

}