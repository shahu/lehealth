define(function(require, exports, module) {

	var $ = require('jquery_mobile');
	var util = require('./common');

	var medicineConfigUrl = "/api/medicinesetting";

	exports.render = function() {
		$(document).bind("pageinit", function() {
			util.hideAddressBar();

			
		});
	};

	exports.bindEvent = function() {
		$(document).bind("pageinit", function() {
			
		});
	};

});