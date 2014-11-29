package com.lehealth.sync.entity;

import net.sf.json.JSONObject;

public class YundfLogin {

	private String phoneNumber="";
	private String password="";
	private int atype=0;
	
	private String uid="";
	private String token="";
	
	private String accid="";
	private int index=0;
	private int count=50;
	private boolean director=false;
	
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setAtype(int atype) {
		this.atype = atype;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public void setAccid(String accid) {
		this.accid = accid;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public void setDirector(boolean director) {
		this.director = director;
	}
	public JSONObject getLogin(){
		JSONObject obj=new JSONObject();
		obj.accumulate("phone", phoneNumber);
		obj.accumulate("password", password);
		obj.accumulate("atype", atype);
		return obj;
	}
	
	public JSONObject getFriendsRequest(){
		JSONObject obj=new JSONObject();
		obj.accumulate("token", token);
		obj.accumulate("uid", uid);
		return obj;
	}
	
	public JSONObject getRecordsRequest(){
		JSONObject obj=new JSONObject();
		obj.accumulate("token", token);
		obj.accumulate("uid", uid);
		obj.accumulate("acc_id", accid);
		obj.accumulate("index", index);
		obj.accumulate("count", count);
		obj.accumulate("director", director);
		return obj;
	}
}
