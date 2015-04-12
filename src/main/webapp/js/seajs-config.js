/*map start*/
seajs.production = true;
if(seajs.production){
    seajs.config({
        map : [
	[
		"lehealth/activitylist.js",
		"lehealth/activitylist-79e9b4ecdb3fa4b7c5968f962b3091ed.js"
	],
	[
		"lehealth/addguard.js",
		"lehealth/addguard-5e78bb2a1a046e7e901e343ced64c616.js"
	],
	[
		"lehealth/bgcommon.js",
		"lehealth/bgcommon-a02cb497c5992c62e0e7569944d08cc7.js"
	],
	[
		"lehealth/bglogin.js",
		"lehealth/bglogin-bc1846d007df76becf10f0aade4e34b8.js"
	],
	[
		"lehealth/bpconfig.js",
		"lehealth/bpconfig-42fec755427995e647f65d18c9d79442.js"
	],
	[
		"lehealth/bpinput.js",
		"lehealth/bpinput-37a569add945201a455d93074a4cfff7.js"
	],
	[
		"lehealth/bpmonitor.js",
		"lehealth/bpmonitor-acaa8170dfb63d9cb679ae287c4c561a.js"
	],
	[
		"lehealth/common.js",
		"lehealth/common-b3ed7b948a2873a69d0da6599dee549a.js"
	],
	[
		"lehealth/config.js",
		"lehealth/config-2ae531004ea74ac107e15e88fc843871.js"
	],
	[
		"lehealth/consultlist.js",
		"lehealth/consultlist-f251f0f0bc6372750ed1fb32c2c226c1.js"
	],
	[
		"lehealth/expertdetail.js",
		"lehealth/expertdetail-634b83a7b4f5c133cf756d3e7b276db6.js"
	],
	[
		"lehealth/guardianlist.js",
		"lehealth/guardianlist-1cb92360bf924a8a775bb8b3a4f88d58.js"
	],
	[
		"lehealth/home.js",
		"lehealth/home-ccf1a1d963f3bd81c50a4b252c44d139.js"
	],
	[
		"lehealth/login.js",
		"lehealth/login-a334439a81f867a651a51abdc5033d95.js"
	],
	[
		"lehealth/medicalhistory.js",
		"lehealth/medicalhistory-4aae6d2d46730abb4a13a9407ed6d296.js"
	],
	[
		"lehealth/medicalhistorydetail.js",
		"lehealth/medicalhistorydetail-9d2a55c136cc69e7beacde4be2503eaa.js"
	],
	[
		"lehealth/medicationconfig.js",
		"lehealth/medicationconfig-e9f943a752be3b25e8c97d73757429de.js"
	],
	[
		"lehealth/medicationdelsetting.js",
		"lehealth/medicationdelsetting-779acde24e7f397de9a52bf78d904af1.js"
	],
	[
		"lehealth/medicationinput.js",
		"lehealth/medicationinput-744bb90bed2fe05be05631abe1ddd5b9.js"
	],
	[
		"lehealth/medicationrecord.js",
		"lehealth/medicationrecord-ef1af68e6c1bcdc62d2f4737f75a879e.js"
	],
	[
		"lehealth/medicationsetting.js",
		"lehealth/medicationsetting-2f0fcd236e8570c67505feecec5a5431.js"
	],
	[
		"lehealth/register.js",
		"lehealth/register-ab06695ab07dccf89910f21f2c88f391.js"
	],
	[
		"lehealth/userconfig.js",
		"lehealth/userconfig-1e637b07de56deb350f38b73292fcc32.js"
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

if(seajs.production) {
	seajs.prefix = '';
} else {
	seajs.prefix = '/lehealth/js/';
}