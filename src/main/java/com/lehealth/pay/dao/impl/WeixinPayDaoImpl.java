package com.lehealth.pay.dao.impl;

import org.springframework.stereotype.Repository;

import com.lehealth.api.dao.impl.BaseJdbcDao;
import com.lehealth.pay.dao.WeixinPayDao;

@Repository("weixinPayDao")
public class WeixinPayDaoImpl extends BaseJdbcDao implements WeixinPayDao {

}
