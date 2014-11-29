package com.lehealth.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.lehealth.util.Constant;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class BloodpressureResult {
	
	private List<BloodpressureRecord> records=new ArrayList<BloodpressureRecord>();
	private BloodpressureConfig config=new BloodpressureConfig();
	private int days=7;
	
	public int getStatus() {
		for(BloodpressureRecord record:records){
			if(record.getDbp()>=config.getDbp2()
				||record.getSbp()>=config.getSbp2()
				||record.getHeartrate()>=config.getHeartrate2()){
				return 3;
			}else if(record.getDbp()<=config.getDbp1()
				||record.getSbp()<=config.getSbp1()
				||record.getHeartrate()<=config.getHeartrate1()){
				return 1;
			}
		}
		return 2;
	}
	
	public void setConfig(BloodpressureConfig config) {
		this.config = config;
	}
	public void setRecords(List<BloodpressureRecord> records) {
		this.records = records;
	}
	public void setDays(int days) {
		this.days = days;
	}

	public JSONObject toJsonObj(){
		JSONObject obj=new JSONObject();
		if(records!=null&&!records.isEmpty()){
			obj.accumulate("status", getStatus());
			
			Map<String,BloodpressureRecord> bpTemp=new HashMap<String, BloodpressureRecord>();
			for(BloodpressureRecord record:records){
				bpTemp.put(DateFormatUtils.format(new Date(record.getDate()), Constant.dateFormat_yyyy_mm_dd),record);
			}
			Date today=new Date();
			Date tempDate=DateUtils.addDays(today, -days);
			JSONArray bpArr=new JSONArray();
			while(tempDate.before(today)){
				String temp=DateFormatUtils.format(tempDate, Constant.dateFormat_yyyy_mm_dd);
				if(bpTemp.containsKey(temp)){
					bpArr.add(bpTemp.get(temp).toJsonObj());
				}
				tempDate=DateUtils.addDays(tempDate, 1);
			}
			obj.accumulate("records", bpArr);
		}
		return obj;
	}
}
