package com.rakuten.ecommerce.web.entities;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gson.Gson;

public class GenerateJson {

	public static void main(String[] args) {
		ProductDetails p = new ProductDetails();
		p.setPrice("10000");
		p.setProductCurrency("INR");
		p.setProductCode("pamp_diap");
		p.setProductDesc("Pamper Active Baby Diaper");
		p.setProductType("Diaper");
		p.setUnitsInStock(2);
		CategoryDetails cd1 = new CategoryDetails();
		cd1.setCategoryDesc("Baby Diapers");
		cd1.setCategoryName("Baby Diapers");
		CategoryDetails cd2 = new CategoryDetails();
		cd2.setCategoryDesc("Hygiene");
		cd2.setCategoryName("Hygiene");
		Set<CategoryDetails> cSet = new HashSet<>();
		cSet.add(cd1);
		cSet.add(cd2);
		p.setCatgeories(cSet);
		System.out.println(new Gson().toJson(p));
	}

}
