$(function() {

	var $domainsLi = $('li.organization'),
		domains = $domainsLi.map(function() { return this.dataset['domain'] }).get();
	if (domains.length) {
		$.get('/domains', {'domains': domains.join(",")}, function(data) {
			var i, currentLi,
				domainsLength = data.result.length,
				domains = data.result,
				imgSrc;
			for (i = 0; i < domainsLength; i += 1) {
				currentLi = $domainsLi.filter("li[data-domain='"+domains[i].domainName+"']")[0]
				imgSrc = domains[i].avatarKey ? "/serve/" + domains[i].avatarKey : "http://lorempixum.com/32/32/";
				currentLi.childNodes[1].src = imgSrc;
				currentLi.childNodes[3].textContent = domains[i].name || users[i].userId;
			}
			document.getElementById("loader").remove();
			$domainsLi.fadeIn();
		}).error(function(data) {
			console.log("não foi possível carregar dados dos usuários");
		});
	}

	// /users
	var $usersTable = $("#users");
	$("#user-search").keyup(function(e) {
    	$.uiTableFilter( $usersTable, this.value, "Nome" );
	});

	// used on description textarea
	/**
	  * showdown.js -- A javascript port of Markdown.
	  * Copyright (c) 2007 John Fraser.
	  * Original Markdown Copyright (c) 2004-2005 John Gruber
	  * Redistributable under a BSD-style open source license.
	  */
	var Showdown={};"object"===typeof exports&&(Showdown=exports,Showdown.parse=function(h,i){return(new Showdown.converter).makeHtml(h,i)});var GitHub;
	Showdown.converter=function(){var h,i,o,p=0;this.makeHtml=function(a,c){"undefined"!==typeof c&&("string"===typeof c&&(c={nameWithOwner:c}),GitHub=c);h=[];i=[];o=[];a=a.replace(/~/g,"~T");a=a.replace(/\$/g,"~D");a=a.replace(/\r\n/g,"\n");a=a.replace(/\r/g,"\n");a="\n\n"+a+"\n\n";a=v(a);a=a.replace(/^[ \t]+$/mg,"");a=w(a);a=C(a);a=q(a);a=x(a);a=a.replace(/~D/g,"$$");a=a.replace(/~T/g,"~");a=a.replace(/https?\:\/\/[^"\s\<\>]*[^.,;'">\:\s\<\>\)\]\!]/g,function(b,c){var d=a.slice(0,c),f=a.slice(c);return d.match(/<[^>]+$/)&&
	f.match(/^[^>]*>/)?b:"<a target='blank' href='"+b+"'>"+b+"</a>"});a=a.replace(/[a-z0-9_\-+=.]+@[a-z0-9\-]+(\.[a-z0-9-]+)+/ig,function(a){return"<a href='mailto:"+a+"'>"+a+"</a>"});a=a.replace(/[a-f0-9]{40}/ig,function(b,c){if("undefined"==typeof GitHub||"undefined"==typeof GitHub.nameWithOwner)return b;var d=a.slice(0,c),f=a.slice(c);return d.match(/@$/)||d.match(/<[^>]+$/)&&f.match(/^[^>]*>/)?b:"<a target='blank' href='http://github.com/"+GitHub.nameWithOwner+"/commit/"+b+"'>"+b.substring(0,7)+"</a>"});
	a=a.replace(/([a-z0-9_\-+=.]+)@([a-f0-9]{40})/ig,function(b,c,d,f){if("undefined"==typeof GitHub||"undefined"==typeof GitHub.nameWithOwner)return b;GitHub.repoName=GitHub.repoName||GitHub.nameWithOwner.match(/^.+\/(.+)$/)[1];var j=a.slice(0,f),f=a.slice(f);return j.match(/\/$/)||j.match(/<[^>]+$/)&&f.match(/^[^>]*>/)?b:"<a target='blank' href='http://github.com/"+c+"/"+GitHub.repoName+"/commit/"+d+"'>"+c+"@"+d.substring(0,7)+"</a>"});a=a.replace(/([a-z0-9_\-+=.]+\/[a-z0-9_\-+=.]+)@([a-f0-9]{40})/ig,
	function(a,c,d){return"<a target='blank' href='http://github.com/"+c+"/commit/"+d+"'>"+c+"@"+d.substring(0,7)+"</a>"});a=a.replace(/#([0-9]+)/ig,function(b,c,d){if("undefined"==typeof GitHub||"undefined"==typeof GitHub.nameWithOwner)return b;var f=a.slice(0,d),d=a.slice(d);return""==f||f.match(/[a-z0-9_\-+=.]$/)||f.match(/<[^>]+$/)&&d.match(/^[^>]*>/)?b:"<a target='blank' href='http://github.com/"+GitHub.nameWithOwner+"/issues/#issue/"+c+"'>"+b+"</a>"});a=a.replace(/([a-z0-9_\-+=.]+)#([0-9]+)/ig,
	function(b,c,d,f){if("undefined"==typeof GitHub||"undefined"==typeof GitHub.nameWithOwner)return b;GitHub.repoName=GitHub.repoName||GitHub.nameWithOwner.match(/^.+\/(.+)$/)[1];var j=a.slice(0,f),f=a.slice(f);return j.match(/\/$/)||j.match(/<[^>]+$/)&&f.match(/^[^>]*>/)?b:"<a target='blank' href='http://github.com/"+c+"/"+GitHub.repoName+"/issues/#issue/"+d+"'>"+b+"</a>"});return a=a.replace(/([a-z0-9_\-+=.]+\/[a-z0-9_\-+=.]+)#([0-9]+)/ig,function(a,c,d){return"<a target='blank' href='http://github.com/"+
	c+"/issues/#issue/"+d+"'>"+a+"</a>"})};var C=function(a){return a=a.replace(/^[ ]{0,3}\[(.+)\]:[ \t]*\n?[ \t]*<?(\S+?)>?[ \t]*\n?[ \t]*(?:(\n*)["(](.+?)[")][ \t]*)?(?:\n+|\Z)/gm,function(a,b,e,d,f){b=b.toLowerCase();h[b]=y(e);if(d)return d+f;f&&(i[b]=f.replace(/"/g,"&quot;"));return""})},w=function(a){a=a.replace(/\n/g,"\n\n");a=a.replace(/^(<(p|div|h[1-6]|blockquote|pre|table|dl|ol|ul|script|noscript|form|fieldset|iframe|math|ins|del)\b[^\r]*?\n<\/\2>[ \t]*(?=\n+))/gm,m);a=a.replace(/^(<(p|div|h[1-6]|blockquote|pre|table|dl|ol|ul|script|noscript|form|fieldset|iframe|math)\b[^\r]*?.*<\/\2>[ \t]*(?=\n+)\n)/gm,
	m);a=a.replace(/(\n[ ]{0,3}(<(hr)\b([^<>])*?\/?>)[ \t]*(?=\n{2,}))/g,m);a=a.replace(/(\n\n[ ]{0,3}<!(--[^\r]*?--\s*)+>[ \t]*(?=\n{2,}))/g,m);a=a.replace(/(?:\n\n)([ ]{0,3}(?:<([?%])[^\r]*?\2>)[ \t]*(?=\n{2,}))/g,m);return a=a.replace(/\n\n/g,"\n")},m=function(a,c){var b;b=c.replace(/\n\n/g,"\n");b=b.replace(/^\n/,"");b=b.replace(/\n+$/g,"");return b="\n\n~K"+(o.push(b)-1)+"K\n\n"},q=function(a){for(var a=D(a),c=k("<hr />"),a=a.replace(/^[ ]{0,2}([ ]?\*[ ]?){3,}[ \t]*$/gm,c),a=a.replace(/^[ ]{0,2}([ ]?\-[ ]?){3,}[ \t]*$/gm,
	c),a=a.replace(/^[ ]{0,2}([ ]?\_[ ]?){3,}[ \t]*$/gm,c),a=z(a),a=E(a),a=F(a),a=G(a),a=w(a),a=a.replace(/^\n+/g,""),a=a.replace(/\n+$/g,""),b=a.split(/\n{2,}/g),a=[],c=b.length,e=0;e<c;e++){var d=b[e];0<=d.search(/~K(\d+)K/g)?a.push(d):0<=d.search(/\S/)&&(d=n(d),d=d.replace(/\n/g,"<br />"),d=d.replace(/^([ \t]*)/g,"<p>"),d+="</p>",a.push(d))}c=a.length;for(e=0;e<c;e++)for(;0<=a[e].search(/~K(\d+)K/);)b=o[RegExp.$1],b=b.replace(/\$/g,"$$$$"),a[e]=a[e].replace(/~K\d+K/,b);return a=a.join("\n\n")},n=function(a){a=
	H(a);a=I(a);a=a.replace(/\\(\\)/g,r);a=a.replace(/\\([`*_{}\[\]()>#+-.!])/g,r);a=a.replace(/(!\[(.*?)\][ ]?(?:\n[ ]*)?\[(.*?)\])()()()()/g,A);a=a.replace(/(!\[(.*?)\]\s?\([ \t]*()<?(\S+?)>?[ \t]*((['"])(.*?)\6[ \t]*)?\))/g,A);a=a.replace(/(\[((?:\[[^\]]*\]|[^\[\]])*)\][ ]?(?:\n[ ]*)?\[(.*?)\])()()()()/g,s);a=a.replace(/(\[((?:\[[^\]]*\]|[^\[\]])*)\]\([ \t]*()<?(.*?)>?[ \t]*((['"])(.*?)\6[ \t]*)?\))/g,s);a=a.replace(/(\[([^\[\]]+)\])()()()()()/g,s);a=J(a);a=y(a);a=a.replace(/(\*\*|__)(?=\S)([^\r]*?\S[*_]*)\1/g,
	"<strong>$2</strong>");a=a.replace(/(\w)_(\w)/g,"$1~E95E$2");a=a.replace(/(\*|_)(?=\S)([^\r]*?\S)\1/g,"<em>$2</em>");return a=a.replace(/ +\n/g," <br />\n")},I=function(a){return a=a.replace(/(<[a-z\/!$]("[^"]*"|'[^']*'|[^'">])*>|<!(--.*?--\s*)+>)/gi,function(a){a=a.replace(/(.)<\/?code>(?=.)/g,"$1`");return a=l(a,"\\`*_")})},s=function(a,c,b,e,d,f,j,g){void 0==g&&(g="");a=e.toLowerCase();if(""==d)if(""==a&&(a=b.toLowerCase().replace(/ ?\n/g," ")),void 0!=h[a])d=h[a],void 0!=i[a]&&(g=i[a]);else if(-1<
	c.search(/\(\s*\)$/m))d="";else return c;d=l(d,"*_");c="<a target='blank' href=\""+d+'"';""!=g&&(g=g.replace(/"/g,"&quot;"),g=l(g,"*_"),c+=' title="'+g+'"');return c+(">"+b+"</a>")},A=function(a,c,b,e,d,f,j,g){a=b;e=e.toLowerCase();g||(g="");if(""==d)if(""==e&&(e=a.toLowerCase().replace(/ ?\n/g," ")),void 0!=h[e])d=h[e],void 0!=i[e]&&(g=i[e]);else return c;a=a.replace(/"/g,"&quot;");d=l(d,"*_");c='<img src="'+d+'" alt="'+a+'"';g=g.replace(/"/g,"&quot;");g=l(g,"*_");return c+(' title="'+g+'"')+" />"},
	D=function(a){a=a.replace(/^(.+)[ \t]*\n=+[ \t]*\n+/gm,function(a,b){return k("<h1>"+n(b)+"</h1>")});a=a.replace(/^(.+)[ \t]*\n-+[ \t]*\n+/gm,function(a,b){return k("<h2>"+n(b)+"</h2>")});return a=a.replace(/^(\#{1,6})[ \t]*(.+?)[ \t]*\#*\n+/gm,function(a,b,e){a=b.length;return k("<h"+a+">"+n(e)+"</h"+a+">")})},t,z=function(a){var a=a+"~0",c=/^(([ ]{0,3}([*+-]|\d+[.])[ \t]+)[^\r]+?(~0|\n{2,}(?=\S)(?![ \t]*(?:[*+-]|\d+[.])[ \t]+)))/gm;p?a=a.replace(c,function(a,c,d){a=c;d=-1<d.search(/[*+-]/g)?"ul":
	"ol";a=a.replace(/\n{2,}/g,"\n\n\n");a=t(a);a=a.replace(/\s+$/,"");return"<"+d+">"+a+"</"+d+">\n"}):(c=/(\n\n|^\n?)(([ ]{0,3}([*+-]|\d+[.])[ \t]+)[^\r]+?(~0|\n{2,}(?=\S)(?![ \t]*(?:[*+-]|\d+[.])[ \t]+)))/g,a=a.replace(c,function(a,c,d,f){a=d;f=-1<f.search(/[*+-]/g)?"ul":"ol";a=a.replace(/\n{2,}/g,"\n\n\n");a=t(a);return c+"<"+f+">\n"+a+"</"+f+">\n"}));return a=a.replace(/~0/,"")};t=function(a){p++;a=a.replace(/\n{2,}$/,"\n");a=(a+"~0").replace(/(\n)?(^[ \t]*)([*+-]|\d+[.])[ \t]+([^\r]+?(\n{1,2}))(?=\n*(~0|\2([*+-]|\d+[.])[ \t]+))/gm,
	function(a,b,e,d,f){a=f;b||-1<a.search(/\n{2,}/)?a=q(u(a)):(a=z(u(a)),a=a.replace(/\n$/,""),a=n(a));return"<li>"+a+"</li>\n"});a=a.replace(/~0/g,"");p--;return a};var F=function(a){a=(a+"~0").replace(/(?:\n\n|^)((?:(?:[ ]{4}|\t).*\n+)+)(\n*[ ]{0,3}[^ \t\n]|(?=~0))/g,function(a,b,e){a=B(u(b));a=v(a);a=a.replace(/^\n+/g,"");a=a.replace(/\n+$/g,"");return k("<pre><code>"+a+"\n</code></pre>")+e});return a=a.replace(/~0/,"")},E=function(a){return a=a.replace(/`{3}(?:(.*$)\n)?([\s\S]*?)`{3}/gm,function(a,
	b,e){return'<div class="highlight"><pre lang="'+b+'">'+e+"</pre></div>"})},k=function(a){a=a.replace(/(^\n+|\n+$)/g,"");return"\n\n~K"+(o.push(a)-1)+"K\n\n"},H=function(a){return a=a.replace(/(^|[^\\])(`+)([^\r]*?[^`])\2(?!`)/gm,function(a,b,e,d){a=d.replace(/^([ \t]*)/g,"");a=a.replace(/[ \t]*$/g,"");a=B(a);return b+"<code>"+a+"</code>"})},B=function(a){a=a.replace(/&/g,"&amp;");a=a.replace(/</g,"&lt;");a=a.replace(/>/g,"&gt;");return a=l(a,"*_{}[]\\",!1)},G=function(a){return a=a.replace(/((^[ \t]*>[ \t]?.+\n(.+\n)*\n*)+)/gm,
	function(a,b){var e;e=b.replace(/^[ \t]*>[ \t]?/gm,"~0");e=e.replace(/~0/g,"");e=e.replace(/^[ \t]+$/gm,"");e=q(e);e=e.replace(/(^|\n)/g,"$1 ");e=e.replace(/(\s*<pre>[^\r]+?<\/pre>)/gm,function(a,b){var c;c=b.replace(/^ /mg,"~0");return c=c.replace(/~0/g,"")});return k("<blockquote>\n"+e+"\n</blockquote>")})},y=function(a){a=a.replace(/&(?!#?[xX]?(?:[0-9a-fA-F]+|\w+);)/g,"&amp;");return a=a.replace(/<(?![a-z\/?\$!])/gi,"&lt;")},J=function(a){a=a.replace(/<((https?|ftp|dict):[^'">\s]+)>/gi,"<a target='blank' href=\"$1\">$1</a>");
	return a=a.replace(/<(?:mailto:)?([-.\w]+\@[-a-z0-9]+(\.[-a-z0-9]+)*\.[a-z]+)>/gi,function(a,b){return K(x(b))})},K=function(a){var c=[function(a){return"&#"+a.charCodeAt(0)+";"},function(a){a=a.charCodeAt(0);return"&#x"+("0123456789ABCDEF".charAt(a>>4)+"0123456789ABCDEF".charAt(a&15))+";"},function(a){return a}],a=("mailto:"+a).replace(/./g,function(a){if("@"==a)a=c[Math.floor(2*Math.random())](a);else if(":"!=a)var e=Math.random(),a=0.9<e?c[2](a):0.45<e?c[1](a):c[0](a);return a});return a=("<a target='blank' href=\""+
	a+'">'+a+"</a>").replace(/">.+:/g,'">')},x=function(a){return a=a.replace(/~E(\d+)E/g,function(a,b){var e=parseInt(b);return String.fromCharCode(e)})},u=function(a){a=a.replace(/^(\t|[ ]{1,4})/gm,"~0");return a=a.replace(/~0/g,"")},v=function(a){a=a.replace(/\t(?=\t)/g," ");a=a.replace(/\t/g,"~A~B");a=a.replace(/~B(.+?)~A/g,function(a,b){for(var e=b,d=4-e.length%4,f=0;f<d;f++)e+=" ";return e});a=a.replace(/~A/g," ");return a=a.replace(/~B/g,"")},l=function(a,c,b){c="(["+c.replace(/([\[\]\\])/g,
	"\\$1")+"])";b&&(c="\\\\"+c);return a=a.replace(RegExp(c,"g"),r)},r=function(a,c){return"~E"+c.charCodeAt(0)+"E"}};

	var parserMarkdown = new Showdown.converter(),
		$description = $('#description');
	$("#description-area").on('click', '.selectable', function(e) {
		switch ($(this).data('action')) {
			case 'preview':
				$('#description').hide();
				$description.text($description.val());
				console.log($description.val());
				$('#markdown-preview').html(parserMarkdown.makeHtml($description.text())).show();	
				break;
			case 'text':
				$('#markdown-preview').hide();
				$('#description').show();
				break;
			default:
				break;
		}
		$('#description-area').find('.selected').removeClass('selected');
		$(this).addClass('selected');
		e.preventDefault();
	});

	var $descriptionAbout = $('#about-intern').find(".description");
	if ($('#about-intern').hasClass('user')) {
		$descriptionAbout.html(parserMarkdown.makeHtml($descriptionAbout.text())).fadeIn();
	}

});