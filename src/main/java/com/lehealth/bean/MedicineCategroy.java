package com.lehealth.bean;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class MedicineCategroy {

	private int cateId=0;
	private String cateName="";
	
	private List<Medicine> medicines=new ArrayList<Medicine>();


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
	public List<Medicine> getMedicines() {
		return medicines;
	}
	public void setMedicines(List<Medicine> medicines) {
		this.medicines = medicines;
	}
	public void addMedicine(Medicine medicine) {
		this.medicines.add(medicine);
	}
	
	public JSONObject toJsonObj(){
		JSONObject obj=new JSONObject();
		obj.accumulate("catename", cateName);
		JSONArray arr=new JSONArray();
		for(Medicine m:medicines){
			arr.add(m.toJsonObj());
		}
		obj.accumulate("medicines", arr);
		return obj;
	}
}
