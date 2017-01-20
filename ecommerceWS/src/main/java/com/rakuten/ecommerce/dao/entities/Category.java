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

import com.rakuten.ecommerce.dao.CategoryDao;
/**
 * @author Kshitiz Garg
 */
@Entity
@NamedQueries({
	@NamedQuery(name=CategoryDao.FETCH_CATEGORY_BY_ID, query="SELECT c FROM Category c where c.categoryId = :" + Category.CATEGORY_ID)
})
@Table(name="Category")

public class Category {

	public static final String CATEGORY_ID = "categoryId";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CategoryId", unique = true, nullable = false)
	private long categoryId;

	@Access(AccessType.FIELD)
	@Column(name = "CategoryDesc", nullable = false)
	private String categoryDesc;

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

}