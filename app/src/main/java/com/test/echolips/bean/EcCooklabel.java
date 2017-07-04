package com.test.echolips.bean;

import java.io.Serializable;

public class EcCooklabel implements Serializable{
    private Integer id;

    private Integer cid;

    private Integer uid;

    private String labelname;

    
    public EcCooklabel() {
		super();
	}

	public EcCooklabel(Integer id, Integer cid, Integer uid, String labelname) {
		super();
		this.id = id;
		this.cid = cid;
		this.uid = uid;
		this.labelname = labelname;
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

    public String getLabelname() {
        return labelname;
    }

    public void setLabelname(String labelname) {
        this.labelname = labelname == null ? null : labelname.trim();
    }
}