package com.lehealth.api.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.lehealth.api.dao.DiseaseDao;
import com.lehealth.bean.DiseaseHistory;
import com.lehealth.util.TokenUtils;

@Repository("diseaseDao")
public class DiseaseDaoImpl extends BaseJdbcDao implements DiseaseDao {

	@Override
	public List<DiseaseHistory> selectDiseaseHistorys(String userId) {
		String sql="SELECT t1.diseasedescription,t1.diseaseid,t1.medicinedescription,t2.name AS diseasename FROM disease_history t1 "
				+"INNER JOIN disease t2 ON t1.diseaseid=t2.id "
				+"WHERE t1.userid=:userid";
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("userid", userId);
		List<DiseaseHistory> list = new ArrayList<DiseaseHistory>();
		SqlRowSet rs=this.namedJdbcTemplate.queryForRowSet(sql, msps);
		while(rs.next()){
			DiseaseHistory h=new DiseaseHistory();
			h.setDiseaseDescription(StringUtils.trimToEmpty(rs.getString("diseasedescription")));
			h.setDiseaseId(rs.getInt("diseaseid"));
			h.setDiseaseName(StringUtils.trimToEmpty(rs.getString("diseasename")));
			h.setMedicineDescription(StringUtils.trimToEmpty(rs.getString("medicinedescription")));
			list.add(h);
		}
		return list;
	}

	@Override
	public boolean updateDiseaseHistory(DiseaseHistory history) {
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("userid", history.getUserId());
		msps.addValue("diseasedescription", history.getDiseaseDescription());
		msps.addValue("medicinedescription", history.getMedicineDescription());
		msps.addValue("diseaseid", history.getDiseaseId());
		String sql="UPDATE disease_history SET diseasedescription=:diseasedescription,medicinedescription=:medicinedescription,diseaseid=:diseaseid WHERE userid=:userid";
		int i=this.namedJdbcTemplate.update(sql, msps);
		if(i==0){
			sql="INSERT INTO disease_history VALUE(:uuid,:userid,:diseaseid,:diseasedescription,:medicinedescription)";
			msps.addValue("uuid", TokenUtils.buildUUid());
			i=this.namedJdbcTemplate.update(sql, msps);
			if(i==0){
				return false;
			}
		}
		return true;
	}

}
