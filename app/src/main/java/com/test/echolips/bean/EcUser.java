package com.test.echolips.bean;

import java.io.Serializable;
import java.util.Date;

public class EcUser implements Serializable{
	private Integer id;
	
	private String nickname;
	
	private String password;
	
	private String phone;
	
	private String email;
	
	private String weichat;
	
	private String qq;
	
	private String weibo;
	
	private String numbereach;
	
	private Integer score;
	
	private String photo;
	
	private String sex;
	
	private Integer age;
	
	private String hometown;
	
	private String residence;
	
	private Date birth;
	
	private String signname;
	
	private String profession;
	
	private String marriage;
	
	private Integer dianzan;
	
	private Date registerdate;
	
	private String chara;
	
	private String valid;

	public EcUser() {
		super();
	}


	public EcUser(Integer id, String nickname, String password, String phone,
			String email, String weichat, String qq, String weibo,
			String numbereach, Integer score, String photo, String sex,
			Integer age, String hometown, String residence, Date birth,
			String signname, String profession, String marriage,
			Integer dianzan, Date registerdate, String chara, String valid) {
		super();
		this.id = id;
		this.nickname = nickname;
		this.password = password;
		this.phone = phone;
		this.email = email;
		this.weichat = weichat;
		this.qq = qq;
		this.weibo = weibo;
		this.numbereach = numbereach;
		this.score = score;
		this.photo = photo;
		this.sex = sex;
		this.age = age;
		this.hometown = hometown;
		this.residence = residence;
		this.birth = birth;
		this.signname = signname;
		this.profession = profession;
		this.marriage = marriage;
		this.dianzan = dianzan;
		this.registerdate = registerdate;
		this.chara = chara;
		this.valid = valid;
	}


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getWeichat() {
        return weichat;
    }

    public void setWeichat(String weichat) {
        this.weichat = weichat == null ? null : weichat.trim();
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq == null ? null : qq.trim();
    }

    public String getWeibo() {
        return weibo;
    }

    public void setWeibo(String weibo) {
        this.weibo = weibo == null ? null : weibo.trim();
    }

    public String getNumbereach() {
        return numbereach;
    }

    public void setNumbereach(String numbereach) {
        this.numbereach = numbereach == null ? null : numbereach.trim();
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo == null ? null : photo.trim();
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown == null ? null : hometown.trim();
    }

    public String getResidence() {
        return residence;
    }

    public void setResidence(String residence) {
        this.residence = residence == null ? null : residence.trim();
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public String getSignname() {
        return signname;
    }

    public void setSignname(String signname) {
        this.signname = signname == null ? null : signname.trim();
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession == null ? null : profession.trim();
    }

    public String getMarriage() {
        return marriage;
    }

    public void setMarriage(String marriage) {
        this.marriage = marriage == null ? null : marriage.trim();
    }

    public Integer getDianzan() {
        return dianzan;
    }

    public void setDianzan(Integer dianzan) {
        this.dianzan = dianzan;
    }

    public Date getRegisterdate() {
        return registerdate;
    }

    public void setRegisterdate(Date registerdate) {
        this.registerdate = registerdate;
    }

    public String getChara() {
        return chara;
    }

    public void setChara(String chara) {
        this.chara = chara == null ? null : chara.trim();
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid == null ? null : valid.trim();
    }
}