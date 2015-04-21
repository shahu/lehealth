package com.lehealth.common.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.lehealth.api.dao.impl.BaseJdbcDao;
import com.lehealth.common.dao.SystemVariableDao;

@Repository("systemVariableDao")
public class SystemVariableDaoImpl extends BaseJdbcDao implements SystemVariableDao {

	@Override
	public Map<String, String> selectSystemVariables() {
		String sql="SELECT skey,svalue FROM system_variable";
		SqlRowSet rs=this.jdbcTemplate.queryForRowSet(sql);
		Map<String, String> map=new HashMap<String, String>();
		while(rs.next()){
			map.put(StringUtils.trimToEmpty(rs.getString("skey")), StringUtils.trimToEmpty(rs.getString("svalue")));
		}
		return map;
	}

}
