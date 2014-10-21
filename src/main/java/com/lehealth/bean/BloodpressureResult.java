package com.lehealth.bean;

import java.util.ArrayList;
import java.util.List;

public class BloodpressureResult {
	private int status=1;
	
	//时刻-血压值
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
	
}
