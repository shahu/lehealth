package com.lehealth.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.lehealth.dao.TestDao;
import com.lehealth.service.TestService;
import com.pplive.ads.admanager.bean.ThrowBean;
import com.pplive.ads.admanager.bean.WebInventoryResult;
import com.util.SuperDate;

@Repository("testDao")
public class TestDaoImpl extends BaseJdbcDao implements TestDao {

	@Autowired
	@Qualifier("webinventoryService")
	private TestService webinventoryService;
	/**
	 * 获取一个维度的库存信息
	 * 
	 * */
	@Override
	public Map<Date, List<ThrowBean>> getCurWebThrow(ThrowBean throwbean) {
		
		Map<Date, List<ThrowBean>> tbMap = new HashMap<Date, List<ThrowBean>>();

		//存储地域名称，防止重复查询数据库
		Map<String,String> areaNameMap = new HashMap<String,String>(); 
		
		String start = SuperDate.formatDateTime(throwbean.getStartDate_group(), SuperDate.patternDateTime)
				.split("[ ]")[0];
		String end = SuperDate.formatDateTime(throwbean.getEndDate_group(), SuperDate.patternDateTime)
				.split("[ ]")[0];
		//新增预定状态5
		String statusFilter = " and b.status in (0,1,2,5)";
		String sql = "select a.ThrowActivityCode,a.ThrowDate,b.Title,b.exclusiveFlag,b.PlayAreaIds,c.LoopCount,d.OrderName,b.ChargeType,e.UserName,b.status "
				+ "from ad_throw_day_data a  " 
				+ "inner join ad_throw_activity b on a.ThrowActivityCode=b.ThrowActivityCode "
				+ "inner JOIN ad_order d on b.OrderCode=d.OrderCode " 
				+ "inner JOIN ad_position c on c.ID=b.AdPostionId " 
				+ "inner JOIN bk_sys_user e on e.ID=d.AEId " 
				+ "where a.ThrowDate BETWEEN'"
				+ start
				+ "' AND '"
				+ end
				+ "' and b.AdPostionId=" 
				+ throwbean.getAdPostionId_group() 
				+ " and a.amount = 1 "
				+ statusFilter 
				+ "and b.Deleted=0"
				+" ORDER BY a.ThrowDate";
		
		SqlRowSet result = this.jdbcTemplate.queryForRowSet(sql);
		while (result != null && result.next()) {
			ThrowBean tb = new ThrowBean();
			Date curDate = result.getDate("ThrowDate");
			tb.setAdPostionId_group(throwbean.getAdPostionId_group());
			tb.setThrowActivityCode(result.getString("ThrowActivityCode"));
			tb.setPlayAreaIds(result.getString("PlayAreaIds"));
			tb.setExclusive(result.getInt("exclusiveFlag"));
			tb.setOrderName(result.getString("OrderName"));
			tb.setChargeType_group(result.getInt("ChargeType"));
			tb.setTitle_throw(result.getString("Title"));
			tb.setAeName(result.getString("UserName"));
			tb.setStatus(result.getInt("status"));
			String playAreaName = "";
			if (areaNameMap.containsKey(tb.getThrowActivityCode())){
				playAreaName = areaNameMap.get(tb.getThrowActivityCode());
				tb.setPlayAreaName(playAreaName);
			}else{
				webinventoryService.buildThrowAreas(tb);
				areaNameMap.put(tb.getThrowActivityCode(), tb.getPlayAreaName());
			}
			if (tbMap.containsKey(curDate)){
				List<ThrowBean> tmptblist = tbMap.get(curDate);
				tmptblist.add(tb);
			}else{
				List<ThrowBean> tmptblist = new ArrayList<ThrowBean>();
				tmptblist.add(tb);
				tbMap.put(curDate, tmptblist);
			}
		}
		return tbMap;
	}
	
