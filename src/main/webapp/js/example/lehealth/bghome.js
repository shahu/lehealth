define(function(require, exports, module) {

	var $ = require('jquery');
	var util = require('./bgcommon');

	var loginUrl = "/lehealth/api/login.do";

	var username = $('#login_username').val(),
		token = $('#login_pwd').val();

	var roles = {
		"admin": 1,
		"doctor": 2,
		"patient": 4
	};

	exports.render = function() {
		$('#patients a').on('click', function() {
			$('#patients li.active').removeClass('active');
			$(this).parent().addClass('active');

		});

		$('#sideupdown_icon').on('click', function() {
			if($(this).hasClass('icon-chevron-up')) {
				$(this).removeClass('icon-chevron-up');
				$(this).addClass('icon-chevron-down');
				$('#list_div').slideUp('slow', function() {
					
				});
			} else if($(this).hasClass('icon-chevron-down')) {
				$(this).removeClass('icon-chevron-down');
				$(this).addClass('icon-chevron-up');
				$('#list_div').slideDown('slow', function() {
					
				});
			}
		});

		getUserBasicInfo(function(err, role) {
			if(err) {
				alertMsg("请求失败，请稍后重试");
				return;
			}
			if(role == roles.admin) {
				//显示医生录入标签
			} else if(role == roles.doctor) {
				//显示我的病人标签，同时请求第一个病人数据展示
				getPatients(function(err, patientArr) {
					if(err) {
						alertMsg("获取病人列表失败");
						return;
					}
					var firstpatient = patientArr[0];
					if(firstpatient) {
						showPatientInfo(firstpatient, function() {
							console.info("获取成功");
						});
					}
				});
			} else {
				alert("you are not permitted");
			}
		});

	};

	function showPatientInfo(patientInfo, cb) {
		var count = 3;
		getAndRenderPatientInfo(function(err) {
			count--;
			if(count == 0) {
				cb();
			}
		});
		getAndRenderPatientBpData(function(err) {
			count--;
			if(count == 0) {
				cb();
			}			
		});
		getAndRenderDiseaseHistory(function(err) {
			count--;
			if(count == 0) {
				cb();
			}			
		});		
	}

	/**
	 * [getUserBasicInfo 获取登录用户的基础信息]
	 * @param  {Function} cb [description]
	 * @return {[type]}      [description]
	 */
	function getUserBasicInfo(cb) {
		var loginId = util.getCookieByKey("loginid"),
			pwd = util.getCookieByKey("tk");
		$.ajax({
			url: '/lehealth/api/role.do',
			dataType: 'json',
			type: 'post',
			data: {
				loginid: loginId,
				token: pwd
			},
			success: function(rsp) {
				console.info(rsp);
			},
			error: function(xhr, errormsg) {

			}
		});
	}

	/**
	 * [getPatients 获取我的病人信息列表]
	 * @param  {Function} cb [description]
	 * @return {[type]}      [description]
	 */
	function getAndRenderPatients(cb) {

	}

	/**
	 * [getPatientInfo 获取给定病人的个人信息]
	 * @param  {[type]}   patientId [description]
	 * @param  {Function} cb        [description]
	 * @return {[type]}             [description]
	 */
	function getAndRenderPatientInfo(patientId, cb) {

	}

	/**
	 * [getPatientBpData 获取病人的血压和用药信息]
	 * @param  {[type]}   patientId [description]
	 * @param  {Function} cb        [description]
	 * @return {[type]}             [description]
	 */
	function getAndRenderPatientBpData(patientId, cb) {

	}

	/**
	 * [getDiseaseHistory 获取病人的病史信息]
	 * @param  {[type]}   patientId [description]
	 * @param  {Function} cb        [description]
	 * @return {[type]}             [description]
	 */
	function getAndRenderDiseaseHistory(patientId, cb) {

	}

	function alertMsg(msg) {
		alert(msg);
	}

});