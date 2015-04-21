package com.lehealth.pay.dao.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.lehealth.api.dao.impl.BaseJdbcDao;
import com.lehealth.pay.dao.WeixinPayDao;
import com.lehealth.pay.entity.WeixinOrder;

@Repository("weixinPayDao")
public class WeixinPayDaoImpl extends BaseJdbcDao implements WeixinPayDao {

	@Override
	public void insert(WeixinOrder order) {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO weixin_order VALUES(")
			.append("NULL,")
			.append(":userid,")
			.append(":openid,")
			.append(":ip,")
			.append("NULL,")
			.append(":orderid,")
			.append(":ordersecret,")
			.append("0,")
			.append(":goodsid,")
			.append("NULL,")
			.append("NULL,")
			.append("now(),")
			.append("NULL,")
			.append("NULL,")
			.append("NULL,")
			.append("NULL,")
			.append("NULL,")
			.append("NULL")
			.append(")");
		MapSqlParameterSource msps = new MapSqlParameterSource();
		msps.addValue("userid", order.getUserId());
		msps.addValue("openid", order.getOpenId());
		msps.addValue("ip", order.getIp());
		msps.addValue("orderid", order.getOrderId());
		msps.addValue("ordersecret", order.getOrderSecret());
		msps.addValue("goodsid", order.getGoodsInfo().getId());
		this.namedJdbcTemplate.update(sql.toString(), msps);
	}

	@Override
	public WeixinOrder selectInfo(String orderId){
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT t1.userid,t1.openid,")
//			.append("t1.id,t1.ip,t1.subscribe,")
			.append("t1.order_id,t1.order_secret,t1.status")
//			.append(",t1.prepay_id,t1.code_url,")
			.append("t1.goods_id,t2.fee,")
//			.append("t2.name,t2.info,t2.detail")
//			.append("t1.creattime,t1.starttime,t1.expiretime,t1.prepaytime,")
//			.append("t1.paytime,t1.callbacktime,t1.closetime ")
			.append("from weixin_order t1 ")
			.append("inner join goods_info t2 ")
			.append("on t1.goods_id = t2.id ")
			.append("where t1.order_id = :orderId ")
			.append("and t2.status = 1 ")
			.append("limit 1");
		MapSqlParameterSource msps = new MapSqlParameterSource();
		msps.addValue("orderid", orderId);
		SqlRowSet rs = this.namedJdbcTemplate.queryForRowSet(sql.toString(), msps);
		if(rs.next()){
			String userId = StringUtils.trimToEmpty(rs.getString("userid"));
			String openId = StringUtils.trimToEmpty(rs.getString("openid"));
			String orderSecret = StringUtils.trimToEmpty(rs.getString("order_secret"));
			int status = rs.getInt("status");
			int goodsId  = rs.getInt("goods_id");
			double fee = rs.getDouble("fee");
			
			WeixinOrder order = new WeixinOrder();
			order.setUserId(userId);
			order.setOpenId(openId);
			order.setOrderId(orderId);
			order.setOrderSecret(orderSecret);
			order.setStatus(status);
			order.getGoodsInfo().setId(goodsId);
			order.getGoodsInfo().setFee(fee);
		}
		return null;
	}

	@Override
	public int updateStatus2PrePay(String orderId, String prePayId) {
		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource msps = new MapSqlParameterSource();
		return this.namedJdbcTemplate.update(sql.toString(), msps);
	}

	@Override
	public int updateStatus2Pay(String orderId) {
		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource msps = new MapSqlParameterSource();
		return this.namedJdbcTemplate.update(sql.toString(), msps);
	}

	@Override
	public int updateStatus2Success(String orderId, String weixinOrderId) {
		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource msps = new MapSqlParameterSource();
		return this.namedJdbcTemplate.update(sql.toString(), msps);
	}

	@Override
	public int updateStatus2Error(String orderId) {
		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource msps = new MapSqlParameterSource();
		return this.namedJdbcTemplate.update(sql.toString(), msps);
	}

	@Override
	public int updateStatus2Close(String orderId) {
		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource msps = new MapSqlParameterSource();
		return this.namedJdbcTemplate.update(sql.toString(), msps);
	}
	

}
