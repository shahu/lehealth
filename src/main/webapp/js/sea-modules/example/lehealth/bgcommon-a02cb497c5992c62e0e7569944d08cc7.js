define("example/lehealth/bgcommon",[],function(a,b){function c(a){var b,c={};c.href=a;for(var e in d)b=d[e].exec(a),c[e]=b[1],a=b[2],""===a&&(a="/"),"pathname"===e&&(c.pathname=b[1],c.search=b[2],c.hash=b[3]);return c}var d={protocol:/([^\/]+:)\/\/(.*)/i,host:/(^[^\:\/]+)((?:\/|:|$)?.*)/,port:/\:?([^\/]*)(\/?.*)/,pathname:/([^\?#]+)(\??[^#]*)(#?.*)/};b.getParams=function(a){var b=window.location.href,d=c(b).search;console.info("params:"+d);var e={};if(d)for(var f=d.split("&"),g=0;g<f.length;g++){var h=f[g].split("=");2==h.length&&(e[h[0]]=decodeURIComponent(h[1]))}return e[a]},b.getCookieByKey=function(a){var b=document.cookie.split("; "),c={};for(var d in b){var e=b[d].split("=");2==e.length&&(c[e[0]]=decodeURIComponent(e[1]))}return c[a]},b.setCookie=function(a,b){var c=new Date;c.setTime(c.getTime()+31536e6),document.cookie=a+"="+escape(b)+";expires="+c.toGMTString()},b.arrayEqual=function(a,b){if(!a||!b)return a||b?!1:!0;if(a.length==b.length){for(var c=0;c<a.length;c++){for(var d=!1,e=0;e<b.length;e++)a[c]==b[e]&&(d=!0);if(!d)return!1}return!0}return!1}});
