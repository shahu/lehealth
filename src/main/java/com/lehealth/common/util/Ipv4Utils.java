package com.lehealth.common.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

public class Ipv4Utils {
	
	private static Pattern localIpPattern = Pattern.compile("^192\\.168.*|^10\\..*|^172\\.(1[6-9]|2\\d|3[0-1]).*|^127\\..*");
	private static Pattern ipv4Pattern = Pattern.compile("^(1?\\d\\d?|2[0-4]\\d|25[0-5])\\.(1?\\d\\d?|2[0-4]\\d|25[0-5])\\.(1?\\d\\d?|2[0-4]\\d|25[0-5])\\.(1?\\d\\d?|2[0-4]\\d|25[0-5])$");
	
	private static final int MIN_VALID_NETMASK_WIDTH = 8;
    private static final int MAX_VALID_NETMASK_WIDTH = 30;
    
	// 从HTTP请求中取得客户端IP地址
	public static String getIp(HttpServletRequest req){
		//TODO test
		String ip = req.getParameter("ip");
		if(!StringUtils.isBlank(ip)){
			return ip;
		}
		
		ip = parseIpInHeader(req.getHeader("x-forwarded-for"));
		if(StringUtils.isBlank(ip)){
			ip = parseIpInHeader(req.getHeader("client_ip"));
			
			if(StringUtils.isBlank(ip)){
				ip = req.getRemoteAddr();
			}
		}
		
		return ip.trim();
	}
	
	// 从header获取IP
	private static String parseIpInHeader(String header){
		if(StringUtils.isBlank(header)){
			return "";
		}
		
		String[] ips = header.split(",\\s*");
		for(String ip : ips){
			if(isIpV4(ip) && !isLocalIp(ip)){
				return ip;
			} else {
				continue;
			}
		}
		return "";
	}
	
	private static boolean isLocalIp(String ip){
		return localIpPattern.matcher(ip).matches();
	}
	
	public static boolean isIpV4(String ip){
		return ipv4Pattern.matcher(ip).matches();
	}
	
	// 把IP字符串转化为ip数值
    public static long ip2Number(String ip){
        if(!isIpV4(ip)){
            throw new IllegalArgumentException("invalid IP address: " + ip);
        }
        return ipAddress2Number(ipString2Address(ip));
    }
    
    // 把IP字符串转化为Inet4Address对象
    public static Inet4Address ipString2Address(String ipString) {
        String[] ipSubstring=ipString.split("\\.");
        return ipByte2Address(new byte[] {
          (byte)NumberUtils.toInt(ipSubstring[0]),
          (byte)NumberUtils.toInt(ipSubstring[1]),
          (byte)NumberUtils.toInt(ipSubstring[2]),
          (byte)NumberUtils.toInt(ipSubstring[3])
        });
    }
    
    // 把IP数值转化为Inet4Address对象
    public static Inet4Address ipUNumber2IP(long address) {
        return ipByte2Address(new byte[] {
            (byte)((address >> 24) & 0xff),
            (byte)((address >> 16) & 0xff),
            (byte)((address >> 8) & 0xff),
            (byte)(address & 0xff),
        });
    }
    
    // 把IP字节数组转化为Inet4Address对象
    public static Inet4Address ipByte2Address(byte[] bytes) {
        if (bytes.length != 4){
            throw new IllegalArgumentException("array has length " + bytes.length + " != 4");
        }
        try {
            return (Inet4Address) InetAddress.getByAddress(ipByte2String(bytes), bytes);
        } catch (UnknownHostException e) {
            throw new RuntimeException("unexpected exception", e);
        }
    }
    
    // 把IP对象转化为ip数值
    public static long ipAddress2Number(Inet4Address address) {
        byte[] bytes = address.getAddress();
        return ((bytes[0] & 0xff) << 24)
             | ((bytes[1] & 0xff) << 16)
             | ((bytes[2] & 0xff) << 8)
             | (bytes[3] & 0xff);
    }
    
    // 把IP对象转化为ip字符串
    public static String ipAddress2String(Inet4Address address) {
        return ipByte2String(address.getAddress());
    }
    
    // 把IP字节数组转化为IP字符串
    public static String ipByte2String(byte[] bytes) {
        assert bytes.length == 4;
        return (bytes[0] & 0xff) + "." + (bytes[1] & 0xff) + "." + (bytes[2] & 0xff) + "." + (bytes[3] & 0xff);
    }
    
    // 把IP字符串转化为字节数组
    public static byte[] ipToBytesByInet(String ipAddr) {
        try {
            return InetAddress.getByName(ipAddr).getAddress();
        } catch (Exception e) {
            throw new IllegalArgumentException(ipAddr + " is invalid IP");
        }
    }
    
    // 把IP对象和IP子网对象转化为ip+子网长度字符串
    public static String ipandmaskAddress2String(Inet4Address address, Inet4Address netmask) {
        if(isValidNetmask(netmask)){
            throw new IllegalArgumentException(netmask + " is invalid netmask");
        }
        return ipAddress2String(address) + "/" + maskAddress2length(netmask);
    }
    
    // 从ip子网对象获取子网长度
    public static int maskAddress2length(Inet4Address netmask) {
        long mask = ipAddress2Number(netmask);
        if (mask == 0)
            return 0;
        for (int shift = 0; shift < 32; shift++) {
            if (mask == (~0 << shift))
                return 32 - shift;
        }
        throw new IllegalArgumentException("invalid netmask " + netmask);
    }
    
    // 是否是合法的子网掩码格式
    private static boolean isValidNetmask(Inet4Address netmask) {
        try {
            int width = maskAddress2length(netmask);
            return width >= MIN_VALID_NETMASK_WIDTH && width <= MAX_VALID_NETMASK_WIDTH;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    
    // IP对象是否在目标IP对象的子网中
    public static boolean isOnNetwork(Inet4Address ip, Inet4Address objectiveIp, int maskLength) {
        long addr1 = ipAddress2Number(ip);
        long addr2 = ipAddress2Number(objectiveIp);
        long mask = ~0 << (32 - maskLength);
        return (addr1 & mask) == (addr2 & mask);
    }
    
    public static String getServerIp(String domain){
    	String rt = "";
		InetAddress ip = null;
		try {
			ip = InetAddress.getByName(domain);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		if(null != ip){
			rt = ip.toString();
		}
		return rt;
	}
}
