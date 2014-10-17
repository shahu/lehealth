package com.lehealth.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.lehealth.util.Constant;

public class BpInfo {
	private int status=1;
	private List<String> dates=new ArrayList<String>();
	private Map<String,Integer> dbp=new HashMap<String, Integer>();
	private Map<String,Integer> sbp=new HashMap<String, Integer>();
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public void setDates(Date lastDate,int lastDays) {
		if(lastDate!=null){
			while(lastDays>0){
				dates.add(DateFormatUtils.format(lastDate, Constant.dateFormat_yyyy_mm_dd));
				lastDate=DateUtils.addDays(lastDate, -1);
				lastDays--;
			}
		}
	}
	public List<String> getDates() {
		return dates;
	}
	public List<Integer> getDbp() {
		List<Integer> list=new ArrayList<Integer>();
		if(!this.dbp.isEmpty()){
			for(String date:dates){
				if(dbp.containsKey(date)){
					list.add(dbp.get(date));
				}else{
					list.add(0);
				}
			}
		}
		return list;
	}
	public void setDbp(Map<String, Integer> dbp) {
		this.dbp = dbp;
	}
	public List<Integer> getSbp() {
		List<Integer> list=new ArrayList<Integer>();
		if(!this.sbp.isEmpty()){
			for(String date:dates){
				if(sbp.containsKey(date)){
					list.add(sbp.get(date));
				}else{
					list.add(0);
				}
			}
		}
		return list;
	}
	public void setSbp(Map<String, Integer> sbp) {
		this.sbp = sbp;
	}
}
