package com.lehealth.bean;

import net.sf.json.JSONObject;

public class Doctor {
	
	private int id=0;
	private String name="";
	private int gender=0;
	private String title="";
	//TODO 医院id
	private String hospital="";
	private String desc="";
	private String thumbnail="";
	private String image="";
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	
	public JSONObject toJsonObj(){
		JSONObject obj=new JSONObject();
		obj.accumulate("id", id);
		obj.accumulate("name", name);
		obj.accumulate("gender", gender);
		obj.accumulate("title", title);
		obj.accumulate("hospital", hospital);
		obj.accumulate("desc", desc);
		obj.accumulate("thumbnail", thumbnail);
		obj.accumulate("image", image);
		return obj;
	}
}
