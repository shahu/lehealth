package com.lehealth.bean;

import java.util.ArrayList;
import java.util.List;

public class MedicineCategroy {

	private int cateid=0;
	private String catename="";
	
	private List<Medicine> medicines=new ArrayList<Medicine>();


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

	public List<Medicine> getMedicines() {
		return medicines;
	}

	public void setMedicines(List<Medicine> medicines) {
		this.medicines = medicines;
	}
	
	public void addMedicine(Medicine medicine) {
		this.medicines.add(medicine);
	}
	
}
