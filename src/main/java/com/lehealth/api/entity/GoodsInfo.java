package com.lehealth.api.entity;

import com.lehealth.data.type.GoodsStatusType;

import net.sf.json.JSONObject;

public class GoodsInfo {
	
	private int id=0;
	private String name;
	private String info;
	private String detail;
	private String feeType;
	private String[] images;
	private double fee;
	private GoodsStatusType status = GoodsStatusType.offline;
	
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
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String[] getImages() {
		return images;
	}
	public void setImages(String[] images) {
		this.images = images;
	}
	public String getFeeType() {
		return feeType;
	}
	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}
	public double getFee() {
		return fee;
	}
	public void setFee(double fee) {
		this.fee = fee;
	}
	public GoodsStatusType getStatus() {
		return status;
	}
	public void setStatus(GoodsStatusType status) {
		this.status = status;
	}
	
	public JSONObject toJsonObj(){
		JSONObject obj=new JSONObject();
		obj.accumulate("id", this.id);
		obj.accumulate("name", this.name);
		obj.accumulate("info", this.info);
		obj.accumulate("detail", this.detail);
		obj.accumulate("images", this.images);
		obj.accumulate("fee_type", this.feeType);
		obj.accumulate("fee", this.fee);
		return obj;
	}
}
