package com.rakuten.ecommerce.web.entities;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

// TODO delete me
public class GenerateJson {

	public static void main(String[] args) {
		createproduct();
		System.out.println("***********************");
		createCategories();
	}

	private static void createproduct() {
		ProductFromWeb p = new ProductFromWeb();
		p.setPrice("10000");
		p.setProductCurrency("INR");
		p.setProductCode("pamp_diap");
		p.setProductDesc("Pamper Active Baby Diaper");
		p.setProductType("Diaper");
		p.setUnitsInStock(2);
		List<Long> cList = new ArrayList<>();
		cList.add(1l);
		cList.add(2l);
		p.setCatgeoryIds(cList);
		System.out.println(new Gson().toJson(p));
	}

	private static void createCategories() {
		CategoryFromWeb cd1 = new CategoryFromWeb();
		cd1.setCategoryDesc("Baby Diapers");
		cd1.setCategoryName("Baby Diapers");
		CategoryFromWeb cd2 = new CategoryFromWeb();
		cd2.setCategoryDesc("Hygiene");
		cd2.setCategoryName("Hygiene");
		CategoryFromWeb cd3 = new CategoryFromWeb();
		cd3.setCategoryDesc("Men Hygiene");
		cd3.setCategoryName("Men Hygiene");
		cd3.setParentCategoryId(2);
		System.out.println(new Gson().toJson(cd1));
		System.out.println(new Gson().toJson(cd2));
		System.out.println(new Gson().toJson(cd3));
	}

}
