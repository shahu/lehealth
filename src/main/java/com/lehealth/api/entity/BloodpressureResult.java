package com.lehealth.api.entity;

import java.util.ArrayList;
import java.util.List;

import com.lehealth.common.util.CheckStatusUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class BloodpressureResult {
	
	private List<BloodpressureRecord> records=new ArrayList<BloodpressureRecord>();
	private BloodpressureConfig config=new BloodpressureConfig();
	
	public int getStatus() {
		return CheckStatusUtils.bloodpress(records, config).getCode();
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
