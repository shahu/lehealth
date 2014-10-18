define(function(require, exports, module) {

	var $ = require('jquery_mobile');
	exports.render = function() {
		$('body').append('Some text');
	};

});