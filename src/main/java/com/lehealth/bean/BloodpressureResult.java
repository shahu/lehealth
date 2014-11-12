package com.lehealth.bean;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class BloodpressureResult {
	
	private List<BloodpressureInfo> records=new ArrayList<BloodpressureInfo>();
	private BloodpressureConfig config=new BloodpressureConfig();
	
	public int getStatus() {
		for(BloodpressureInfo record:records){
			if(!(record.getDbp()>=config.getDbp1()
					&&record.getDbp()<=config.getDbp2()
					&&record.getSbp()>=config.getSbp1()
					&&record.getSbp()<=config.getSbp2()
					&&record.getHeartrate()>=config.getHeartrate1()
					&&record.getHeartrate()<=config.getHeartrate2())){
				return 1;
			}
		}
		return 0;
	}
	public void setConfig(BloodpressureConfig config) {
		this.config = config;
	}
	public void setRecords(List<BloodpressureInfo> records) {
		this.records = records;
	}
	public JSONObject toJsonObj(){
		JSONObject obj=new JSONObject();
		if(records!=null&&!records.isEmpty()){
			obj.accumulate("status", getStatus());
			JSONArray arr=new JSONArray();
			for(BloodpressureInfo record:records){
				arr.add(record.toJsonObj());
			}
			obj.accumulate("records", arr);
		}
		return obj;
	}
}
