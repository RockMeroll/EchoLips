package com.test.echolips.bean;


public class SaveDate {

	private String previousText;//前一步 步骤
	private String img;//当前步骤的图片
	private String currentText;//当前步骤
	private String nextText;//下一步 步骤
	public String getPreviousText() {
		return previousText;
	}
	public void setPreviousText(String previousText) {
		this.previousText = previousText;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getCurrentText() {
		return currentText;
	}
	public void setCurrentText(String currentText) {
		this.currentText = currentText;
	}
	public String getNextText() {
		return nextText;
	}
	public void setNextText(String nextText) {
		this.nextText = nextText;
	}
	public SaveDate() {
		super();
		// TODO Auto-generated constructor stub
	}
	public SaveDate(String previousText, String img, String currentText,
					String nextText) {
		super();
		this.previousText = previousText;
		this.img = img;
		this.currentText = currentText;
		this.nextText = nextText;
	}



}
