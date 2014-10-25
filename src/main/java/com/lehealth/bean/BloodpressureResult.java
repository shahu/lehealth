package com.lehealth.bean;

import java.util.ArrayList;
import java.util.List;

import com.lehealth.util.JacksonGlobalMappers;

public class BloodpressureResult {
	
	private int status=1;
	private int score=0;
	private List<BloodpressureInfo> infos=new ArrayList<BloodpressureInfo>();
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public List<BloodpressureInfo> getInfos() {
		return infos;
	}
	public void setInfos(List<BloodpressureInfo> infos) {
		this.infos = infos;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	
	public String toJson(){
		return JacksonGlobalMappers.getNoNullJsonStr(this);
	}
}
