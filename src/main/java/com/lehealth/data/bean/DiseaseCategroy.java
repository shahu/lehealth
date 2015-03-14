package com.lehealth.data.bean;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class DiseaseCategroy {

	private int cateId=0;
	private String cateName="";
	
	private List<Disease> diseases=new ArrayList<Disease>();


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
	public List<Disease> getDiseases() {
		return diseases;
	}
	public void setDiseases(List<Disease> diseases) {
		this.diseases = diseases;
	}
	public void addDisease(Disease diseases) {
		this.diseases.add(diseases);
	}
	public JSONObject toJsonObj(){
		JSONObject obj=new JSONObject();
		obj.accumulate("catename", cateName);
		JSONArray arr=new JSONArray();
		for(Disease d:diseases){
			arr.add(d.toJsonObj());
		}
		obj.accumulate("diseases", arr);
		return obj;
	}
}
