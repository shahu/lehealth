package com.lehealth.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lehealth.bean.MedicineCategroy;
import com.lehealth.bean.MedicineResult;
import com.lehealth.dao.MedicineDao;

@Repository("medicineDao")
public class MedicineDaoImpl extends BaseJdbcDao implements MedicineDao {

	public MedicineResult selectMedicineRecords() {
		// TODO Auto-generated method stub
		return null;
	}

	public String insertMedicineRecord() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<MedicineCategroy> selectMedicines() {
		// TODO Auto-generated method stub
		return null;
	}

}
