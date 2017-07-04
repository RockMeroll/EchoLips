package com.test.echolips.bean;

import java.io.Serializable;

public class EcCookitem implements Serializable{

	private Long id;

    private Integer cid;

    private Integer uid;

    private Integer step;

    private String pictureurl;

    private String itemcontent;

    private Integer score;
	private int imgId;
	private int delId;
	private String itemImgNetUrl;
	private String imgLocalUrl;
	private String itemContent;

	
	public EcCookitem() {
		super();
	}

	public EcCookitem(Long id, Integer cid, Integer uid, Integer step,
			String pictureurl, String itemcontent, Integer score) {
		super();
		this.id = id;
		this.cid = cid;
		this.uid = uid;
		this.step = step;
		this.pictureurl = pictureurl;
		this.itemcontent = itemcontent;
		this.score = score;
	}

	public EcCookitem(int imgId, int delId, String itemImgNetUrl,
			String imgLocalUrl, String itemContent) {
		super();
		this.imgId = imgId;
		this.delId = delId;
		this.itemImgNetUrl = itemImgNetUrl;
		this.imgLocalUrl = imgLocalUrl;
		this.itemContent = itemContent;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public Integer getStep() {
		return step;
	}

	public void setStep(Integer step) {
		this.step = step;
	}

	public String getPictureurl() {
		return pictureurl;
	}

	public void setPictureurl(String pictureurl) {
		this.pictureurl = pictureurl;
	}

	public String getItemcontent() {
		return itemcontent;
	}

	public void setItemcontent(String itemcontent) {
		this.itemcontent = itemcontent;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public int getImgId() {
		return imgId;
	}

	public void setImgId(int imgId) {
		this.imgId = imgId;
	}

	public int getDelId() {
		return delId;
	}

	public void setDelId(int delId) {
		this.delId = delId;
	}

	public String getItemImgNetUrl() {
		return itemImgNetUrl;
	}

	public void setItemImgNetUrl(String itemImgNetUrl) {
		this.itemImgNetUrl = itemImgNetUrl;
	}

	public String getImgLocalUrl() {
		return imgLocalUrl;
	}

	public void setImgLocalUrl(String imgLocalUrl) {
		this.imgLocalUrl = imgLocalUrl;
	}

	public String getItemContent() {
		return itemContent;
	}

	public void setItemContent(String itemContent) {
		this.itemContent = itemContent;
	}

	@Override
	public String toString() {
		return "EcCookitem [id=" + id + ", cid=" + cid + ", uid=" + uid
				+ ", step=" + step + ", pictureurl=" + pictureurl
				+ ", itemcontent=" + itemcontent + ", score=" + score
				+ ", imgId=" + imgId + ", delId=" + delId + ", itemImgNetUrl="
				+ itemImgNetUrl + ", imgLocalUrl=" + imgLocalUrl
				+ ", itemContent=" + itemContent + "]";
	}
	
}