package com.lehealth.dao.impl;

import org.springframework.stereotype.Repository;
import com.lehealth.bean.UserInfo;
import com.lehealth.dao.LoginDao;


@Repository("loginDao")
public class LoginDaoImpl extends BaseJdbcDao implements LoginDao {

	public UserInfo selectUserInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	public String insertUser() {
		// TODO Auto-generated method stub
		return null;
	}

}
