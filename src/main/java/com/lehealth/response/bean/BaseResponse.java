package com.lehealth.response.bean;

import net.sf.json.JSONObject;

import com.lehealth.data.type.ErrorCodeType;

public class BaseResponse {
	
	private ErrorCodeType type;
	private String message;
	
	public BaseResponse(ErrorCodeType type) {
		this.type = type;
		this.message = type.getMessage();
	}
	
	public BaseResponse(ErrorCodeType type, String message) {
		this.type = type;
		this.message = message;
	}
	
	public JSONObject toJson(){
		JSONObject result = new JSONObject();
		result.accumulate("errorcode", this.type.getCode());
		result.accumulate("errormsg", this.message);
		return result;
	}
}
