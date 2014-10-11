/**
 * 
 */
package com.lehealth.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lehealth.service.TestService;

@Controller
@RequestMapping("/test")
public class TestController {
	private static Logger log = Logger.getLogger(TestController.class);
	
	@Autowired
    @Qualifier("testService")
    private TestService testService;
	
	@RequestMapping(value = "/test.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,String> test(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		Map<String,String> map=new HashMap<String,String>();
		return map;
	}
}
