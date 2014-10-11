package com.lehealth.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.lehealth.dao.TestDao;
import com.lehealth.service.TestService;

@Service("testService")
public class TestServiceImpl implements TestService {

    @Autowired
    @Qualifier("testDao")
    private TestDao testDao;

	public void test() {
		this.testDao.test();
	}
    

}
