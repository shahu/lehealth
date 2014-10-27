package com.lehealth.bean;

import com.lehealth.type.ErrorCodeType;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ResponseBean {
	
	private ErrorCodeType type=ErrorCodeType.normal;
	private JSONObject resultObj=null;
	private JSONArray resultArr=null;
	
	public void setType(ErrorCodeType type) {
		this.type = type;
	}
	public int getErrorcode(){
		return type.getCode();
	}
	public String getErrormsg(){
		return type.getMessage();
	}
	public Object getResult() {
		if(resultObj!=null){
			return resultObj;
		}else if(resultArr!=null){
			return resultArr;
		}else{
			return "";
		}
	}
	public void setResult(JSONObject result) {
		this.resultObj = result;
	}
	public void setResult(JSONArray result) {
		this.resultArr = result;
	}
}
