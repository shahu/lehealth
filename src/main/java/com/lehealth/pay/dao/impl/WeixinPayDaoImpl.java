package com.lehealth.pay.dao.impl;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import com.lehealth.api.dao.impl.BaseJdbcDao;
import com.lehealth.pay.dao.WeixinPayDao;
import com.lehealth.pay.entity.WeixinOrder;

@Repository("weixinPayDao")
public class WeixinPayDaoImpl extends BaseJdbcDao implements WeixinPayDao {

	@Override
	public void insert(WeixinOrder order) {
		String sql = "INSERT INTO weixin_order VALUE(NULL,:userid,:openid,:orderid,0,:goodsid,NULL,NULL,NULL,NOW(),NULL,NULL,NULL);";
		MapSqlParameterSource msps = new MapSqlParameterSource();
		msps.addValue("userid", order.getUserId());
		msps.addValue("openid", order.getOpenId());
		msps.addValue("orderid", order.getOrderId());
		msps.addValue("goodsid", order.getGoodsInfo().getId());
		this.namedJdbcTemplate.update(sql, msps);
	}

	@Override
	public WeixinOrder selectInfo(String orderId){
		
	}
	
	@Override
	public int updateStatus2PrePay(String orderId, String prePayId) {
		String sql = "";
		MapSqlParameterSource msps = new MapSqlParameterSource();
		return this.namedJdbcTemplate.update(sql, msps);
	}

	@Override
	public int updateStatus2Pay(String orderId, String weixinOrderId) {
		String sql = "";
		MapSqlParameterSource msps = new MapSqlParameterSource();
		return this.namedJdbcTemplate.update(sql, msps);
	}

	@Override
	public int updateStatus2Close(String orderId) {
		String sql = "";
		MapSqlParameterSource msps = new MapSqlParameterSource();
		return this.namedJdbcTemplate.update(sql, msps);
	}

	@Override
	public int updateInfo(WeixinOrder order) {
		String sql = "";
		MapSqlParameterSource msps = new MapSqlParameterSource();
		return this.namedJdbcTemplate.update(sql, msps);
	}

}
