package com.lehealth.util;

import java.util.Comparator;

import com.lehealth.data.bean.BloodpressureRecord;
import com.lehealth.data.bean.MedicineRecord;

public class Constant {
	public static final String dateFormat_hh = "HH";
	public static final String dateFormat_yyyy = "yyyy";
	public static final String dateFormat_yyyy_mm_dd = "yyyy-MM-dd";
	public static final String dateFormat_yyyy_mm_dd_hh_mm_ss = "yyyy-MM-dd HH:mm:ss";
	public static final String dateFormat_yyyymmddhhmmss = "yyyyMMddHHmmss";
	
	public static final Comparator<BloodpressureRecord> bpComparator = new Comparator<BloodpressureRecord>() {
		@Override
		public int compare(BloodpressureRecord o1, BloodpressureRecord o2) {
			return (int) (o1.getDate()-o2.getDate());
		}
	};
	
	public static final Comparator<MedicineRecord> medicineComparator = new Comparator<MedicineRecord>() {
		@Override
		public int compare(MedicineRecord o1, MedicineRecord o2) {
			if(o1.getDate()==o2.getDate()){
				return o1.getMedicineId()-o2.getMedicineId();
			}else{
				return (int) (o1.getDate()-o2.getDate());
			}
		}
	};
	
	public static final String identifyingCodeValidityMinute = "1";
}
