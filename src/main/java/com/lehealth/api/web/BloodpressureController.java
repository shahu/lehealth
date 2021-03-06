package com.lehealth.api.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lehealth.api.entity.BloodpressureConfig;
import com.lehealth.api.entity.BloodpressureRecord;
import com.lehealth.api.entity.BloodpressureResult;
import com.lehealth.api.entity.UserBaseInfo;
import com.lehealth.api.service.BloodpressureService;
import com.lehealth.api.service.LoginService;
import com.lehealth.data.type.ErrorCodeType;
import com.lehealth.response.bean.BaseResponse;
import com.lehealth.response.bean.JsonObjectResponse;

@Controller
@RequestMapping("/api/bp")
public class BloodpressureController {

	@Autowired
	@Qualifier("loginService")
	private LoginService loginService;

	@Autowired
	@Qualifier("bloodpressureService")
	private BloodpressureService bloodpressureService;

	// 患者获取自己血压数据
	@ResponseBody
	@RequestMapping(value = "/record/list", method = RequestMethod.GET)
	public JSONObject getBpRecords(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		String loginId = StringUtils.trimToEmpty(request
				.getParameter("loginid"));
		String token = StringUtils.trimToEmpty(request.getParameter("token"));
		UserBaseInfo user = this.loginService.getUserByToken(loginId, token);
		if (user != null) {
			int days = NumberUtils.toInt(request.getParameter("days"), 7);
			if (days == 0) {
				days = 7;
			}
			BloodpressureResult result = this.bloodpressureService.getRecords(
					user.getUserId(), days);
			return new JsonObjectResponse(ErrorCodeType.success,
					result.toJsonObj()).toJson();
		} else {
			return new BaseResponse(ErrorCodeType.invalidToken).toJson();
		}
	}

	// 患者录入自己血压数据
	@ResponseBody
	@RequestMapping(value = "/record/add", method = RequestMethod.POST)
	public JSONObject addBpRecord(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		String loginId = StringUtils.trimToEmpty(request
				.getParameter("loginid"));
		String token = StringUtils.trimToEmpty(request.getParameter("token"));
		UserBaseInfo user = this.loginService.getUserByToken(loginId, token);
		if (user != null) {
			int dbp = NumberUtils.toInt(request.getParameter("dbp"));
			int sbp = NumberUtils.toInt(request.getParameter("sbp"));
			int heartrate = NumberUtils
					.toInt(request.getParameter("heartrate"));
			int dosed = NumberUtils.toInt(request.getParameter("dosed"));// 0未服1已服
			long date = NumberUtils.toLong(request.getParameter("date"));
			if (date == 0) {
				date = System.currentTimeMillis();
			}
			BloodpressureRecord bpInfo = new BloodpressureRecord();
			bpInfo.setUserId(user.getUserId());
			bpInfo.setDbp(dbp);
			bpInfo.setSbp(sbp);
			bpInfo.setHeartrate(heartrate);
			bpInfo.setDate(date);
			bpInfo.setDosed(dosed);
			if (this.bloodpressureService.addRecord(bpInfo, loginId)) {
				return new BaseResponse(ErrorCodeType.success).toJson();
			} else {
				return new BaseResponse(ErrorCodeType.failed).toJson();
			}
		} else {
			return new BaseResponse(ErrorCodeType.invalidToken).toJson();
		}
	}

	// 患者录入自己血压数据
	@ResponseBody
	@RequestMapping(value = "/record/remove", method = RequestMethod.GET)
	public JSONObject delBpRecord(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		String loginId = StringUtils.trimToEmpty(request
				.getParameter("loginid"));
		String token = StringUtils.trimToEmpty(request.getParameter("token"));
		UserBaseInfo user = this.loginService.getUserByToken(loginId, token);
		if (user != null) {
			String id = StringUtils.trimToEmpty(request.getParameter("id"));
			
			if (this.bloodpressureService.delRecord(id)) {
				return new BaseResponse(ErrorCodeType.success).toJson();
			} else {
				return new BaseResponse(ErrorCodeType.failed).toJson();
			}
		} else {
			return new BaseResponse(ErrorCodeType.invalidToken).toJson();
		}
	}

	// 患者获取自己血压控制设置
	@ResponseBody
	@RequestMapping(value = "/setting/info", method = RequestMethod.GET)
	public JSONObject getBpSetting(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		String loginId = StringUtils.trimToEmpty(request
				.getParameter("loginid"));
		String token = StringUtils.trimToEmpty(request.getParameter("token"));
		UserBaseInfo user = this.loginService.getUserByToken(loginId, token);
		if (user != null) {
			BloodpressureConfig bpConfig = this.bloodpressureService
					.getConfig(user.getUserId());
			if (StringUtils.isBlank(bpConfig.getUserId())) {
				return new BaseResponse(ErrorCodeType.failed).toJson();
			} else {
				return new JsonObjectResponse(ErrorCodeType.success,
						bpConfig.toJsonObj()).toJson();
			}
		} else {
			return new BaseResponse(ErrorCodeType.invalidToken).toJson();
		}
	}

	// 患者更新自己血压控制设置
	@ResponseBody
	@RequestMapping(value = "/setting/modify", method = RequestMethod.POST)
	public JSONObject modifyBpSetting(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		String loginId = StringUtils.trimToEmpty(request
				.getParameter("loginid"));
		String token = StringUtils.trimToEmpty(request.getParameter("token"));
		UserBaseInfo user = this.loginService.getUserByToken(loginId, token);
		if (user != null) {
			int dbp1 = NumberUtils.toInt(request.getParameter("dbp1"));
			int dbp2 = NumberUtils.toInt(request.getParameter("dbp2"));
			int sbp1 = NumberUtils.toInt(request.getParameter("sbp1"));
			int sbp2 = NumberUtils.toInt(request.getParameter("sbp2"));
			int heartrate1 = NumberUtils.toInt(request
					.getParameter("heartrate1"));
			int heartrate2 = NumberUtils.toInt(request
					.getParameter("heartrate2"));
			BloodpressureConfig bpConfig = new BloodpressureConfig();
			bpConfig.setUserId(user.getUserId());
			bpConfig.setDbp1(dbp1);
			bpConfig.setDbp2(dbp2);
			bpConfig.setSbp1(sbp1);
			bpConfig.setSbp2(sbp2);
			bpConfig.setHeartrate1(heartrate1);
			bpConfig.setHeartrate2(heartrate2);
			if (this.bloodpressureService.modifyConfig(bpConfig)) {
				return new BaseResponse(ErrorCodeType.success).toJson();
			} else {
				return new BaseResponse(ErrorCodeType.failed).toJson();
			}
		} else {
			return new BaseResponse(ErrorCodeType.invalidToken).toJson();
		}
	}

}
