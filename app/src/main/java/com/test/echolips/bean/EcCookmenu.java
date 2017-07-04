package com.test.echolips.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class EcCookmenu implements Serializable{
	/*
    菜谱类：
    菜谱的格式
    数据存入菜谱实例，在listView显示
     */
	private String introduction;

	//用于匹配json

	private Integer id;
	private String cookname;
	private String imgsrc;
	private Integer uid;
	private String mealsnumber;
	private Integer steps;
	private String cookingpoint;
	private Integer score;
	private String valid;
	private String name;
	private String time;
	private String hard;


	public EcCookmenu() {
		super();
	}

	public EcCookmenu(String introduction, Integer id, String cookname, String imgsrc, Integer uid, String mealsnumber, Integer steps, String cookingpoint, Integer score, String valid, String name, String time, String hard) {
		this.introduction = introduction;
		this.id = id;
		this.cookname = cookname;
		this.imgsrc = imgsrc;
		this.uid = uid;
		this.mealsnumber = mealsnumber;
		this.steps = steps;
		this.cookingpoint = cookingpoint;
		this.score = score;
		this.valid = valid;
		this.name = name;
		this.time = time;
		this.hard = hard;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getHard() {
		return hard;
	}

	public void setHard(String hard) {
		this.hard = hard;
	}


	//用于本地上传
	private String menuName;
	private String imgLocalUrl;
	private String imgNetUrl;
	private String mealsNum;
	private String mealsHard;
	private String mealsTime;
	private String cookPoint;
	private ArrayList<String> labels;

	public EcCookmenu(String menuName, String imgLocalUrl, String imgNetUrl,
				String introduction, String mealsNum, String mealsHard,
				String mealsTime, String cookPoint, ArrayList<String> labels) {
		super();
		this.menuName = menuName;
		this.imgLocalUrl = imgLocalUrl;
		this.imgNetUrl = imgNetUrl;
		this.introduction = introduction;
		this.mealsNum = mealsNum;
		this.mealsHard = mealsHard;
		this.mealsTime = mealsTime;
		this.cookPoint = cookPoint;
		this.labels = labels;
	}

	public EcCookmenu(Integer id, String cookname, String imgsrc, Integer uid,
				String introduction, String mealsnumber, Integer steps,
				String cookingpoint, Integer score, String valid) {
		super();
		this.id = id;
		this.cookname = cookname;
		this.imgsrc = imgsrc;
		this.uid = uid;
		this.introduction = introduction;
		this.mealsnumber = mealsnumber;
		this.steps = steps;
		this.cookingpoint = cookingpoint;
		this.score = score;
		this.valid = valid;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCookname() {
		return cookname;
	}

	public void setCookname(String cookname) {
		this.cookname = cookname;
	}

	public String getImgsrc() {
		return imgsrc;
	}

	public void setImgsrc(String imgsrc) {
		this.imgsrc = imgsrc;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}



	public String getMealsnumber() {
		return mealsnumber;
	}

	public void setMealsnumber(String mealsnumber) {
		this.mealsnumber = mealsnumber;
	}

	public Integer getSteps() {
		return steps;
	}

	public void setSteps(Integer steps) {
		this.steps = steps;
	}

	public String getCookingpoint() {
		return cookingpoint;
	}

	public void setCookingpoint(String cookingpoint) {
		this.cookingpoint = cookingpoint;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public String getValid() {
		return valid;
	}

	public void setValid(String valid) {
		this.valid = valid;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getImgLocalUrl() {
		return imgLocalUrl;
	}

	public void setImgLocalUrl(String imgLocalUrl) {
		this.imgLocalUrl = imgLocalUrl;
	}

	public String getImgNetUrl() {
		return imgNetUrl;
	}

	public void setImgNetUrl(String imgNetUrl) {
		this.imgNetUrl = imgNetUrl;
	}

	public String getMealsNum() {
		return mealsNum;
	}

	public void setMealsNum(String mealsNum) {
		this.mealsNum = mealsNum;
	}

	public String getMealsHard() {
		return mealsHard;
	}

	public void setMealsHard(String mealsHard) {
		this.mealsHard = mealsHard;
	}

	public String getMealsTime() {
		return mealsTime;
	}

	public void setMealsTime(String mealsTime) {
		this.mealsTime = mealsTime;
	}

	public String getCookPoint() {
		return cookPoint;
	}

	public void setCookPoint(String cookPoint) {
		this.cookPoint = cookPoint;
	}

	public ArrayList<String> getLabels() {
		return labels;
	}

	public void setLabels(ArrayList<String> labels) {
		this.labels = labels;
	}

	@Override
	public String toString() {
		return "EcCookmenu [introduction=" + introduction + ", id=" + id
				+ ", cookname=" + cookname + ", imgsrc=" + imgsrc + ", uid="
				+ uid + ", mealsnumber=" + mealsnumber + ", steps=" + steps
				+ ", cookingpoint=" + cookingpoint + ", score=" + score
				+ ", valid=" + valid + ", menuName=" + menuName
				+ ", imgLocalUrl=" + imgLocalUrl + ", imgNetUrl=" + imgNetUrl
				+ ", mealsNum=" + mealsNum + ", mealsHard=" + mealsHard
				+ ", mealsTime=" + mealsTime + ", cookPoint=" + cookPoint
				+ ", labels=" + labels + "]";
	}
}




/*


import java.io.Serializable;
import java.util.ArrayList;

public class EcCookmenu implements Serializable{
	
	private String introduction;

	private Integer id;
    private String cookname;
    private String imgsrc;
    private Integer uid;
    private String mealsnumber;
    private Integer steps;
    private String cookingpoint;
    private Integer score;
    private String valid;

	private String menuName;
	private String imgLocalUrl;
	private String imgNetUrl;
	private String mealsNum;
	private String mealsHard;
	private String mealsTime;
	private String cookPoint;
	private ArrayList<String> labels;
	
	public EcCookmenu() {
		super();
	}

	public EcCookmenu(String menuName, String imgLocalUrl, String imgNetUrl,
			String introduction, String mealsNum, String mealsHard,
			String mealsTime, String cookPoint, ArrayList<String> labels) {
		super();
		this.menuName = menuName;
		this.imgLocalUrl = imgLocalUrl;
		this.imgNetUrl = imgNetUrl;
		this.introduction = introduction;
		this.mealsNum = mealsNum;
		this.mealsHard = mealsHard;
		this.mealsTime = mealsTime;
		this.cookPoint = cookPoint;
		this.labels = labels;
	}
	
    public EcCookmenu(Integer id, String cookname, String imgsrc, Integer uid,
			String introduction, String mealsnumber, Integer steps,
			String cookingpoint, Integer score, String valid) {
		super();
		this.id = id;
		this.cookname = cookname;
		this.imgsrc = imgsrc;
		this.uid = uid;
		this.introduction = introduction;
		this.mealsnumber = mealsnumber;
		this.steps = steps;
		this.cookingpoint = cookingpoint;
		this.score = score;
		this.valid = valid;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCookname() {
		return cookname;
	}

	public void setCookname(String cookname) {
		this.cookname = cookname;
	}

	public String getImgsrc() {
		return imgsrc;
	}

	public void setImgsrc(String imgsrc) {
		this.imgsrc = imgsrc;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public String getMealsnumber() {
		return mealsnumber;
	}

	public void setMealsnumber(String mealsnumber) {
		this.mealsnumber = mealsnumber;
	}

	public Integer getSteps() {
		return steps;
	}

	public void setSteps(Integer steps) {
		this.steps = steps;
	}

	public String getCookingpoint() {
		return cookingpoint;
	}

	public void setCookingpoint(String cookingpoint) {
		this.cookingpoint = cookingpoint;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public String getValid() {
		return valid;
	}

	public void setValid(String valid) {
		this.valid = valid;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getImgLocalUrl() {
		return imgLocalUrl;
	}

	public void setImgLocalUrl(String imgLocalUrl) {
		this.imgLocalUrl = imgLocalUrl;
	}

	public String getImgNetUrl() {
		return imgNetUrl;
	}

	public void setImgNetUrl(String imgNetUrl) {
		this.imgNetUrl = imgNetUrl;
	}

	public String getMealsNum() {
		return mealsNum;
	}

	public void setMealsNum(String mealsNum) {
		this.mealsNum = mealsNum;
	}

	public String getMealsHard() {
		return mealsHard;
	}

	public void setMealsHard(String mealsHard) {
		this.mealsHard = mealsHard;
	}

	public String getMealsTime() {
		return mealsTime;
	}

	public void setMealsTime(String mealsTime) {
		this.mealsTime = mealsTime;
	}

	public String getCookPoint() {
		return cookPoint;
	}

	public void setCookPoint(String cookPoint) {
		this.cookPoint = cookPoint;
	}

	public ArrayList<String> getLabels() {
		return labels;
	}

	public void setLabels(ArrayList<String> labels) {
		this.labels = labels;
	}

	@Override
	public String toString() {
		return "EcCookmenu [introduction=" + introduction + ", id=" + id
				+ ", cookname=" + cookname + ", imgsrc=" + imgsrc + ", uid="
				+ uid + ", mealsnumber=" + mealsnumber + ", steps=" + steps
				+ ", cookingpoint=" + cookingpoint + ", score=" + score
				+ ", valid=" + valid + ", menuName=" + menuName
				+ ", imgLocalUrl=" + imgLocalUrl + ", imgNetUrl=" + imgNetUrl
				+ ", mealsNum=" + mealsNum + ", mealsHard=" + mealsHard
				+ ", mealsTime=" + mealsTime + ", cookPoint=" + cookPoint
				+ ", labels=" + labels + "]";
	}



}*/
