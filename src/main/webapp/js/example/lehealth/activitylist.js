define(function(require, exports, module) {

	var $ = require('jquery_mobile');
	var util = require("./common");

	var activityUrl = "/lehealth/api/activities.do";

	exports.render = function() {

		function getTimeStr(start, end) {
			var str = "";
			str += new Date(start).getMonth() + '月' + new Date(start).getDate() + '日 - '
				+ new Date(end).getMonth() + '月' + new Date(end).getDate() + '日';
			return str;
		}

		$(document).off("pageshow", "#activitylist");

		$(document).on("pageshow", "#activitylist", function() {

			$.ajax({
				url: activityUrl,
				type: "GET",
				dataType: "json",
				async: true,
				success: function(rspData) {
					if (rspData.errorcode) {
						util.toast("获取数据失败，请刷新界面");
					} else {
						var list = rspData.result;
						var html = "";
						for(var i = 0; i < list.length; i++) {
							var actobj = list[i];
							var seperator = "";//(i == (list.length -1))? "" : '<div style="border-bottom:#888 1px solid"></div>';
							var timestr = getTimeStr(actobj.starttime, actobj.endtime);
							html += '<li><a id="acturl" href="' + actobj.externalurl
							 + '" data-transition="slide" style="padding:5px 10px;"><h2 id="actname">' 
							 + actobj.name
							 + '</h2><p id="actdesc" style="margin: 10px 0px; font-size: 14px;width:100%;word-wrap:break-word;">'
							 + actobj.desc
							 + '</p><div style="font-size:12px;">时间：<span id="acttime">'
							 + timestr
							 + '</span>&nbsp;&nbsp;地点：<span id="actloc">'
							 + actobj.location
							 + '</span></div></a>'
							 + seperator 
							 + '</li>';
						}
						$('#listwraper').empty();
						$('#listwraper').html(html);
						$('#listwraper').listview("refresh");						

						$("#activitycover").css("display", "none");
					}
				},
				error: function(xhr, errormsg) {
					util.toast("获取数据失败，请刷新界面");
				}
			});


		});
	};

});