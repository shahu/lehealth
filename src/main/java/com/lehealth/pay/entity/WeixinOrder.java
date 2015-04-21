package com.lehealth.pay.entity;

import java.util.Date;

import com.lehealth.api.entity.GoodsInfo;

import net.sf.json.JSONObject;

public class WeixinOrder {
	
	private int id;
	private String orderId;
	
	private String userId;
	private String openId;
	private String ip;
	
	private GoodsInfo goodsInfo;
	private String ext;
	
	private Date createTime;
	private Date payTime;
	private Date closeTime;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public GoodsInfo getGoodsInfo() {
		return goodsInfo;
	}
	public void setGoodsInfo(GoodsInfo goodsInfo) {
		this.goodsInfo = goodsInfo;
	}
	public String getExt() {
		return ext;
	}
	public void setExt(String ext) {
		this.ext = ext;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getPayTime() {
		return payTime;
	}
	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}
	public Date getCloseTime() {
		return closeTime;
	}
	public void setCloseTime(Date closeTime) {
		this.closeTime = closeTime;
	}
	
	public JSONObject toJsonObj(){
		JSONObject obj=new JSONObject();
		obj.accumulate("id", this.id);
		obj.accumulate("orderid", this.orderId);
		obj.accumulate("userid", this.userId);
		obj.accumulate("openid", this.openId);
		obj.accumulate("ip", this.ip);
		return obj;
	}
}
