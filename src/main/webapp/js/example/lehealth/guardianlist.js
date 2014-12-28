define(function(require, exports, module) {

	var $ = require('jquery_mobile');
	var util = require('./common');

	var getguardianApi = "/lehealth/api/guardian/list";
	var delguardianApi = "/lehealth/api/guardian/delete";

	exports.render = function() {

		$(document).off("pageshow", "#guardianlist");
		$(document).on("pageshow", "#guardianlist", function() {

			var username = util.getCookieByKey("loginid"),
				token = util.getCookieByKey("tk");

			$("#guardianlistcover").css("display", "none");
			

			var username = util.getCookieByKey("loginid"),
				token = util.getCookieByKey("tk");

			$.ajax({
				url: getguardianApi,
				type: "GET",
				dataType: "json",
				data: {
					loginid: username,
					token: token
				},				
				async: true,
				success: function(rspData) {
					if (rspData.errorcode) {
						if(rspData.errorcode !== 3) {
							util.toast("获取数据失败，请刷新界面");
						}
					} else {
						var ids = {};
						var results = rspData.result;
						var html = "";
						for (var i = 0; i < results.length; i++) {
							var guardian = results[i];
							var seperator = "";//(i == (results.length -1))? "" : '<div style="border-bottom:#888 1px solid"></div>';
							html += '<li data-icon="delete"><a href="#" ><span>' 
								+ guardian.guardianname 
								+ '</span>&nbsp;&nbsp;<span>手机：' 
								+ guardian.guardiannumber 
								+'</span>&nbsp;&nbsp;</a><a href="#" class="delitem" phone="'+guardian.guardiannumber +'" ></a></li>';
						}
						$('#mylistwraper').empty();
						$('#mylistwraper').html(html);
						$('#mylistwraper').listview("refresh");
					}

				},
				error: function(xhr, errormsg) {
					util.toast("获取数据失败，请刷新界面");
				}
			});

			$('#mylistwraper').off('click', '.delitem');
			$('#mylistwraper').on('click', '.delitem', function() {
				var guardiannumber = $(this).attr('phone');
				var that = this;
				$.ajax({
					url: delguardianApi,
					type: "POST",
					dataType: "json",
					data: {
						loginid: username,
						token: token,
						guardiannumber: guardiannumber
					},				
					async: true,
					success: function(rspData) {
						if (rspData.errorcode) {
							if(rspData.errorcode !== 3) {
								util.toast("获取数据失败，请刷新界面");
							}
						} else {
							util.toast("删除成功");
							$(that).parent().remove();
							$('#mylistwraper').listview("refresh");
						}

					},
					error: function(xhr, errormsg) {
						util.toast("获取数据失败，请刷新界面");
					}					
				});
			});

		});
	};

});