package com.lehealth.api.dao.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.lehealth.api.dao.LoginDao;
import com.lehealth.api.entity.UserBaseInfo;
import com.lehealth.data.type.UserRoleType;

@Repository("loginDao")
public class LoginDaoImpl extends BaseJdbcDao implements LoginDao {

	@Override
	public boolean insertUser(UserBaseInfo user) {
		String sql="INSERT INTO user_base_info VALUE(:userid,:loginid,:pwdmd5,:roleid)";
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("userid", user.getUserId());
		msps.addValue("loginid", user.getLoginId());
		msps.addValue("pwdmd5", user.getPwdmd5());
		msps.addValue("roleid", user.getRole().getRoleId());
		int result=this.namedJdbcTemplate.update(sql, msps);
		if(result==0){
			return false;
		}else{
			return true;
		}
	}

	@Override
	public UserBaseInfo selectUserBaseInfo(String loginId) {
		return this.selectUserBaseInfo(loginId, null);
	}

	@Override
	public UserBaseInfo selectUserBaseInfo(String loginId, String pwdmd5) {
		MapSqlParameterSource msps=new MapSqlParameterSource();
		String sql="SELECT userid,loginid,pwdmd5,roleid FROM user_base_info WHERE loginid=:loginid ";
		msps.addValue("loginid", loginId);
		if(StringUtils.isNotBlank(pwdmd5)){
			sql += "AND pwdmd5=:pwdmd5 ";
			msps.addValue("pwdmd5", pwdmd5);
		}
		SqlRowSet rs=this.namedJdbcTemplate.queryForRowSet(sql, msps);
		
		if(rs.next()){
			String userid=StringUtils.trimToEmpty(rs.getString("userid"));
			String password=StringUtils.trimToEmpty(rs.getString("pwdmd5"));
			UserRoleType role = UserRoleType.getTypeById(rs.getInt("roleid")) ;
			return new UserBaseInfo(userid, loginId, password, role);
		}else{
			return null;
		}
	}
}
