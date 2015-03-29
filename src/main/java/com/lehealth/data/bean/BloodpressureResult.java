package com.lehealth.data.bean;

import java.util.ArrayList;
import java.util.List;

import com.lehealth.data.type.BloodPressStatusType;
import com.lehealth.util.Constant;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class BloodpressureResult {
	
	private List<BloodpressureRecord> records=new ArrayList<BloodpressureRecord>();
	private BloodpressureConfig config=new BloodpressureConfig();
	
	public int getStatus() {
		if(records != null && !records.isEmpty()){
			for(BloodpressureRecord record : records){
				BloodPressStatusType statusCode = Constant.getBpStatus(record.getSbp(), record.getDbp(), record.getHeartrate(), config);
				if(statusCode != BloodPressStatusType.normal){
					return statusCode.getCode();
				}
			}
		}
		return BloodPressStatusType.normal.getCode();
	}
	
	public void setConfig(BloodpressureConfig config) {
		this.config = config;
	}
	public void setRecords(List<BloodpressureRecord> records) {
		this.records = records;
	}

	public JSONObject toJsonObj(){
		JSONObject obj=new JSONObject();
		if(records!=null&&!records.isEmpty()){
			obj.accumulate("status", getStatus());
			
			JSONArray bpArr=new JSONArray();
			for(BloodpressureRecord record:records){
				bpArr.add(record.toJsonObj());
			}
			
			obj.accumulate("records", bpArr);
			
		}
		return obj;
	}
}
