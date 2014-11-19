package com.lehealth.dao.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.lehealth.bean.User;
import com.lehealth.dao.LoginDao;

@Repository("loginDao")
public class LoginDaoImpl extends BaseJdbcDao implements LoginDao {

	@Override
	public boolean insertUser(User user) {
		String sql="INSERT INTO user VALUE(:userid,:loginid,:pwdmd5)";
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("userid", user.getUserid());
		msps.addValue("loginid", user.getLoginid());
		msps.addValue("pwdmd5", user.getPwd());
		int result=this.namedJdbcTemplate.update(sql, msps);
		if(result==0){
			return false;
		}else{
			return true;
		}
	}

	@Override
	public boolean checkUser4Login(String loginId, String pwdmd5) {
		String sql="SELECT loginid FROM user WHERE loginid=:loginId AND pwdmd5=:pwdmd5";
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("loginId", loginId);
		msps.addValue("pwdmd5", pwdmd5);
		SqlRowSet rs=this.namedJdbcTemplate.queryForRowSet(sql, msps);
		if(rs.next()){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public User getUser(String loginId) {
		String sql="SELECT userid,pwdmd5 FROM user WHERE loginid=:loginId";
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("loginId", loginId);
		SqlRowSet rs=this.namedJdbcTemplate.queryForRowSet(sql, msps);
		User user=new User();
		if(rs.next()){
			String userid=StringUtils.trimToEmpty(rs.getString("userid"));
			String pwdmd5=StringUtils.trimToEmpty(rs.getString("pwdmd5"));
			user.setLoginid(loginId);
			user.setPwdmd5(pwdmd5);
			user.setUserid(userid);
		}
		return user;
	}
}
