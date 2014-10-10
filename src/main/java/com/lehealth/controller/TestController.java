/**
 * 
 */
package com.lehealth.controller;

import java.io.IOException;
import java.io.StringReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.lehealth.service.TestService;

@Controller
@RequestMapping("/test")
public class TestController {
	private static Log log = LogFactory.getLog(TestController.class);
	
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
