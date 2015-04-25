package com.lehealth.api.entity;

import java.util.Calendar;

import org.apache.commons.lang3.StringUtils;

import net.sf.json.JSONObject;

public class PanientInfo {
	
	private String userId="";
	private String userName="";
	private String phoneNumber="";
	private int gender=0;
	private long birthday=0;
	private float height=0;
	private float weight=0;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public int getAge() {
		Calendar c=Calendar.getInstance();
		int nowYear=c.get(Calendar.YEAR);
		c.setTimeInMillis(birthday);
		int birthYear=c.get(Calendar.YEAR);
		int age=nowYear-birthYear;
		if(age<=0){
			return 0;
		}
		return age;
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
	public long getBirthday() {
		return birthday;
	}
	public void setBirthday(long birthday) {
		this.birthday = birthday;
	}
	
	public JSONObject toJsonObj(){
		JSONObject obj=new JSONObject();
		obj.accumulate("username", this.userName);
		obj.accumulate("gender", this.gender);
		obj.accumulate("weight", this.weight);
		obj.accumulate("height", this.height);
		obj.accumulate("birthday", this.birthday);
		obj.accumulate("age", getAge());
		return obj;
	}
	
	public JSONObject toBaseJsonObj(){
		JSONObject obj=new JSONObject();
		if(StringUtils.isNotBlank(this.userName)){
			obj.accumulate("username", this.userName);
		}else if(StringUtils.isNotBlank(this.phoneNumber)){
			obj.accumulate("username", this.phoneNumber);
		}
		obj.accumulate("userid", this.userId);
		return obj;
	}
}
