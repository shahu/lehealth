package com.lehealth.bean;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class HomeResult extends BloodpressureResult{
	
	private List<MedicineInfo> medicineecords=new ArrayList<MedicineInfo>();
	
	public List<MedicineInfo> getMedicineecords() {
		return medicineecords;
	}
	public void setMedicineecords(List<MedicineInfo> medicineecords) {
		this.medicineecords = medicineecords;
	}

	public JSONObject toJsonObj(){
		JSONObject obj=super.toJsonObj();
		if(medicineecords!=null&&!medicineecords.isEmpty()){
			JSONArray arr=new JSONArray();
			for(MedicineInfo record:medicineecords){
				arr.add(record.toHomeJsonObj());
			}
			obj.accumulate("history", arr);
		}
		return obj;
	}
}
