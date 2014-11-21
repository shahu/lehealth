package com.lehealth.bean;

import net.sf.json.JSONObject;

public class DiseaseHistory {
	
	private String userId="";
	private int diseaseId=0;
	private String diseaseName="";
	private String diseaseDescription="";
	private String medicineDescription="";
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getDiseaseId() {
		return diseaseId;
	}
	public void setDiseaseId(int diseaseId) {
		this.diseaseId = diseaseId;
	}
	public String getDiseaseDescription() {
		return diseaseDescription;
	}
	public void setDiseaseDescription(String diseaseDescription) {
		this.diseaseDescription = diseaseDescription;
	}
	public String getMedicineDescription() {
		return medicineDescription;
	}
	public void setMedicineDescription(String medicineDescription) {
		this.medicineDescription = medicineDescription;
	}
	public String getDiseaseName() {
		return diseaseName;
	}
	public void setDiseaseName(String diseaseName) {
		this.diseaseName = diseaseName;
	}
	public JSONObject toJsonObj(){
		JSONObject obj=new JSONObject();
		obj.accumulate("diseaseId", this.diseaseId);
		obj.accumulate("diseaseName", this.diseaseName);
		obj.accumulate("diseaseDescription", diseaseDescription);
		obj.accumulate("medicinedescription", medicineDescription);
		return obj;
	}
	
}
