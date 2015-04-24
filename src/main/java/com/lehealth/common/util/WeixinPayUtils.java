package com.lehealth.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class WeixinPayUtils {
	
	public static void main(String[] args) {
		Map<String, String> requestMap = new LinkedHashMap<String, String>();
		requestMap.put("appid", "wxe4b3e1f50a76f240");
		requestMap.put("attach", "b71a8859d76867338c5078fc357d3713");
		requestMap.put("body", "info1");
		requestMap.put("detail", "detail1");
		requestMap.put("mch_id", "1238122402");
		requestMap.put("nonce_str", "bbb");
		requestMap.put("notify_url", "http://lehealth.net.cn/weixin/callback/pay");
		requestMap.put("openid", "o2KUDt-Ex21Cb_QhEJoBQwvMZG_w");
		requestMap.put("out_trade_no", "O201504242225331");
		requestMap.put("spbill_create_ip", "127.0.0.1");
		requestMap.put("time_expire", "20150425222533");
		requestMap.put("time_start", "20150424222533");
//		// 微信单位是分
		requestMap.put("total_fee", "1");
		requestMap.put("trade_type", "JSAPI");
		String sign = getSign(requestMap, "fa9b9bcd5c7780705cb706b6b04e1075",true);
		requestMap.put("sign", sign);
		
		String requestBody = WeixinPayUtils.transf2String(requestMap);
		System.out.println(requestBody);
        String responseBody = HttpUtils.getPostResponse(Constant.weixinPrePayApi, requestBody);
        System.out.println(responseBody);
	}
	
	public static String getSign(Map<String, String> map, String key, boolean sortFlag){
		if(sortFlag){
			sortBySpell(map);
		}
		StringBuilder sb = new StringBuilder();
		for(Entry<String, String> e : map.entrySet()){
			if(StringUtils.isNotBlank(e.getValue())
					&& !StringUtils.equals("sign", e.getKey())){
				sb.append(e.getKey()).append("=").append(e.getValue()).append("&");
			}
		}
		sb.append("key=").append(key);
		String temp = sb.toString();
		System.out.println("md5="+temp);
		String sign = StringUtils.trimToEmpty(DigestUtils.md5Hex(temp).toUpperCase());
		System.out.println("sign="+sign);
		return sign;
	}
	
	private static final Comparator<String> spellComparator = new Comparator<String>() {
		@Override
		public int compare(String s1, String s2) {
			return s1.compareTo(s2);
		}
	};
	
	private static void sortBySpell(Map<String, String> map){
		List<String> keys = new ArrayList<String>(map.keySet());
		Collections.sort(keys, spellComparator);
		Map<String, String> temp = new LinkedHashMap<String, String>();
		for(String key : keys){
			temp.put(key, map.get(key));
		}
		map.clear();
		map.putAll(temp);
	}
	
	public static String transf2String(Map<String, String> map){
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		Document document = null;
		if(builder != null){
			document = builder.newDocument(); 
		}
		if(document != null){
			Element root = document.createElement("xml");
			document.appendChild(root);
			
			if(map != null && !map.isEmpty()){
				for(Entry<String, String> e : map.entrySet()){
					Element element = document.createElement(e.getKey());
					CDATASection cdata = document.createCDATASection(e.getValue());
					element.appendChild(cdata);
					root.appendChild(element);
				}
			}
			
			TransformerFactory transFactory = TransformerFactory.newInstance();
			DOMSource domSource = new DOMSource(document);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            Transformer transFormer = null;
			try {
				transFormer = transFactory.newTransformer();
				transFormer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
				transFormer.transform(domSource, new StreamResult(bos));
			} catch (TransformerConfigurationException e) {
				e.printStackTrace();
			} catch (TransformerException e) {
				e.printStackTrace();
			}
            if(transFormer != null){
            	String result = bos.toString();
            	return result;
            }
		}
		return "";
	}
	
	public static Map<String, String> transf2Xml(String xmlStr){
		Map<String, String> map = new LinkedHashMap<String, String>();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		Document document = null;
        try {
			document= builder.parse(new ByteArrayInputStream(xmlStr.getBytes("UTF-8")));
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        if(document != null){
        	Element root = document.getDocumentElement();
            NodeList nodes = root.getChildNodes();
            for(int i = 0 ; i < nodes.getLength() ; i++){
            	Node node = nodes.item(i);
            	if(node.getNodeType() == Node.ELEMENT_NODE){
            		map.put(node.getNodeName(), node.getTextContent());
            	}
            }
        }
        return map;
	}
	
	public static String findKeyInXml(String xmlStr, String key){
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		Document document = null;
        try {
			document= builder.parse(new ByteArrayInputStream(xmlStr.getBytes()));
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        if(document != null){
            NodeList nodes = document.getElementsByTagName(key);
            if(nodes.getLength() > 0){
            	return nodes.item(0).getTextContent();
            }
        }
        return "";
	}
}
