package com.lehealth.sync.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.lehealth.api.dao.impl.BaseJdbcDao;
import com.lehealth.sync.dao.SyncBloodpressureDao;
import com.lehealth.sync.entity.YundfRecord;
import com.lehealth.sync.entity.YundfUser;
import com.lehealth.util.TokenUtils;
import com.lehealth.util.YundfUtils;

@Repository("syncBloodpressureDao")
public class SyncBloodpressureDaoImpl extends BaseJdbcDao implements SyncBloodpressureDao {

	@Override
	public Map<String,YundfUser> getLastRids(Set<String> phoneNumbers){
		String sql="SELECT t1.loginid,t1.userid,t2.lastSyncRid "
				+"FROM user_base_info t1 "
				+"LEFT OUTER JOIN sync_patient_record t2 ON (t1.userid=t2.userid AND source="+YundfUtils.source+") "
				+"WHERE loginid IN (:phones) "
				+"AND t1.roleid!=3";
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("phones", phoneNumbers);
		SqlRowSet rs=this.namedJdbcTemplate.queryForRowSet(sql, msps);
		Map<String,YundfUser> map=new HashMap<String,YundfUser>();
		while(rs.next()){
			YundfUser user=new YundfUser();
			user.setLastRid(rs.getInt("lastSyncRid"));
			user.setUserId(StringUtils.trimToEmpty(rs.getString("userid")));
			user.setPhone(StringUtils.trimToEmpty(rs.getString("loginid")));
			map.put(user.getPhone(),user);
		}
		return map;
	}

	@Override
	public void insertRecord(final String userId, final List<YundfRecord> records) {
		String sql="INSERT INTO bp_record VALUE(?,?,?,?,?,?,-1)";
		BatchPreparedStatementSetter updateBpss = new BatchPreparedStatementSetter(){
            @Override
            public void setValues(PreparedStatement ps, int i)
                    throws SQLException {
                final YundfRecord record = records.get(i);
                ps.setString(1,TokenUtils.buildUUid());
                ps.setString(2,userId);
                ps.setInt(3,record.getDiastolic());
                ps.setInt(4,record.getSystolic());
                ps.setInt(5,record.getPulse());
                ps.setTimestamp(6, new Timestamp(record.getRdatetime()));
            }
            @Override
            public int getBatchSize() {
                return records.size();
            }
        };
        this.jdbcTemplate.batchUpdate(sql, updateBpss);
	}

	@Override
	public void deleteSyncRid(Set<String> userIds) {
		String sql="DELETE FROM sync_patient_record WHERE userid IN (:userids)";
		MapSqlParameterSource msps=new MapSqlParameterSource();
		msps.addValue("userids", userIds);
		this.namedJdbcTemplate.update(sql, msps);
	}

	@Override
	public void insertSyncRid(final List<YundfUser> users) {
		String sql="INSERT INTO sync_patient_record VALUE(?,?,?,?,now())";
		BatchPreparedStatementSetter updateBpss = new BatchPreparedStatementSetter(){
            @Override
            public void setValues(PreparedStatement ps, int i)
                    throws SQLException {
                final YundfUser user = users.get(i);
                ps.setString(1,TokenUtils.buildUUid());
                ps.setString(2,user.getUserId());
                ps.setInt(3,user.getLastRid());
                ps.setInt(4,YundfUtils.source);
            }
            @Override
            public int getBatchSize() {
                return users.size();
            }
        };
        this.jdbcTemplate.batchUpdate(sql, updateBpss);
	}
	
}
