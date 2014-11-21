package com.lehealth.api.dao.impl;

import java.sql.Timestamp;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.lehealth.api.dao.UserDao;
import com.lehealth.bean.UserGuardianInfo;
import com.lehealth.bean.UserInfo;
import com.lehealth.util.TokenUtils;

@Repository("userDao")
public class UserDaoImpl extends BaseJdbcDao implements UserDao {

	@Override
	public UserInfo selectUserInfo(String userid) {
		String sql="SELECT userid,username,gender,birthday,height,weight FROM user_info WHERE userid=:userid";
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("userid", userid);
		SqlRowSet rs=this.namedJdbcTemplate.queryForRowSet(sql, msps);
		UserInfo info=new UserInfo();
		if(rs.next()){
			String id=StringUtils.trimToEmpty(rs.getString("userid"));
			String userName=StringUtils.trimToEmpty(rs.getString("username"));
			int gender=rs.getInt("gender");
			long birthday=rs.getDate("birthday").getTime();
			float height=rs.getFloat("height");
			float weight=rs.getFloat("weight");
			info.setUserId(id);
			info.setUserName(userName);
			info.setGender(gender);
			info.setBirthday(birthday);
			info.setHeight(height);
			info.setWeight(weight);
		}
		return info;
	}

	@Override
	public boolean updateUserInfo(UserInfo info) {
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("userid", info.getUserId());
		msps.addValue("gender", info.getGender());
		msps.addValue("birthday", new Timestamp(info.getBirthday()));
		msps.addValue("height", info.getHeight());
		msps.addValue("weight", info.getWeight());
		msps.addValue("username", info.getUserName());
		String sql="UPDATE user_info SET gender=:gender,birthday=:birthday,height=:height,weight=:weight,username=:username WHERE userid=:userid";
		int i=this.namedJdbcTemplate.update(sql, msps);
		if(i==0){
			sql="INSERT INTO user_info VALUE(:uuid,:userid,:gender,:birthday,:height,:weight,:username)";
			msps.addValue("uuid", TokenUtils.buildUUid());
			i=this.namedJdbcTemplate.update(sql, msps);
			if(i==0){
				return false;
			}
		}
		return true;
	}

	@Override
	public UserGuardianInfo selectUserGuardianInfo(String userId) {
		String sql="SELECT userid,guardianname,guardiannumber FROM user_guardian WHERE userid=:userid";
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("userid", userId);
		SqlRowSet rs=this.namedJdbcTemplate.queryForRowSet(sql, msps);
		UserGuardianInfo info=new UserGuardianInfo();
		if(rs.next()){
			String id=StringUtils.trimToEmpty(rs.getString("userid"));
			String guardianName=StringUtils.trimToEmpty(rs.getString("guardianname"));
			String guardianNumber=StringUtils.trimToEmpty(rs.getString("guardiannumber"));
			info.setUserId(id);
			info.setGuardianName(guardianName);
			info.setGuardianNumber(guardianNumber);
		}
		return info;
	}

	@Override
	public boolean insertUserGuardianInfo(UserGuardianInfo info) {
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("userid", info.getUserId());
		msps.addValue("guardianname", info.getGuardianName());
		msps.addValue("guardiannumber", info.getGuardianNumber());
		msps.addValue("uuid", TokenUtils.buildUUid());
		String sql="INSERT INTO user_guardian VALUE(:uuid,:userid,:guardianname,:guardiannumber)";
		int i=this.namedJdbcTemplate.update(sql, msps);
		if(i==0){
			return false;
		}else{
			return true;
		}
	}

	@Override
	public boolean deleteUserGuardianInfo(String userId) {
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("userid", userId);
		String sql="DELETE FROM user_guardian WHERE userid=:userid";
		int i=this.namedJdbcTemplate.update(sql, msps);
		if(i==0){
			return false;
		}else{
			return true;
		}
	}

}
