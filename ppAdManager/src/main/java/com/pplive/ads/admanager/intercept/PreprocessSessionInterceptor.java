/**
 * 
 */
package com.pplive.ads.admanager.intercept;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.pplive.web.ad.comm.Util;
import com.util.SuperString;

public class PreprocessSessionInterceptor extends HandlerInterceptorAdapter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle
	 * (javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse, java.lang.Object)
	 * 
	 * 对所有发过来的请求先检验session是否有效，如果无效则使用cookie填充或者重新登陆，避免系统出现500的错误
	 */
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		HttpSession session = request.getSession();
		if (session.getAttribute("admin_name") == null) {
			String uri = SuperString.notNullTrim(request.getRequestURI());
			if (!uri.startsWith("/"))
				uri = "/" + uri;
			boolean isroot = true;// 判断应用是否在根目录下
			String appname = SuperString.notNullTrim(Util
					.getProperties("WEB_APP_NAME"));
			if (!appname.equals("") && !appname.equals("/"))
				isroot = false;
			if (!appname.startsWith("/")) {
				appname = "/" + appname;
			}
			if (!isroot) {
				int index = uri.indexOf(appname);
				uri = uri.substring(index + appname.length());
			}
			response.sendRedirect((uri.lastIndexOf("/") > 0 ? "../" : "")
					+ "rlogin.do?isAutoLogin=on&url="
					+ com.pplive.web.ad.comm.Util
							.encodeUrl(com.pplive.web.ad.comm.Util
									.encodeDes(uri
											+ com.util.SuperPage.getQueryQ(
													request, ""))));
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
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
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#
	 * afterCompletion(javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse, java.lang.Object,
	 * java.lang.Exception)
	 */
	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

}
