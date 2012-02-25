$(function() {

	var $domainsLi = $('li.organization'),
		domains = $domainsLi.map(function() { return this.dataset['domain'] }).get();
	if (domains.length) {
		$.get('/domains', {'domains': domains.join(",")}, function(data) {
			var i, currentLi,
				domainsLength = data.result.length,
				domains = data.result;
			for (i = 0; i < domainsLength; i += 1) {
				currentLi = $domainsLi.filter("li[data-domain='"+domains[i].domainName+"']")[0]
				currentLi.childNodes[1].src = "/serve/" + domains[i].avatarKey || "http://lorempixum.com/32/32/";
				currentLi.childNodes[3].textContent = domains[i].name;
			}
			document.getElementById("loader").remove();
			$domainsLi.fadeIn();
		}).error(function(data) {
			console.log("não foi possível carregar dados dos usuários");
		});
	}

});