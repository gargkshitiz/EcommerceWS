package com.rakuten.ecommerce.dao.entities;

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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.rakuten.ecommerce.dao.CategoryDao;
@Entity
@NamedQueries({
	@NamedQuery(name=CategoryDao.FETCH_CATEGORY_BY_ID, query="SELECT c FROM Category c where c.categoryId = :" + Category.CATEGORY_ID)
})
@Table(name="Category")
/**
 * @author Kshitiz Garg
 */
public class Category {

	public static final String CATEGORY_ID = "categoryId";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CategoryId", unique = true, nullable = false)
	private long categoryId;

	@Access(AccessType.FIELD)
	@Column(name = "CategoryDesc", nullable = false)
	private String categoryDesc;

	@Access(AccessType.FIELD)
	@Column(name = "CategoryName", nullable = false)
	private String categoryName;
	
	@Access(AccessType.FIELD)
	@ManyToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name="Product_Category", joinColumns={@JoinColumn(name="CategoryId")}, inverseJoinColumns={@JoinColumn(name="ProductId")})
	private Set<Product> products ;
	
	public long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryDesc() {
		return categoryDesc;
	}

	public void setCategoryDesc(String categoryDesc) {
		this.categoryDesc = categoryDesc;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Set<Product> getProducts() {
		return products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}

}