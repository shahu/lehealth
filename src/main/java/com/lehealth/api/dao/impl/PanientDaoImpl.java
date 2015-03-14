package com.lehealth.api.dao.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.lehealth.api.dao.PanientDao;
import com.lehealth.data.bean.PanientGuardianInfo;
import com.lehealth.data.bean.PanientInfo;
import com.lehealth.util.TokenUtils;

@Repository("panientDao")
public class PanientDaoImpl extends BaseJdbcDao implements PanientDao {

	@Override
	public PanientInfo selectPanient(String userid) {
		String sql="SELECT userid,username,gender,birthday,height,weight FROM user_patient_info WHERE userid=:userid";
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("userid", userid);
		SqlRowSet rs=this.namedJdbcTemplate.queryForRowSet(sql, msps);
		PanientInfo info=new PanientInfo();
		if(rs.next()){
			String id=StringUtils.trimToEmpty(rs.getString("userid"));
			String userName=StringUtils.trimToEmpty(rs.getString("username"));
			int gender=rs.getInt("gender");
			if(rs.getDate("birthday")!=null){
				long birthday=rs.getDate("birthday").getTime();
				info.setBirthday(birthday);
			}
			float height=rs.getFloat("height");
			float weight=rs.getFloat("weight");
			info.setUserId(id);
			info.setUserName(userName);
			info.setGender(gender);
			info.setHeight(height);
			info.setWeight(weight);
		}
		return info;
	}

	@Override
	public boolean updatePanient(PanientInfo info) {
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("userid", info.getUserId());
		msps.addValue("gender", info.getGender());
		msps.addValue("birthday", new Timestamp(info.getBirthday()));
		msps.addValue("height", info.getHeight());
		msps.addValue("weight", info.getWeight());
		msps.addValue("username", info.getUserName());
		String sql="UPDATE user_patient_info SET gender=:gender,birthday=:birthday,height=:height,weight=:weight,username=:username WHERE userid=:userid";
		int i=this.namedJdbcTemplate.update(sql, msps);
		if(i==0){
			sql="INSERT INTO user_patient_info VALUE(:uuid,:userid,:gender,:birthday,:height,:weight,:username)";
			msps.addValue("uuid", TokenUtils.buildUUid());
			i=this.namedJdbcTemplate.update(sql, msps);
			if(i==0){
				return false;
			}
		}
		return true;
	}

	@Override
	public List<PanientGuardianInfo> selectGuardianList(String userId) {
		String sql="SELECT userid,guardianname,guardiannumber FROM user_patient_guardian WHERE userid=:userid";
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("userid", userId);
		SqlRowSet rs=this.namedJdbcTemplate.queryForRowSet(sql, msps);
		List<PanientGuardianInfo> list=new ArrayList<PanientGuardianInfo>();
		while(rs.next()){
			PanientGuardianInfo info=new PanientGuardianInfo();
			String id=StringUtils.trimToEmpty(rs.getString("userid"));
			String guardianName=StringUtils.trimToEmpty(rs.getString("guardianname"));
			String guardianNumber=StringUtils.trimToEmpty(rs.getString("guardiannumber"));
			info.setUserId(id);
			info.setGuardianName(guardianName);
			info.setGuardianNumber(guardianNumber);
			list.add(info);
		}
		return list;
	}

	@Override
	public boolean insertGuardian(PanientGuardianInfo info) {
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("userid", info.getUserId());
		msps.addValue("guardianname", info.getGuardianName());
		msps.addValue("guardiannumber", info.getGuardianNumber());
		msps.addValue("uuid", TokenUtils.buildUUid());
		String sql="INSERT INTO user_patient_guardian VALUE(:uuid,:userid,:guardianname,:guardiannumber)";
		int i=this.namedJdbcTemplate.update(sql, msps);
		if(i==0){
			return false;
		}else{
			return true;
		}
	}

	@Override
	public boolean deleteGuardian(String userId,String guardianNumber) {
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("userid", userId);
		msps.addValue("guardiannumber", guardianNumber);
		String sql="DELETE FROM user_patient_guardian WHERE userid=:userid and guardiannumber=:guardiannumber";
		int i=this.namedJdbcTemplate.update(sql, msps);
		if(i==0){
			return false;
		}else{
			return true;
		}
	}

	@Override
	public List<PanientInfo> selectPanientListByGuardian(String guardianPhoneNumber) {
		List<PanientInfo> list=new ArrayList<PanientInfo>();
		String sql="select t2.username,t2.userid from user_patient_guardian t1 "
				+"inner join user_patient_info t2 on t1.userid=t2.userid "
				+"where t1.guardiannumber = :guardianPhoneNumber";
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("guardianPhoneNumber", guardianPhoneNumber);
		SqlRowSet rs=this.namedJdbcTemplate.queryForRowSet(sql, msps);
		while(rs.next()){
			PanientInfo info=new PanientInfo();
			info.setUserId(StringUtils.trimToEmpty(rs.getString("userid")));
			info.setUserName(StringUtils.trimToEmpty(rs.getString("username")));
			list.add(info);
		}
		return list;
	}
	
	@Override
	public List<PanientInfo> selectPanientListByDoctor(String doctorUserId) {
		List<PanientInfo> list=new ArrayList<PanientInfo>();
		String sql="SELECT t1.patinetid,t2.username FROM mapping_doctor_patient_attention t1 "
				+"LEFT JOIN user_patient_info t2 ON t1.patinetid=t2.userid "
				+"WHERE t1.doctorid=:doctorid ";
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("doctorid", doctorUserId);
		SqlRowSet rs=this.namedJdbcTemplate.queryForRowSet(sql, msps);
		while(rs.next()){
			PanientInfo info=new PanientInfo();
			info.setUserId(StringUtils.trimToEmpty(rs.getString("patinetid")));
			info.setUserName(StringUtils.trimToEmpty(rs.getString("username")));
			list.add(info);
		}
		return list;
	}

}
