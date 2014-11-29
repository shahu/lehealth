define(function(require, exports, module) {

	var $ = require('jquery_mobile');
	var util = require('./common');

	var historyApi = "/lehealth/api/diseasehistorys.do";

	exports.render = function() {

		$(document).off("pageshow", "#historylist");
		$(document).on("pageshow", "#historylist", function() {

			var username = util.getCookieByKey("loginid"),
				token = util.getCookieByKey("tk");

			$("#historylistcover").css("display", "none");
			

			var username = util.getCookieByKey("loginid"),
				token = util.getCookieByKey("tk");

			$.ajax({
				url: historyApi,
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

						var results = rspData.result;
						var html = "";
						for (var i = 0; i < results.length; i++) {
							var historyobj = results[i];
							var seperator = "";//(i == (results.length -1))? "" : '<div style="border-bottom:#888 1px solid"></div>';
							html += '<li><a href="/lehealth/medicalhistorydetail.html?a=1&diseaseId=' 
								+ historyobj.diseaseid + '"><span>' 
								+ historyobj.diseasename 
								+ '</span></li>';
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


		});
	};

});