package com.lehealth.dao.impl;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.lehealth.dao.TestDao;

@Repository("testDao")
public class TestDaoImpl extends BaseJdbcDao implements TestDao {

	public void test() {
		String sql="select count(1) as c from test";
		SqlRowSet rs=this.jdbcTemplate.queryForRowSet(sql);
		if(rs.next()){
			System.out.println("dao1:"+rs.getString("c"));
		}
		
		sql="select id as c from test where b=:index";
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("index", "33");
		rs=this.namedJdbcTemplate4Read.queryForRowSet(sql,msps);
		if(rs.next()){
			System.out.println("dao2:"+rs.getString("c"));
		}
	}

}
