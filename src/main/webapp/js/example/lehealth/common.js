define(function(require, exports, module) {

	var $ = require("jquery_mobile");
	//这个方法要在页面加载完成后调用，用来隐藏手机上的地址栏
	exports.hideAddressBar = function() {
		return;
		// if (document.documentElement.scrollHeight <= document.documentElement.clientHeight) {
		// 	bodyTag = document.getElementsByTagName('body')[0];
		// 	bodyTag.style.height = document.documentElement.clientWidth / screen.width * screen.height + 'px';
		// }
		// setTimeout(function() {
		// 	window.scrollTo(0, 1)
		// }, 0);
	};

	var r = {
		protocol: /([^\/]+:)\/\/(.*)/i,
		host: /(^[^\:\/]+)((?:\/|:|$)?.*)/,
		port: /\:?([^\/]*)(\/?.*)/,
		pathname: /([^\?#]+)(\??[^#]*)(#?.*)/
	};

	function parseUrl(url) {
		var tmp, res = {};
		res["href"] = url;
		for (var p in r) {
			tmp = r[p].exec(url);
			res[p] = tmp[1];
			url = tmp[2];
			if (url === "") {
				url = "/";
			}
			if (p === "pathname") {
				res["pathname"] = tmp[1];
				res["search"] = tmp[2];
				res["hash"] = tmp[3];
			}
		}
		return res;
	};

	exports.getParams = function(key) {
		var url = window.location.href;
		var params = parseUrl(url)["search"];
		console.info("params:" + params);
		var paramsMap = {};
		if (params) {
			var array = params.split("&");
			for (var i = 0; i < array.length; i++) {
				var kvs = array[i].split("=");
				if (kvs.length == 2) {
					paramsMap[kvs[0]] = decodeURIComponent(kvs[1]);
				}
			}
		}
		return paramsMap[key];
	};

	exports.getCookieByKey = function(key) {
		var cookieArr = document.cookie.split("; ");
		var cookieMap = {};
		for (var i in cookieArr) {
			var kvs = cookieArr[i].split("=");
			if (kvs.length == 2) {
				cookieMap[kvs[0]] = decodeURIComponent(kvs[1]);
			}
		}
		return cookieMap[key];
	};

	exports.setCookie = function(key, value) {
		var exp = new Date(); //获得当前时间  
		exp.setTime(exp.getTime() + 365 * 24 * 60 * 60 * 1000); //换成毫秒  
		document.cookie = key + "=" + escape(value) + ";expires=" + exp.toGMTString();
	};

	//提示信息
	exports.showDialog = function(msg, pagename) {
		$("#" + pagename + " .dialog_text").html("<p>" + msg + "</p>");
		$("#" + pagename + " .dialog").popup();
		$("#" + pagename + " .dialog").popup("open");
	};

	exports.dismissDialog = function(pagename) {
		$("#" + pagename + " .dialog").popup("close");
	};

	exports.toast = function(msg) {
		$("<div class='ui-loader ui-overlay-shadow ui-body-f ui-corner-all' style='background-color:#f9f9f9'><h3>" + msg + "</h3></div>")
			.css({
				display: "block",
				opacity: 0.90,
				position: "fixed",
				padding: "7px",
				"text-align": "center",
				width: "270px",
				left: ($(window).width() - 284) / 2,
				top: $(window).height() / 2 - 30
			})
			.appendTo($.mobile.pageContainer).delay(1500)
			.fadeOut(400, function() {
				$(this).remove();
			});
	};

	exports.arrayEqual = function(arr1, arr2) {
		if(arr1 && arr2) {
			if(arr1.length == arr2.length) {
				for(var i = 0; i < arr1.length; i++) {
					var found = false;
					for(var j = 0; j < arr2.length; j++) {
						if(arr1[i] == arr2[j]) {
							found = true;
						}
					}
					if(!found) {
						return false;
					}
				}
				return true;
			}
		} else if(!arr1 && !arr2) {
			return true;
		} else {
			return false;
		}
		return false;
	};

});