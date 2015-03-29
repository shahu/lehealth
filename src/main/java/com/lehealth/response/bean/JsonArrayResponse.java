package com.lehealth.response.bean;

import com.lehealth.data.type.ErrorCodeType;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JsonArrayResponse extends BaseResponse {
	
	public JsonArrayResponse(ErrorCodeType type, JSONArray resultArr) {
		super(type);
		this.resultArr = resultArr;
	}
	
	private JSONArray resultArr;
	
	public JSONObject toJson(){
		JSONObject result = super.toJson();
		result.accumulate("result", resultArr);
		return result;
	}
}
