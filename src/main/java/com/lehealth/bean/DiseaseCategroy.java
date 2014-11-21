package com.lehealth.bean;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class DiseaseCategroy {

	private int cateid=0;
	private String catename="";
	
	private List<Disease> diseases=new ArrayList<Disease>();

	public int getCateid() {
		return cateid;
	}

	public void setCateid(int cateid) {
		this.cateid = cateid;
	}

	public String getCatename() {
		return catename;
	}

	public void setCatename(String catename) {
		this.catename = catename;
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
		obj.accumulate("catename", catename);
		JSONArray arr=new JSONArray();
		for(Disease m:diseases){
			arr.add(m.toJsonObj());
		}
		obj.accumulate("diseases", arr);
		return obj;
	}
}
