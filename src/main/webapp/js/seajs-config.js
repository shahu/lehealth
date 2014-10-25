/*map start*/
seajs.production = false;
if(seajs.production){
    seajs.config({
        map : [
	[
		"lehealth/activitylist.js",
		"lehealth/activitylist-5148d5608a621c4c1916f144777989d8.js"
	],
	[
		"lehealth/bpconfig.js",
		"lehealth/bpconfig-81f6fab42ea0042c1d565ac4eace89ce.js"
	],
	[
		"lehealth/bpinput.js",
		"lehealth/bpinput-d56759c5b758a3c79fac1c3a74507cd6.js"
	],
	[
		"lehealth/bpmonitor.js",
		"lehealth/bpmonitor-b135a68bfaf8cdbdf52eee38107c1f9a.js"
	],
	[
		"lehealth/common.js",
		"lehealth/common-53e04aa01fd3373885207aaa3c0a377f.js"
	],
	[
		"lehealth/config.js",
		"lehealth/config-898392d2b79e41404fc12f00fca68e21.js"
	],
	[
		"lehealth/consultlist.js",
		"lehealth/consultlist-f048f5cb09cc7bf1bfa96996f933baeb.js"
	],
	[
		"lehealth/expertdetail.js",
		"lehealth/expertdetail-9c924b6246d928cf901f8243518ecbbd.js"
	],
	[
		"lehealth/medicationconfig.js",
		"lehealth/medicationconfig-67eb005f282032f93fe55b57188aeeb1.js"
	],
	[
		"lehealth/medicationinput.js",
		"lehealth/medicationinput-283503f702da3666ef5c3e0372c59b3c.js"
	],
	[
		"lehealth/medicationrecord.js",
		"lehealth/medicationrecord-50a134d76a7604df45a05d1b5d47de14.js"
	],
	[
		"leheanth/login.js",
		"leheanth/login-d41d8cd98f00b204e9800998ecf8427e.js"
	]
]
    });
}

if(seajs.production) {
	seajs.prefix = '';
} else {
	seajs.prefix = '/lehealth/js/'
}
/*map end*/
seajs.config({

	alias: {
		jquery: 'jquery/jquery/1.7.2/jquery',
		jquery_mobile: 'jquery-mobile/1.4.4/jquery.mobile',
		highcharts: 'highcharts/4.0.3/highcharts-all',
		highcharts_theme: 'highcharts/4.0.3/highchartsTheme'
	}

});