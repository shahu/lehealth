package com.lehealth.response.bean;

import com.lehealth.data.type.ErrorCodeType;

import net.sf.json.JSONObject;

public class JsonObjectResponse extends BaseResponse {
	
	public JsonObjectResponse(ErrorCodeType type, JSONObject resultObj) {
		super(type);
		this.resultObj = resultObj;
	}
	
	private JSONObject resultObj;
	
	public JSONObject toJson(){
		JSONObject result = super.toJson();
		result.accumulate("result", resultObj);
		return result;
	}
}
