package com.lehealth.pay.dao.impl;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import com.lehealth.api.dao.impl.BaseJdbcDao;
import com.lehealth.pay.dao.WeixinPayDao;
import com.lehealth.pay.entity.WeixinOrder;

@Repository("weixinPayDao")
public class WeixinPayDaoImpl extends BaseJdbcDao implements WeixinPayDao {

	@Override
	public void insertNewOrder(WeixinOrder order) {
		String sql = "INSERT INTO weixin_order VALUE(NULL,:userid,:openid,:orderid,0,:goodsid,NULL,NULL,NULL,NOW(),NULL,NULL,NULL);";
		MapSqlParameterSource msps = new MapSqlParameterSource();
		msps.addValue("userid", order.getUserId());
		msps.addValue("openid", order.getOpenId());
		msps.addValue("orderid", order.getOrderId());
		msps.addValue("goodsid", order.getGoodsInfo().getId());
		this.namedJdbcTemplate.update(sql, msps);
	}

	@Override
	public int updateOrderStatus2PrePay(String orderId, String prePayId) {
		String sql = "";
		MapSqlParameterSource msps = new MapSqlParameterSource();
		return this.namedJdbcTemplate.update(sql, msps);
	}

	@Override
	public int updateOrderStatus2Pay(String orderId, String weixinOrderId) {
		String sql = "";
		MapSqlParameterSource msps = new MapSqlParameterSource();
		return this.namedJdbcTemplate.update(sql, msps);
	}

	@Override
	public int updateOrderStatus2Close(String orderId) {
		String sql = "";
		MapSqlParameterSource msps = new MapSqlParameterSource();
		return this.namedJdbcTemplate.update(sql, msps);
	}

	@Override
	public int updateOrderInfo(String orderId, String weixinOrderId,
			int status, int toStatus) {
		String sql = "";
		MapSqlParameterSource msps = new MapSqlParameterSource();
		return this.namedJdbcTemplate.update(sql, msps);
	}

}
