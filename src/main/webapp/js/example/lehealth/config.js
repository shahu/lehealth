define(function(require, exports, module) {

	var $ = require('jquery_mobile');
	var util = require('./common');

	exports.render = function() {
		$(document).bind("pageinit", function() {
			util.hideAddressBar();
		});
	};

	exports.bindEvent = function() {
		$(document).bind("pageinit", function() {
			$("#bpconfig").on('click', function(event) {

			});
			$("#medicationconfig").on('click', function(event) {

			});
			$("#userconfig").on('click', function(event) {
				console.info("喵帕斯");
			});
			$("#machineconfig").on('click', function(event) {
				console.info("喵帕斯");
			});
		});
	};

});