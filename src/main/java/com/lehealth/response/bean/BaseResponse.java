package com.lehealth.response.bean;

import net.sf.json.JSONObject;

import com.lehealth.data.type.ErrorCodeType;

public class BaseResponse {
	
	private ErrorCodeType type=ErrorCodeType.normal;
	
	public BaseResponse(ErrorCodeType type) {
		this.type = type;
	}
	
	public JSONObject toJson(){
		JSONObject result = new JSONObject();
		result.accumulate("errorcode", type.getCode());
		result.accumulate("errormsg", type.getMessage());
		return result;
	}
}
