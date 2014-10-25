package com.lehealth.util;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;

public class JacksonGlobalMappers {
	private static ObjectMapper defaultMapper = new ObjectMapper();
	private static ObjectMapper noNullMapper = new ObjectMapper();;
	
	static {
		noNullMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
	}

	public static String getDefaultJsonStr(Object o){
	    if(o==null){
	        return "";
	    }
	    try {
            return defaultMapper.writeValueAsString(o);
        } catch (JsonGenerationException e) {
            e.printStackTrace();
            return "";
        } catch (JsonMappingException e) {
            e.printStackTrace();
            return "";
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
	
    public static String getNoNullJsonStr(Object o){
        if(o==null){
            return "";
        }
        try {
            return noNullMapper.writeValueAsString(o);
        } catch (JsonGenerationException e) {
            e.printStackTrace();
            return "";
        } catch (JsonMappingException e) {
            e.printStackTrace();
            return "";
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
    
    public static <T> T getObjectByJsonStr(String jsonStr,Class<T> beanClass){
        if(StringUtils.isBlank(jsonStr)){
            return null;
        }
        try {
            return defaultMapper.readValue(jsonStr, beanClass);
        } catch (JsonParseException e) {
            e.printStackTrace();
            return null;
        } catch (JsonMappingException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
