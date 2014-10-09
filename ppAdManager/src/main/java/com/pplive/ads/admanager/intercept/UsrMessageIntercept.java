package com.pplive.ads.admanager.intercept;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.pplive.adcontrol.util.IpUtil;
import com.pplive.web.ad.comm.ThreadVar;

public class UsrMessageIntercept extends HandlerInterceptorAdapter {

	@SuppressWarnings("unchecked")
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		HttpSession session = request.getSession();
		String userName = "未知用户";
		if (session.getAttribute("admin_name") != null) {
			userName = session.getAttribute("admin_name").toString();
		}
		
		String ip = IpUtil.getIp(request);
		if (ip == null) {
			ip = "0.0.0.0";
		}
		;
		List<String> perGroupIds = new ArrayList<String>();
		if (session.getAttribute("groupIds") != null) {
			perGroupIds = (List<String>) session.getAttribute("groupIds");
		}
		boolean isAdmin = false;
		if (session.getAttribute("adminpermission") != null) {
			isAdmin = (Boolean) session.getAttribute("adminpermission");
		}
		String bid = "n/a";
		if(session.getAttribute("browserid") != null) {
			bid = (String)session.getAttribute("browserid");
		}
		ThreadVar.clear();
		ThreadVar.setValue("perGroupIds", perGroupIds);
		ThreadVar.setValue("isAdmin", isAdmin);
		ThreadVar.setValue("ip", ip);
		ThreadVar.setValue("userName", userName);
		ThreadVar.setValue("browserid", bid);
		return true;
	}

	/*
	 * Will be called after the handler is executed (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.servlet.handler.HandlerInterceptorAdapter#postHandle
	 * (javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse, java.lang.Object,
	 * org.springframework.web.servlet.ModelAndView)
	 */
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	/*
	 * 
	 * It is called after the complete request has finished 
	 * (non-Javadoc)
	 * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#afterCompletion(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
	 */
	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}
}