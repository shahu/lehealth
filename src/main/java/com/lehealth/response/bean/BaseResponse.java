package com.lehealth.response.bean;

import net.sf.json.JSONObject;

import com.lehealth.data.type.ErrorCodeType;

public class BaseResponse {
	
	private ErrorCodeType type;
	
	public BaseResponse(ErrorCodeType type) {
		this.type = type;
	}
	
	
	public JSONObject toJson(){
		JSONObject result = new JSONObject();
		result.accumulate("errorcode", this.type.getCode());
		result.accumulate("errormsg", this.type.getMessage());
		return result;
	}
}
