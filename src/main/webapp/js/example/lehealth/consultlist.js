define(function(require, exports, module) {

	var $ = require('jquery_mobile');
	var util = require('./common');

	var getDoctorListUrl = "/lehealth/api/doctors.do";

	var myDoctorListUrl = "/lehealth/api/attentiondoctors.do";

	exports.render = function() {

		$(document).off("pageshow", "#doctorlist");
		$(document).on("pageshow", "#doctorlist", function() {

			var username = util.getCookieByKey("loginid"),
				token = util.getCookieByKey("tk");

			$("#doctorlistcover").css("display", "none");


			var username = util.getCookieByKey("loginid"),
				token = util.getCookieByKey("tk");

			$.ajax({
				url: getDoctorListUrl,
				type: "GET",
				dataType: "json",
				data: {
					loginid: username,
					token: token,				
				},
				async: true,
				success: function(rspData) {
					if (rspData.errorcode) {
						util.toast("获取数据失败，请刷新界面");
					} else {
						var results = rspData.result;
						var html = "",
							attentionhtml = "";
						for (var i = 0; i < results.length; i++) {
							var doctor = results[i];
							
							var seperator = "";//(i == (results.length - 1)) ? "" : '<div style="border-bottom:#888 1px solid"></div>';
							var thumbnail = doctor.thumbnail ? doctor.thumbnail : "images/person.jpg";
							if(doctor.attention == 0) {
								html +=
									'<li><a href="/lehealth/expertdetail.html?a=1&status=' + status + '&id=' + doctor.id + '" id="' + doctor.id +
									'" ><img src="' + thumbnail + '" style="height:80px"><h2>' + doctor.name + '</h2><p>' + doctor.desc + '</p></a>' + seperator + '</li>';									
							}
							if(doctor.attention == 1) {
								attentionhtml +=
									'<li><a href="/lehealth/expertdetail.html?a=1&status=' + status + '&id=' + doctor.id + '" id="' + doctor.id +
									'" ><img src="' + thumbnail + '" style="height:80px"><h2>' + doctor.name + '</h2><p>' + doctor.desc + '</p></a>' + seperator + '</li>';									
							}							
							
						}
						$('#listwraper').empty();
						$('#listwraper').html(html);
						$('#listwraper').listview("refresh");

						$('#mylistwraper').empty();
						$('#mylistwraper').html(attentionhtml);
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