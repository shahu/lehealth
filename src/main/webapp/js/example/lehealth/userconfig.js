define(function(require, exports, module) {

	var $ = require('jquery_mobile');
	var util = require('./common');

	exports.render = function() {
		// $(document).bind("pageinit", function() {
		// 	util.hideAddressBar();
		// });
	};

	exports.bindEvent = function() {

		$(document).off("pageshow", "#userconfigpage");

		$(document).on("pageshow", "#userconfigpage", function() {
			$("#userconfigcover").css("display", "none");

		});
	};

});