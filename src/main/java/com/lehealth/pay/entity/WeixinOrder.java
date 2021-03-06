package com.lehealth.pay.entity;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.lehealth.api.entity.GoodsInfo;

import net.sf.json.JSONObject;

public class WeixinOrder {
	
	private int id;
	private String orderId;
	private String orderSecret;
	private String prepayId;
	private String transactionId;
	
	private String userId;
	private String userName;
	private String openId;
	private String ip;
	
	private String subscribe;
	
	private GoodsInfo goodsInfo = new GoodsInfo();
	private double fee;
	private int period;
	
	private int status = -1;
	
	// 订单创建时间
	private Date createTime;
	// 订单交易时间
	private Date startTime;
	private Date expireTime;
	// 订单支付时间
	private Date payTime;
	private Date callbackTime;
	// 订单关闭时间
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
	public String getOrderSecret() {
		return orderSecret;
	}
	public void setOrderSecret(String orderSecret) {
		this.orderSecret = orderSecret;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
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
	public String getPrepayId() {
		return prepayId;
	}
	public void setPrepayId(String prepayId) {
		this.prepayId = prepayId;
	}
	public String getSubscribe() {
		return subscribe;
	}
	public void setSubscribe(String subscribe) {
		this.subscribe = subscribe;
	}
	public GoodsInfo getGoodsInfo() {
		return goodsInfo;
	}
	public void setGoodsInfo(GoodsInfo goodsInfo) {
		this.goodsInfo = goodsInfo;
	}
	public double getFee() {
		return fee;
	}
	public void setFee(double fee) {
		this.fee = fee;
	}
	public int getPeriod() {
		return period;
	}
	public void setPeriod(int period) {
		this.period = period;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = new Date(createTime);
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = new Date(startTime);
	}
	public Date getExpireTime() {
		return expireTime;
	}
	public void setExpireTime(long expireTime) {
		this.expireTime = new Date(expireTime);
	}
	public Date getCallbackTime() {
		return callbackTime;
	}
	public void setCallbackTime(long callbackTime) {
		this.callbackTime = new Date(callbackTime);
	}
	public Date getPayTime() {
		return payTime;
	}
	public void setPayTime(long payTime) {
		this.payTime = new Date(payTime);
	}
	public Date getCloseTime() {
		return closeTime;
	}
	public void setCloseTime(long closeTime) {
		this.closeTime = new Date(closeTime);
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public JSONObject toJsonObj(){
		JSONObject obj=new JSONObject();
		obj.accumulate("orderid", StringUtils.trimToEmpty(this.orderId));
		obj.accumulate("fee", this.fee);
		obj.accumulate("status", this.status);
		if(this.createTime != null){
			obj.accumulate("createtime", this.createTime.getTime());
		}
		obj.accumulate("goodsname", StringUtils.trimToEmpty(this.goodsInfo.getName()));
		obj.accumulate("goodsinfo", StringUtils.trimToEmpty(this.goodsInfo.getInfo()));
		obj.accumulate("goodsdetail", StringUtils.trimToEmpty(this.goodsInfo.getDetail()));
		return obj;
	}
	
	public JSONObject toBackendJsonObj(){
		JSONObject obj=new JSONObject();
		obj.accumulate("orderid", StringUtils.trimToEmpty(this.orderId));
		obj.accumulate("transactionid", StringUtils.trimToEmpty(this.transactionId));
		obj.accumulate("userid", StringUtils.trimToEmpty(this.userId));
		obj.accumulate("openid", StringUtils.trimToEmpty(this.openId));
		obj.accumulate("subscribe", StringUtils.trimToEmpty(this.subscribe));
		obj.accumulate("fee", this.fee);
		obj.accumulate("period", this.period);
		obj.accumulate("status", this.status);
		
		if(this.createTime != null){
			obj.accumulate("createtime", this.createTime.getTime());
		}
		if(this.startTime != null){
			obj.accumulate("starttime", this.startTime.getTime());
		}
		if(this.expireTime != null){
			obj.accumulate("expiretime", this.expireTime.getTime());
		}
		if(this.payTime != null){
			obj.accumulate("paytime", this.payTime.getTime());
		}
		if(this.callbackTime != null){
			obj.accumulate("callbacktime", this.callbackTime.getTime());
		}
		if(this.closeTime != null){
			obj.accumulate("closetime", this.closeTime.getTime());
		}
		
		obj.accumulate("goodsname", StringUtils.trimToEmpty(this.goodsInfo.getName()));
		obj.accumulate("goodsinfo", StringUtils.trimToEmpty(this.goodsInfo.getInfo()));
		obj.accumulate("goodsdetail", StringUtils.trimToEmpty(this.goodsInfo.getDetail()));
		
		obj.accumulate("username", StringUtils.trimToEmpty(this.userName));
		return obj;
	}
}
