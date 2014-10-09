package com.pplive.ads.admanager.dao.impl;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author Administrator
 *
 * @version , 2009-10-13
 * @since 
 */
@Repository("BaseJdbcDao")
public class BaseJdbcDao {

	protected JdbcTemplate jdbcTemplate;
	protected JdbcTemplate jdbcTemplate4Read;
    protected NamedParameterJdbcTemplate namedJdbcTemplate;
    protected NamedParameterJdbcTemplate namedJdbcTemplate4Read;
	
	@Autowired
	public void setDataSource(@Qualifier("dataSourceDefault") DataSource dataSource){
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.namedJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
	@Autowired
    public void setDataSource4Read(@Qualifier("dataSourceDefault4Read") DataSource dataSource){
	    this.jdbcTemplate4Read = new JdbcTemplate(dataSource);
        this.namedJdbcTemplate4Read = new NamedParameterJdbcTemplate(dataSource);
    }
}
