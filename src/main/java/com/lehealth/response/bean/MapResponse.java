package com.lehealth.response.bean;

import java.util.Map;

import net.sf.json.JSONObject;

import com.lehealth.data.type.ErrorCodeType;

public class MapResponse extends BaseResponse {

	public MapResponse(ErrorCodeType type, Map<String, String> result) {
		super(type);
		this.result = result;
	}
	
	private Map<String, String> result;
	
	public JSONObject toJson(){
		JSONObject result = super.toJson();
		result.accumulate("result", this.result);
		return result;
	}

}
