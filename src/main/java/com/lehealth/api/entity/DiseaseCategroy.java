package com.lehealth.api.entity;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class DiseaseCategroy {

	private int cateId=0;
	private String cateName="";
	
	private List<DiseaseInfo> diseases=new ArrayList<DiseaseInfo>();


	public int getCateId() {
		return cateId;
	}
	public void setCateId(int cateId) {
		this.cateId = cateId;
	}
	public String getCateName() {
		return cateName;
	}
	public void setCateName(String cateName) {
		this.cateName = cateName;
	}
	public List<DiseaseInfo> getDiseases() {
		return diseases;
	}
	public void setDiseases(List<DiseaseInfo> diseases) {
		this.diseases = diseases;
	}
	public void addDisease(DiseaseInfo diseases) {
		this.diseases.add(diseases);
	}
	public JSONObject toJsonObj(){
		JSONObject obj=new JSONObject();
		obj.accumulate("catename", cateName);
		JSONArray arr=new JSONArray();
		for(DiseaseInfo d:diseases){
			arr.add(d.toJsonObj());
		}
		obj.accumulate("diseases", arr);
		return obj;
	}
}
