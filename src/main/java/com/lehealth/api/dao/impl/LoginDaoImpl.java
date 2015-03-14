package com.lehealth.api.dao.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.lehealth.api.dao.LoginDao;
import com.lehealth.data.bean.UserInfomation;

@Repository("loginDao")
public class LoginDaoImpl extends BaseJdbcDao implements LoginDao {

	@Override
	public boolean insertUser(UserInfomation user) {
		String sql="INSERT INTO user_base_info VALUE(:userid,:loginid,:pwdmd5,:roleid)";
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("userid", user.getUserId());
		msps.addValue("loginid", user.getLoginId());
		msps.addValue("pwdmd5", user.getPwd());
		msps.addValue("roleid", user.getRoleId());
		int result=this.namedJdbcTemplate.update(sql, msps);
		if(result==0){
			return false;
		}else{
			return true;
		}
	}

	@Override
	public boolean checkUser4Login(String loginId, String pwdmd5) {
		String sql="SELECT loginid FROM user_base_info WHERE loginid=:loginid AND pwdmd5=:pwdmd5";
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("loginid", loginId);
		msps.addValue("pwdmd5", pwdmd5);
		SqlRowSet rs=this.namedJdbcTemplate.queryForRowSet(sql, msps);
		if(rs.next()){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public UserInfomation getUser(String loginId) {
		String sql="SELECT userid,pwdmd5,roleid FROM user_base_info WHERE loginid=:loginid";
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("loginid", loginId);
		SqlRowSet rs=this.namedJdbcTemplate.queryForRowSet(sql, msps);
		UserInfomation user=new UserInfomation();
		if(rs.next()){
			String userid=StringUtils.trimToEmpty(rs.getString("userid"));
			String pwdmd5=StringUtils.trimToEmpty(rs.getString("pwdmd5"));
			user.setLoginId(loginId);
			user.setPwdmd5(pwdmd5);
			user.setUserId(userid);
			user.setRoleID(rs.getInt("roleid"));
		}
		return user;
	}
}
