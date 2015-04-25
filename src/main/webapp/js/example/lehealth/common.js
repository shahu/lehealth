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

	exports.parseUri = function(url) {
		//url的正则，黑科技勿动
		var urlreg = /^([^:]+):\/\/([0-9a-zA-Z_\.]+)(?::(\d+))?(\/[0-9a-zA-Z_\/\.]*)?(?:\?((?:[^=&#\?]+=[^&#\?]*)?(?:(?:&[^=&#\?]+=[^&#\?]*)*)))?(?:#((?:[^=&#\?]+=[^&#\?]*)?(?:(?:&[^=&#\?]+=[^&#\?]*)*)))?$/;
		var parser = urlreg.exec(url);
		if (parser == null) {
			parser = [];
		}
		var paramMap = {};
		if (parser[5]) {
			var query = parser[5];
			var reg = /([^=&]+)=([^&]*)/g;
			var rs;
			while ((rs = reg.exec(query)) !== null) {
				if (rs && rs.length == 3 && rs[1]) {
					paramMap[rs[1]] = decodeURIComponent(rs[2] ? rs[2] : "");
				}
			}
		}
		console.info(parser);
		return {
			url: url,
			getSchema: function() {
				return parser[1] || "";
			},

			getHost: function() {
				return parser[2] || "";
			},

			getPort: function() {
				return parser[3] || 80;
			},

			getPath: function() {
				return parser[4] || "";
			},

			getQueryStr: function() {
				return parser[5] || "";
			},

			getQueryParameter: function(key) {
				return paramMap[key];
			}
		};
	}	

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
		if (arr1 && arr2) {
			if (arr1.length == arr2.length) {
				for (var i = 0; i < arr1.length; i++) {
					var found = false;
					for (var j = 0; j < arr2.length; j++) {
						if (arr1[i] == arr2[j]) {
							found = true;
						}
					}
					if (!found) {
						return false;
					}
				}
				return true;
			}
		} else if (!arr1 && !arr2) {
			return true;
		} else {
			return false;
		}
		return false;
	};

	exports.sha1 = function(str) {
		// 对str进行UTF-8编码，转成Uint8Array
		var tmp = new Uint8Array(encodeUTF8(str));
		// sha1加密
		var result = encodeSHA1(tmp);
		// Uint8Array转回str
		return Array.prototype.map.call(result, function(e) {
			return (e < 16 ? "0" : "") + e.toString(16);
		}).join("");

		function encodeUTF8(s) {
			var i, r = [],
				c, x;
			for (i = 0; i < s.length; i++) {
				if ((c = s.charCodeAt(i)) < 0x80) {
					r.push(c);
				} else if (c < 0x800) {
					r.push(0xC0 + (c >> 6 & 0x1F), 0x80 + (c & 0x3F));
				} else {
					if ((x = c ^ 0xD800) >> 10 == 0) {
						c = (x << 10) + (s.charCodeAt(++i) ^ 0xDC00) + 0x10000;
						r.push(0xF0 + (c >> 18 & 0x7), 0x80 + (c >> 12 & 0x3F));
					} else {
						r.push(0xE0 + (c >> 12 & 0xF));
					}
					r.push(0x80 + (c >> 6 & 0x3F), 0x80 + (c & 0x3F));
				};
			}
			return r;
		}

		function encodeSHA1(data) {
			var i, j, t;
			var l = ((data.length + 8) >>> 6 << 4) + 16,
				s = new Uint8Array(l << 2);
			s.set(new Uint8Array(data.buffer)), s = new Uint32Array(s.buffer);
			for (t = new DataView(s.buffer), i = 0; i < l; i++) {
				s[i] = t.getUint32(i << 2);
			}
			s[data.length >> 2] |= 0x80 << (24 - (data.length & 3) * 8);
			s[l - 1] = data.length << 3;
			var w = [];
			var f = [

				function() {
					return m[1] & m[2] | ~m[1] & m[3];
				},
				function() {
					return m[1] ^ m[2] ^ m[3];
				},
				function() {
					return m[1] & m[2] | m[1] & m[3] | m[2] & m[3];
				},
				function() {
					return m[1] ^ m[2] ^ m[3];
				}
			];
			var rol = function(n, c) {
				return n << c | n >>> (32 - c);
			};
			var k = [1518500249, 1859775393, -1894007588, -899497514];
			var m = [1732584193, -271733879, null, null, -1009589776];
			m[2] = ~m[0];
			m[3] = ~m[1];
			for (i = 0; i < s.length; i += 16) {
				var o = m.slice(0);
				for (j = 0; j < 80; j++) {
					w[j] = j < 16 ? s[i + j] : rol(w[j - 3] ^ w[j - 8] ^ w[j - 14] ^ w[j - 16], 1);
					t = rol(m[0], 5) + f[j / 20 | 0]() + m[4] + w[j] + k[j / 20 | 0] | 0;
					m[1] = rol(m[1], 30), m.pop(), m.unshift(t);
				}
				for (j = 0; j < 5; j++) {
					m[j] = m[j] + o[j] | 0;
				}
			};
			t = new DataView(new Uint32Array(m).buffer);
			for (var i = 0; i < 5; i++) {
				m[i] = t.getUint32(i << 2);
			}
			return new Uint8Array(new Uint32Array(m).buffer);
		}
	};

	// Support date Date().Format(fmt)
    if (!Date.prototype.format) {
        Date.prototype.format = function(fmt) {
            var o = {
                "M+": this.getMonth() + 1, //月份
                "d+": this.getDate(), //日
                "h+": this.getHours(), //小时
                "m+": this.getMinutes(), //分
                "s+": this.getSeconds(), //秒
                "q+": Math.floor((this.getMonth() + 3) / 3), //季度
                "S": this.getMilliseconds() //毫秒
            };
            if (/(y+)/.test(fmt))
                fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
            for (var k in o)
                if (new RegExp("(" + k + ")").test(fmt))
                    fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
            return fmt;
        };
    }	

});