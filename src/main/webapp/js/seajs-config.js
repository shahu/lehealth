/*map start*/
seajs.production = true;
if(seajs.production){
    seajs.config({
        map : [
	[
		"lehealth/index.js",
		"lehealth/index-3f5ae298f62ba713ae46c7398e10005e.js"
	]
]
    });
}
/*map end*/
seajs.config({

	alias: {
		jquery: 'jquery/jquery/1.7.2/jquery',
		jquery_mobile: 'jquery-mobile/1.4.4/jquery.mobile'
	}

});
