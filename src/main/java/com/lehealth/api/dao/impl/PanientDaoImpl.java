package com.lehealth.api.dao.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.lehealth.api.dao.PanientDao;
import com.lehealth.api.entity.PanientGuardianInfo;
import com.lehealth.api.entity.PanientInfo;
import com.lehealth.api.entity.UserBaseInfo;
import com.lehealth.common.util.TokenUtils;
import com.lehealth.data.type.UserRoleType;

@Repository("panientDao")
public class PanientDaoImpl extends BaseJdbcDao implements PanientDao {

	@Override
	public PanientInfo selectPanient(String userid) {
		String sql="SELECT t1.loginid,t1.userid, t2.username, t2.gender, t2.birthday, t2.height, t2.weight, t2.idnumber, t2.files "
				+ "FROM user_base_info t1 "
				+ "left join user_patient_info t2 on t1.userid=t2.userid "
				+ "where t1.userid=:userid ";
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("userid", userid);
		SqlRowSet rs=this.namedJdbcTemplate.queryForRowSet(sql, msps);
		PanientInfo info=new PanientInfo();
		if(rs.next()){
			String id=StringUtils.trimToEmpty(rs.getString("userid"));
			String userName=StringUtils.trimToEmpty(rs.getString("username"));
			int gender=rs.getInt("gender");
			if(rs.getTimestamp("birthday") != null){
				long birthday=rs.getTimestamp("birthday").getTime();
				info.setBirthday(birthday);
			}
			float height=rs.getFloat("height");
			float weight=rs.getFloat("weight");
			String idNum = StringUtils.trimToEmpty(rs.getString("idnumber"));
			String files = StringUtils.trimToEmpty(rs.getString("files"));
			info.setUserId(id);
			info.setUserName(userName);
			info.setGender(gender);
			info.setHeight(height);
			info.setWeight(weight);
			info.setIDNumber(idNum);
			info.setFiles(files);
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
		msps.addValue("idnumber", info.getIDNumber());
		String sql="UPDATE user_patient_info SET gender=:gender,birthday=:birthday,height=:height,weight=:weight,username=:username,idnumber=:idnumber WHERE userid=:userid";
		int i=this.namedJdbcTemplate.update(sql, msps);
		if(i==0){
			sql="INSERT INTO user_patient_info(id,userid,gender,birthday,height,weight,username,idnumber,files) VALUE(:uuid,:userid,:gender,:birthday,:height,:weight,:username,:idnumber,'')";
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
		String sql="select t3.username,t2.loginid,t1.userid from user_patient_guardian t1 "
				+"inner join user_base_info t2 on t1.userid=t2.userid "
				+"left join user_patient_info t3 on t1.userid=t3.userid "
				+"where t1.guardiannumber = :guardianPhoneNumber";
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("guardianPhoneNumber", guardianPhoneNumber);
		SqlRowSet rs=this.namedJdbcTemplate.queryForRowSet(sql, msps);
		while(rs.next()){
			PanientInfo info=new PanientInfo();
			info.setUserId(StringUtils.trimToEmpty(rs.getString("userid")));
			info.setUserName(StringUtils.trimToEmpty(rs.getString("username")));
			info.setPhoneNumber(StringUtils.trimToEmpty(rs.getString("loginid")));
			list.add(info);
		}
		return list;
	}
	
	@Override
	public List<PanientInfo> selectPanientListByRole(UserBaseInfo user) {
		List<PanientInfo> list=new ArrayList<PanientInfo>();
		String sql="SELECT t1.userid,t2.username,t1.loginid "
				+"FROM user_base_info t1 "
				+"LEFT JOIN user_patient_info t2 ON t1.userid=t2.userid ";
		MapSqlParameterSource msps=new MapSqlParameterSource();
		if(user.getRole() == UserRoleType.dotcor){
			sql += "LEFT JOIN mapping_doctor_patient_attention t3 on t1.userid=t3.patientid "
					+ "WHERE t3.doctorid=:doctorid ";
			msps.addValue("doctorid", user.getUserId());
		}
		sql += "ORDER BY t1.createtime desc ";
		SqlRowSet rs=this.namedJdbcTemplate.queryForRowSet(sql, msps);
		while(rs.next()){
			PanientInfo info=new PanientInfo();
			info.setUserId(StringUtils.trimToEmpty(rs.getString("userid")));
			info.setUserName(StringUtils.trimToEmpty(rs.getString("username")));
			info.setPhoneNumber(StringUtils.trimToEmpty(rs.getString("loginid")));
			list.add(info);
		}
		return list;
	}

}
