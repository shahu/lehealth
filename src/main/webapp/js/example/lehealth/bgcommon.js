define(function(require, exports, module) {

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