package com.lehealth.common.util;

import java.util.Comparator;

import com.lehealth.api.entity.BloodpressureRecord;
import com.lehealth.api.entity.MedicineRecord;

public class ComparatorUtils {
	
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
	
}