	/**
	 * 获取一个维度的排期
	 * 
	 * */
	public List<WebInventoryResult> getwebPlanData(ThrowBean tb){
		List<WebInventoryResult> webInventorylist = new ArrayList<WebInventoryResult>();
		String throwCode = tb.getThrowActivityCode();
		String start = SuperDate.formatDateTime(tb.getStartDate_group(), SuperDate.patternDateTime)
				.split("[ ]")[0];
		String end = SuperDate.formatDateTime(tb.getEndDate_group(), SuperDate.patternDateTime)
				.split("[ ]")[0];
		String sql = " select * from ad_throw_day_data where throwdate BETWEEN '"
				+ start
				+ "' AND '"
				+ end
				+ "' AND ThrowActivityCode = '"
				+ throwCode + "' ORDER BY throwdate";
		SqlRowSet rs = this.jdbcTemplate.queryForRowSet(sql);
		while (rs.next()) {
			WebInventoryResult onewebresult = new WebInventoryResult();
			onewebresult.setCastInventory(rs.getInt("amount"));
			onewebresult.setDate(rs.getDate("ThrowDate"));
			webInventorylist.add(onewebresult);
		}
		return webInventorylist;
	}
	
	/**
     * 获取一个维度的库存信息
     * 
     * */
    @Override
    public Map<Date, List<ThrowBean>> getCurWebThrowByAdsPosition(int adPostionId, Date startDate, Date endDate) {
        
        Map<Date, List<ThrowBean>> tbMap = new HashMap<Date, List<ThrowBean>>();

        //存储地域名称，防止重复查询数据库
        Map<String,String> areaNameMap = new HashMap<String,String>(); 
        
        String start = SuperDate.formatDateTime(startDate, SuperDate.patternDateTime)
                .split("[ ]")[0];
        String end = SuperDate.formatDateTime(endDate, SuperDate.patternDateTime)
                .split("[ ]")[0];
        //新增预定状态5
        String statusFilter = " and b.status in (0,1,2,5)";
        String sql = "select a.ThrowActivityCode,a.ThrowDate,b.exclusiveFlag,b.PlayAreaIds,c.LoopCount,d.OrderName,b.ChargeType,b.status "
                + "from ad_throw_day_data a  " 
                + "inner join ad_throw_activity b on a.ThrowActivityCode=b.ThrowActivityCode "
                + "inner JOIN ad_order d on b.OrderCode=d.OrderCode " 
                + "inner JOIN ad_position c on c.ID=b.AdPostionId " 
                + "where a.ThrowDate BETWEEN'"
                + start
                + "' AND '"
                + end
                + "' and b.AdPostionId=" 
                + adPostionId
                + " and a.amount = 1 "
                + statusFilter 
                + "and b.Deleted=0"+
                " ORDER BY a.ThrowDate";
        SqlRowSet result = this.jdbcTemplate.queryForRowSet(sql);
        while (result != null && result.next()) {
            ThrowBean tb = new ThrowBean();
            Date curDate = result.getDate("ThrowDate");
            tb.setAdPostionId_group(adPostionId);
            tb.setThrowActivityCode(result.getString("ThrowActivityCode"));
            tb.setPlayAreaIds(result.getString("PlayAreaIds"));
            tb.setExclusive(result.getInt("exclusiveFlag"));
            tb.setOrderName(result.getString("OrderName"));
            tb.setChargeType_group(result.getInt("ChargeType"));
            tb.setStatus(result.getInt("status"));
            String playAreaName = "";
            if (areaNameMap.containsKey(tb.getThrowActivityCode())){
                playAreaName = areaNameMap.get(tb.getThrowActivityCode());
                tb.setPlayAreaName(playAreaName);
            }else{
                webinventoryService.buildThrowAreas(tb);
                areaNameMap.put(tb.getThrowActivityCode(), tb.getPlayAreaName());
            }
            if (tbMap.containsKey(curDate)){
                List<ThrowBean> tmptblist = tbMap.get(curDate);
                tmptblist.add(tb);
            }else{
                List<ThrowBean> tmptblist = new ArrayList<ThrowBean>();
                tmptblist.add(tb);
                tbMap.put(curDate, tmptblist);
            }
        }
        return tbMap;
    }
	
}
