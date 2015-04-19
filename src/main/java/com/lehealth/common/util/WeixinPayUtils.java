package com.lehealth.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.codec.digest.DigestUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class WeixinPayUtils {
	
	public static void main(String[] args) {
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("appid", "wx2421b1c4370ec43b");
		map.put("attach", "支付测试");
		map.put("body", "JSAPI支付测试");
		map.put("mch_id", "10000100");
		map.put("nonce_str", "1add1a30ac87aa2db72f57a2375d8fec");
		map.put("notify_url", "http://wxpay.weixin.qq.com/pub_v2/pay/notify.v2.php");
		map.put("openid", "oUpF8uMuAJO_M2pxb1Q9zNjWeS6o");
		map.put("out_trade_no", "1415659990");
		map.put("spbill_create_ip", "14.23.150.211");
		map.put("total_fee", "1");
		map.put("trade_type", "JSAPI");
		System.out.println(getSign(map,"test"));
		map.put("sign", getSign(map,"test"));
		
		String requestBody = transf2String(map);
		System.out.println("========================================");
		System.out.println(requestBody);
		
		String responseBody = HttpUtils.getPostResponse(Constant.weixinPrePayApi, requestBody);
		System.out.println("========================================");
        System.out.println(responseBody);
        String result = findKeyInXml(responseBody, "return_msg");
        System.out.println("========================================");
        System.out.println(result);
	}
	
	public static String getSign(Map<String, String> map,String key){
		StringBuilder sb = new StringBuilder();
		for(Entry<String, String> e : map.entrySet()){
			sb.append(e.getKey()).append("=").append(e.getValue()).append("&");
		}
		sb.append("key=").append(key);
		String temp = sb.toString();
		String sign = DigestUtils.md5Hex(temp);
		return sign;
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
			
			for(Entry<String, String> e : map.entrySet()){
				Element element = document.createElement(e.getKey());
				element.setTextContent(e.getValue());
				root.appendChild(element);
			}
			
			TransformerFactory transFactory = TransformerFactory.newInstance();
			DOMSource domSource = new DOMSource(document);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            Transformer transFormer = null;
			try {
				transFormer = transFactory.newTransformer();
				transFormer.transform(domSource, new StreamResult(bos));
			} catch (TransformerConfigurationException e) {
				e.printStackTrace();
			} catch (TransformerException e) {
				e.printStackTrace();
			}
            if(transFormer != null){
            	String requestBody = bos.toString();
            	return requestBody;
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
			document= builder.parse(xmlStr);
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
            	map.put(node.getNodeName(), node.getNodeValue());
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