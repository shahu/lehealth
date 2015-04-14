package com.lehealth.common.util;

import java.util.List;

import com.lehealth.data.bean.BloodpressureConfig;
import com.lehealth.data.bean.BloodpressureRecord;
import com.lehealth.data.type.BloodPressStatusType;

public class CheckStatusUtil {

	public static BloodPressStatusType bloodpress(int sbp, int dbp, int heartrate, BloodpressureConfig config) {
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

	public static BloodPressStatusType bloodpress(List<BloodpressureRecord> records, BloodpressureConfig config) {
		if(records != null && !records.isEmpty()){
			for(BloodpressureRecord record : records){
				BloodPressStatusType statusCode = bloodpress(record.getSbp(), record.getDbp(), record.getHeartrate(), config);
				if(statusCode != BloodPressStatusType.normal){
					return statusCode;
				}
			}
		}
		return BloodPressStatusType.normal;
	}
	
}
