/*map start*/
seajs.production = true;
if(seajs.production) {
	seajs.prefix = '';
} else {
	seajs.prefix = '/lehealth/js/'
}
if(seajs.production){
    seajs.config({
        map : [
	[
		"lehealth/activitylist.js",
		"lehealth/activitylist-5148d5608a621c4c1916f144777989d8.js"
	],
	[
		"lehealth/bpinput.js",
		"lehealth/bpinput-9d985ba66e37ae36d8ac5a949d7b27fb.js"
	],
	[
		"lehealth/bpmonitor.js",
		"lehealth/bpmonitor-cb46e27f408ac3a44d6379b39ceb86fa.js"
	],
	[
		"lehealth/consultlist.js",
		"lehealth/consultlist-6a6a59cb6f8e00d4b9499ae256d4a104.js"
	],
	[
		"lehealth/expertdetail.js",
		"lehealth/expertdetail-9c924b6246d928cf901f8243518ecbbd.js"
	],
	[
		"lehealth/medicationinput.js",
		"lehealth/medicationinput-3478eedf4126b1653b98c7ca73162de7.js"
	],
	[
		"lehealth/medicationrecord.js",
		"lehealth/medicationrecord-6cb88fb5baa20ea0e2ef64df11d71c48.js"
	]
]
    });
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