package com.test.echolips.bean;

import java.io.Serializable;

public class EcCookingredient implements Serializable{
	
    private Integer id;

    private Integer cid;

    private Integer uid;

    private Float num;

    private String ingredientname;

    private String companyname;
    
    
	public EcCookingredient() {
		super();
	}

	public EcCookingredient(Integer id, Integer cid, Integer uid, Float num,
			String ingredientname, String companyname) {
		super();
		this.id = id;
		this.cid = cid;
		this.uid = uid;
		this.num = num;
		this.ingredientname = ingredientname;
		this.companyname = companyname;
	}

	public EcCookingredient(String ingredientname, String companyname, float num) {
		super();
		this.ingredientname = ingredientname;
		this.companyname = companyname;
		this.num = num;
	}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Float getNum() {
        return num;
    }

    public void setNum(Float num) {
        this.num = num;
    }

    public String getIngredientname() {
        return ingredientname;
    }

    public void setIngredientname(String ingredientname) {
        this.ingredientname = ingredientname == null ? null : ingredientname.trim();
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname == null ? null : companyname.trim();
    }
}