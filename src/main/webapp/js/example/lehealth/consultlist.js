define(function(require, exports, module) {

	var $ = require('jquery_mobile');
	var util = require('./common');

	var getDoctorListUrl = "/lehealth/api/doctors.do";

	exports.render = function() {

		$(document).off("pageshow", "#doctorlist");
		$(document).on("pageshow", "#doctorlist", function() {

			$("body").css("display", "inline");
			util.hideAddressBar();

			var username = util.getCookieByKey("loginid"),
				token = util.getCookieByKey("tk");
			$.ajax({
				url: getDoctorListUrl,
				type: "GET",
				dataType: "json",
				async: true,
				success: function(rspData) {
					if (rspData.errorcode) {
						util.toast("获取数据失败，请刷新界面");
					} else {
						var results = rspData.result;
						var html = "";
						for (var i = 0; i < results.length; i++) {
							var doctor = results[i];
							var seperator = (i == (results.length -1))? "" : '<div style="border-bottom:#888 1px solid"></div>';
							var thumbnail = doctor.thumbnail? doctor.thumbnail : "images/person.jpg";
							html += 
								'<li><a href="/lehealth/expertdetail.html?a=1&id=' + doctor.id + '" id="' 
									+ doctor.id + 
									'" ><img src="' + thumbnail + '" style="height:80px"><h2>' 
									+ doctor.name 
									+ '</h2><p>' + doctor.desc + '</p></a>' + seperator + '</li>';
						}
						$('#listwraper').empty();
						$('#listwraper').html(html);
						$('#listwraper').listview("refresh");
					}
				},
				error: function(xhr, errormsg) {
					util.toast("获取数据失败，请刷新界面");
				}
			});


		});
	};

});