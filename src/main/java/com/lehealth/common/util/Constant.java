package com.lehealth.common.util;

import java.util.Comparator;

import com.lehealth.data.bean.BloodpressureConfig;
import com.lehealth.data.bean.BloodpressureRecord;
import com.lehealth.data.bean.MedicineRecord;
import com.lehealth.data.type.BloodPressStatusType;

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
	
	public static final long identifyingCodeValidityMinute = 1;
	public static final long identifyingCodeValidityTime = identifyingCodeValidityMinute*60*1000;
	public static final long identifyingCodeValidityClearTime = 60*60*1000;
	
	public static BloodPressStatusType getBpStatus(int sbp, int dbp, int heartrate, BloodpressureConfig config){
		if((dbp >= config.getDbp2() && config.getDbp2() > 0)
			|| (sbp >= config.getSbp2() && config.getSbp2() > 0)
			|| (heartrate >= config.getHeartrate2() && config.getHeartrate2() > 0)){
			return BloodPressStatusType.high;
		}else if((dbp <= config.getDbp1() && config.getDbp1() > 0)
			|| (sbp <= config.getSbp1() && config.getSbp1() > 0)
			|| (heartrate <= config.getHeartrate1() && config.getHeartrate1() > 0)){
			return BloodPressStatusType.low;
		}
		return BloodPressStatusType.normal;
	}
}
