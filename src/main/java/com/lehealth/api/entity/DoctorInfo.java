package com.lehealth.api.entity;

import net.sf.json.JSONObject;

public class DoctorInfo {
	
	private String id="";
	private String userId="";
	private String name="";
	private int gender=0;
	private String title="";
	private String hospital="";
	private String desc="";
	private String thumbnail="";
	private String image="";
	private int attention=0;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getHospital() {
		return hospital;
	}
	public void setHospital(String hospital) {
		this.hospital = hospital;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public int getAttention() {
		return attention;
	}
	public void setAttention(int attention) {
		this.attention = attention;
	}
	
	public JSONObject toJsonObj(){
		JSONObject obj=new JSONObject();
		obj.accumulate("id", userId);
		obj.accumulate("name", name);
		obj.accumulate("gender", gender);
		obj.accumulate("title", title);
		obj.accumulate("hospital", hospital);
		obj.accumulate("desc", desc);
		obj.accumulate("thumbnail", thumbnail);
		obj.accumulate("image", image);
		obj.accumulate("attention", attention);
		return obj;
	}
}
