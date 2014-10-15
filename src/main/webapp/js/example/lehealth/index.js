define(function(require, exports, module) {

	var $ = require('jquery');
	exports.render = function() {
		$('body').append('Some text');
	};

});