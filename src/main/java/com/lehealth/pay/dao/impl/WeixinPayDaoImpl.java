package com.lehealth.pay.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.lehealth.api.dao.impl.BaseJdbcDao;
import com.lehealth.api.entity.UserBaseInfo;
import com.lehealth.data.type.UserRoleType;
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
			.append(":fee,")
			.append(":period,")
			.append("NULL,")
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
		msps.addValue("fee", order.getGoodsInfo().getId());
		msps.addValue("period", order.getGoodsInfo().getId());
		this.namedJdbcTemplate.update(sql.toString(), msps);
	}

	@Override
	public WeixinOrder selectInfo(String orderId){
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT t1.userid,t1.openid,")
			.append("t1.order_id,t1.transaction_id,t1.order_secret,t1.status,")
//			.append("t1.id,t1.ip,t1.subscribe,")
//			.append("t1.prepay_id,t1.code_url,")
//			.append("t1.creattime,t1.starttime,t1.expiretime,t1.prepaytime,")
//			.append("t1.paytime,t1.callbacktime,t1.closetime,")
			.append("t1.goods_id,t1.fee,t1.period,")
			.append("t2.name,t2.info,t2.detail,t2.fee as goods_fee ")
			.append("from weixin_order t1 ")
			.append("inner join goods_info t2 on t1.goods_id=t2.id")
			.append("where t1.order_id = :orderId ");
		MapSqlParameterSource msps = new MapSqlParameterSource();
		msps.addValue("orderid", orderId);
		SqlRowSet rs = this.namedJdbcTemplate.queryForRowSet(sql.toString(), msps);
		if(rs.next()){
			String userId = StringUtils.trimToEmpty(rs.getString("userid"));
			String openId = StringUtils.trimToEmpty(rs.getString("openid"));
			String orderSecret = StringUtils.trimToEmpty(rs.getString("order_secret"));
			String transactionId = StringUtils.trimToEmpty(rs.getString("transaction_id"));
			int status = rs.getInt("status");
			int goodsId  = rs.getInt("goods_id");
			double fee = rs.getDouble("fee");
			int period = rs.getInt("period");
			String goodsName = StringUtils.trimToEmpty(rs.getString("name"));
			String goodsInfo = StringUtils.trimToEmpty(rs.getString("info"));
			String goodsDetail = StringUtils.trimToEmpty(rs.getString("detail"));
			double goodsFee = rs.getDouble("goods_fee");
			
			WeixinOrder order = new WeixinOrder();
			order.setUserId(userId);
			order.setOpenId(openId);
			order.setOrderId(orderId);
			order.setOrderSecret(orderSecret);
			order.setTransactionId(transactionId);
			order.setStatus(status);
			order.getGoodsInfo().setId(goodsId);
			order.getGoodsInfo().setName(goodsName);
			order.getGoodsInfo().setInfo(goodsInfo);
			order.getGoodsInfo().setDetail(goodsDetail);
			order.getGoodsInfo().setFee(goodsFee);
			order.setFee(fee);
			order.setPeriod(period);
		}
		return null;
	}

	@Override
	public List<WeixinOrder> selectInfos(UserBaseInfo user){
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT t1.userid,t1.openid,")
			.append("t1.order_id,t1.transaction_id,t1.order_secret,t1.status,")
			.append("t1.goods_id,t1.fee,t1.period,")
			.append("t2.name,t2.info,t2.detail,t2.fee as goods_fee ")
			.append("from weixin_order t1 ")
			.append("inner join goods_info t2 on t1.goods_id=t2.id");
		if(user.getRole() != UserRoleType.admin){
			sql.append("where t1.userid = :userid ");
		}
		MapSqlParameterSource msps = new MapSqlParameterSource();
		msps.addValue("userid", user.getUserId());
		SqlRowSet rs = this.namedJdbcTemplate.queryForRowSet(sql.toString(), msps);
		List<WeixinOrder> list = new ArrayList<WeixinOrder>();
		while(rs.next()){
			String userId = StringUtils.trimToEmpty(rs.getString("userid"));
			String openId = StringUtils.trimToEmpty(rs.getString("openid"));
			String orderId = StringUtils.trimToEmpty(rs.getString("order_id"));
			String orderSecret = StringUtils.trimToEmpty(rs.getString("order_secret"));
			String transactionId = StringUtils.trimToEmpty(rs.getString("transaction_id"));
			int status = rs.getInt("status");
			int goodsId  = rs.getInt("goods_id");
			double fee = rs.getDouble("fee");
			int period = rs.getInt("period");
			String goodsName = StringUtils.trimToEmpty(rs.getString("name"));
			String goodsInfo = StringUtils.trimToEmpty(rs.getString("info"));
			String goodsDetail = StringUtils.trimToEmpty(rs.getString("detail"));
			double goodsFee = rs.getDouble("goods_fee");
			
			WeixinOrder order = new WeixinOrder();
			order.setUserId(userId);
			order.setOpenId(openId);
			order.setOrderId(orderId);
			order.setOrderSecret(orderSecret);
			order.setTransactionId(transactionId);
			order.setStatus(status);
			order.getGoodsInfo().setId(goodsId);
			order.getGoodsInfo().setName(goodsName);
			order.getGoodsInfo().setInfo(goodsInfo);
			order.getGoodsInfo().setDetail(goodsDetail);
			order.getGoodsInfo().setFee(goodsFee);
			order.setFee(fee);
			order.setPeriod(period);
			
			list.add(order);
		}
		return list;
	}
	
	@Override
	public int updateStatus2PrePay(String orderId, String prePayId) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE weixin_order SET status=1,prepay_id=:prePayId,prepaytime=now() WHERE orderid=:orderId AND STATUS=0");
		MapSqlParameterSource msps = new MapSqlParameterSource();
		msps.addValue("prePayId", prePayId);
		msps.addValue("orderId", orderId);
		return this.namedJdbcTemplate.update(sql.toString(), msps);
	}

	@Override
	public int updateStatus2Success(String orderId, String weixinOrderId, Date payTime) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE weixin_order SET status=2,transaction_id=:weixinOrderId,paytime=:payTime,callbacktime=now() WHERE orderid=:orderId AND STATUS=1");
		MapSqlParameterSource msps = new MapSqlParameterSource();
		msps.addValue("weixinOrderId", weixinOrderId);
		msps.addValue("payTime", payTime);
		msps.addValue("orderId", orderId);
		return this.namedJdbcTemplate.update(sql.toString(), msps);
	}

	@Override
	public int updateStatus2Error(String orderId) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE weixin_order SET status=3,callbacktime=now() WHERE orderid=:orderId AND STATUS=1");
		MapSqlParameterSource msps = new MapSqlParameterSource();
		return this.namedJdbcTemplate.update(sql.toString(), msps);
	}

	@Override
	public int updateStatus2Close(String orderId) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE weixin_order SET status=4,closetime=now() WHERE orderid=:orderId");
		MapSqlParameterSource msps = new MapSqlParameterSource();
		msps.addValue("orderId", orderId);
		return this.namedJdbcTemplate.update(sql.toString(), msps);
	}
	

}
