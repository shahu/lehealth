package com.lehealth.dao.impl;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.lehealth.dao.TestDao;

@Repository("testDao")
public class TestDaoImpl extends BaseJdbcDao implements TestDao {

	public void test() {
		String sql="select count(1) as c from test where a=:a";
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("a", "1");
		SqlRowSet rs=this.jdbcTemplate4Read.queryForRowSet(sql,msps);
		while(rs.next()){
			System.out.println(rs.getInt("c"));
		}
	}

}
