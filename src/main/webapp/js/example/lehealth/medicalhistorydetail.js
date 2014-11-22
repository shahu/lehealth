define(function(require, exports, module) {

	var $ = require('jquery_mobile');
	var util = require('./common');

	var historyApi = "/lehealth/api/diseasehistory.do";
	var diseaseApi = "/lehealth/api/diseases.do";

	exports.bindEvent = function() {

		$(document).off("pageshow", "#historydetailpage");

		$(document).on("pageshow", "#historydetailpage", function() {
			$("#historydetailcover").css("display", "none");

			function getAndSetUserConfigData() {
				var username = util.getCookieByKey("loginid"),
					token = util.getCookieByKey("tk");
				$.ajax({
					url: historyApi,
					type: "GET",
					dataType: "json",
					async: true,
					data: {
						loginid: username,
						token: token,
						diseaseid: util.getParams('diseaseId')
					},
					success: function(rspData) {
						if (rspData.errorcode) {
							if (rspData.errorcode === 1) {
								util.setCookie("jump", "/lehealth/config.html");
								$.mobile.changePage("/lehealth/login.html", "slide");
								return;
							}
							if (rspData.errorcode == 3) {
								return; //服务端无数据，采用默认数据
							}
							util.toast("获取数据失败，请刷新界面");
						} else {
							$('#diseaselist').val(rspData.result.diseaseId);
							$('#diseaselist').selectmenu("refresh");
							$('#diseasedescription').text(rspData.result.diseasedescription);
							$('#medicinedescription').text(rspData.result.medicinedescription);
						}
					},
					error: function(xhr, errormsg) {
						util.toast("获取数据失败，请刷新界面");
					}
				});
			}
			if (util.getParams('diseaseId')) {
				getAndSetUserConfigData();
			}
			$.ajax({
				url: diseaseApi,
				type: "GET",
				dataType: "json",
				async: true,
				success: function(rspData) {
					if (rspData.errorcode) {
						util.toast("获取数据失败，请刷新界面");
					} else {
						var result = rspData.result;
						
						var option = '';
						for(var j = 0; j < result.length; j++) {
							option += '<optgroup label="' + result[j].catename + '">';
							for(var i = 0; i < result[j]['diseases'].length; i++) {
								option += '<option value="' + result[j]['diseases'][i].id + '">'+ result[j]['diseases'][i].name +'</option>';	
							}
							option += ' </optgroup>';
						}
						$('#diseaselist').html(option);
						$('#diseaselist').selectmenu("refresh");
					}
				},
				error: function(xhr, errormsg) {
					util.toast("获取数据失败，请刷新界面");
				}
			});


			$("#save_history").off('click');
			$("#save_history").on('click', function(event) {
				var loginid = util.getCookieByKey("loginid"),
					token = util.getCookieByKey("tk");
					

				$.ajax({
					url: historyApi,
					type: "POST",
					dataType: "json",
					async: true,
					data: {
						loginid: loginid,
						token: token,
						diseaseid: $('#diseaselist').val(),
						diseasedescription: $('#diseasedescription').val(),
						medicinedescription: $('#medicinedescription').val()
					},
					success: function(rspData) {
						if (rspData.errorcode) {
							if (rspData.errorcode === 1) {
								util.setCookie("jump", "/lehealth/bpconfig.html");
								$.mobile.changePage("/lehealth/login.html", "slide");
								return;
							}
							util.toast("提交数据失败，请重新提交");
						} else {
							util.toast("提交成功");
						}
					},
					error: function(xhr, errormsg) {
						util.toast("提交数据失败，请重新提交");
					}
				});
			});

		});
	};

});