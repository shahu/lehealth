package com.lehealth.bean;

import java.util.Date;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import com.lehealth.util.Constant;

public class UserInfo {
	
	private String userId="";
	private String gender="";
	private Date birthday=new Date();
	private float height=0;
	private float weight=0;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public int getAge() {
		if(birthday==null){
			return 0;
		}
		int age=NumberUtils.toInt(DateFormatUtils.format(new Date(), Constant.dateFormat_yyyy));
		age=age-NumberUtils.toInt(DateFormatUtils.format(birthday, Constant.dateFormat_yyyy));
		if(age<0){
			return 0;
		}
		return age;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public float getHeight() {
		return height;
	}
	public void setHeight(float height) {
		this.height = height;
	}
	public float getWeight() {
		return weight;
	}
	public void setWeight(float weight) {
		this.weight = weight;
	}
	
}
