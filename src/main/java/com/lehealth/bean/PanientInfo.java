package com.lehealth.bean;

import java.util.Calendar;
import net.sf.json.JSONObject;

public class PanientInfo {
	
	private String userId="";
	private String userName="";
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
		obj.accumulate("username", userName);
		obj.accumulate("gender", gender);
		obj.accumulate("weight", weight);
		obj.accumulate("height", height);
		obj.accumulate("birthday", birthday);
		obj.accumulate("age", getAge());
		return obj;
	}
	
}
