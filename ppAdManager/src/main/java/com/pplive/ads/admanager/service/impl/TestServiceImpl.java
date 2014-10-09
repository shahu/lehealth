package com.pplive.ads.admanager.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

@Service("testService")
public class TestServiceImpl implements TestService {

    @Autowired
    @Qualifier("testDao")
    private TestDao testDao;
    
    @Override
    public void test() {
    	testDao.test();
    }

}
