package com.lehealth.data.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.lehealth.util.Constant;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class HomeResult{
	private List<BloodpressureRecord> bpRecords ;
	private BloodpressureConfig bpConfig ;
	private List<MedicineRecord> medicineRecords ;
	private List<PanientInfo> guardedInfos ;
	private PanientInfo info ;
	private int days=7;

	public HomeResult(List<BloodpressureRecord> bpRecords,
			BloodpressureConfig bpConfig,
			List<MedicineRecord> medicineRecords,
			int days,
			PanientInfo info){
		this.setCommon(bpRecords, bpConfig, medicineRecords, days, info);
	}
	
	public HomeResult(List<BloodpressureRecord> bpRecords,
			BloodpressureConfig bpConfig,
			List<MedicineRecord> medicineRecords,
			int days,
			List<PanientInfo> guardedInfos,
			PanientInfo info){
		this.setCommon(bpRecords, bpConfig, medicineRecords, days, info);
		if(guardedInfos !=null && !guardedInfos.isEmpty()){
			this.guardedInfos = guardedInfos;
		}
	}
	
	private void setCommon(List<BloodpressureRecord> bpRecords,
			BloodpressureConfig bpConfig,
			List<MedicineRecord> medicineRecords,
			int days,
			PanientInfo info){
		if(bpRecords !=null && !bpRecords.isEmpty()){
			this.bpRecords = bpRecords;
		}
		if(bpConfig !=null){
			this.bpConfig = bpConfig;
		}
		if(medicineRecords !=null && !medicineRecords.isEmpty()){
			this.medicineRecords = medicineRecords;
		}
		this.days = days;
		if(info !=null){
			this.info = info;
		}
	}
	
	public JSONObject toJsonObj(){
		JSONObject obj = new JSONObject();
		
		Map<String,List<BloodpressureRecord>> bpTemp = new HashMap<String,List<BloodpressureRecord>>();
		if(this.bpRecords != null && !this.bpRecords.isEmpty()){
			for(BloodpressureRecord record : this.bpRecords){
				Date date = new Date(record.getDate());
				String key = DateFormatUtils.format(date, Constant.dateFormat_yyyy_mm_dd);
				int hour = NumberUtils.toInt(DateFormatUtils.format(date, Constant.dateFormat_hh));
				//TODO 测试需要
				//if(hour >= 4 && hour <= 10){
					if(!bpTemp.containsKey(key)){
						bpTemp.put(key, new ArrayList<BloodpressureRecord>());
					}
					bpTemp.get(key).add(record);
				//}
			}
			
			Date today = new Date();
			Date tempDate = DateUtils.addDays(today, -this.days);
			JSONArray bpArr = new JSONArray();
			while(!tempDate.after(today)){
				String tempKey = DateFormatUtils.format(tempDate, Constant.dateFormat_yyyy_mm_dd);
				if(bpTemp.containsKey(tempKey)){
					List<BloodpressureRecord> list = bpTemp.get(tempKey);
					if(list.size() == 1){
						bpArr.add(list.get(0).toJsonObj());
					}else if(list.size() >= 2){
						BloodpressureRecord tempRecord = new BloodpressureRecord();
						tempRecord.setDbp((list.get(list.size()-1).getDbp() + list.get(list.size()-2).getDbp())/2);
						tempRecord.setSbp((list.get(list.size()-1).getSbp() + list.get(list.size()-2).getSbp())/2);
						tempRecord.setDate(list.get(0).getDate());
						tempRecord.setHeartrate((list.get(list.size()-1).getHeartrate() + list.get(list.size()-2).getHeartrate())/2);
						bpArr.add(tempRecord.toJsonObj());
					}
				}
				tempDate = DateUtils.addDays(tempDate, 1);
			}
			
			if(bpArr.size() > 0){
				obj.accumulate("status", this.getStatus());
				obj.accumulate("records", bpArr);
			}
		}
		
		if(this.medicineRecords != null && !this.medicineRecords.isEmpty()){
			JSONArray mArr = new JSONArray();
			for(MedicineRecord record : medicineRecords){
				mArr.add(record.toHomeJsonObj());
			}
			if(mArr.size() > 0){
				obj.accumulate("history", mArr);
			}
		}
		
		if(this.guardedInfos != null && !this.guardedInfos.isEmpty()){
			JSONArray gArr = new JSONArray();
			for(PanientInfo guarded : guardedInfos){
				gArr.add(guarded.toBaseJsonObj());
			}
			if(gArr.size() > 0){
				obj.accumulate("guardeds", gArr);
			}
		}
		
		if(this.info != null && StringUtils.isNotBlank(this.info.getUserId())){
			obj.accumulate("info", this.info.toJsonObj());
		}
		
		return obj;
	}
	
	public int getStatus() {
		if(bpRecords != null && !bpRecords.isEmpty() && bpConfig != null){
			for(BloodpressureRecord record : bpRecords){
				if(record.getDbp() >= bpConfig.getDbp2()
					|| record.getSbp() >= bpConfig.getSbp2()
					|| record.getHeartrate() >= bpConfig.getHeartrate2()){
					return 3;
				}else if(record.getDbp() <= bpConfig.getDbp1()
					|| record.getSbp() <= bpConfig.getSbp1()
					|| record.getHeartrate() <= bpConfig.getHeartrate1()){
					return 1;
				}
			}
			return 2;
		}else{
			return 0;
		}
	}
}
