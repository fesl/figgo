$(function() {
	$("#providers .thumbs-up").bind('click', function() {
		var $this = $(this),
			$providers = $("#providers > ul"),
			$service = $("#service");
		if ($this.hasClass('not')) {
			$.ajax({
				type: "POST",
				url: "/service/" + $service[0].dataset['id'] + "/provider/new",
			}).success(function(data) {
				$this.removeClass('not');
				$providers.prepend('<li data-user="' + data.userId + '"><a class="contract-link" href="/service/' + data.serviceId
					+ '"/contract/"' + data.userId + '">contratar</a></li>');
			});
		} else {
			$.ajax({
				type: "POST",
				url: "/service/" + $service[0].dataset['id'] + "/provider/delete",
			}).success(function(data) {
				$this.addClass('not');
				$providers.find("li[data-user='" + $providers.data("user") + "']").remove();
			});
		}
	});
	
	$("#providers > ul").delegate('a', 'click', function(e) {
		var $providers = $("#providers > ul");
		$.ajax({
			type: "POST",
			url: $(this).attr('href'),
		}).success(function(data) {
			window.location.replace("/services/contracts");
		});
		e.preventDefault();
	});
	
	var users = $("#providers > ul").find('li').map(function() { return this.dataset['user'] }).get();
	if (users.length) {
		var $providers = $("#providers > ul");
		$.get('/users/search/', {'users': users.join(",")}, function(data) {
			var i, currentLi,
				usersLength = data.result.length,
				users = data.result;
			for (i = 0; i < usersLength; i += 1) {
				currentLi = $providers.find("li[data-user='"+users[i].userId+"']")[0]
				currentLi.childNodes[0].textContent = users[i].name;
			}
			$(".loader").remove();
			$providers.fadeIn();
		}).error(function(data) {
			console.log("não foi possível carregar dados dos usuários");
		});
	}

	users = $("dd.provider").map(function() { return this.textContent }).get();
	if (users.length) {
		var $providers = $("dd.provider");
		$.get('/users/search/', {'users': users.join(",")}, function(data) {
			var i, $currentDd,
				usersLength = data.result.length,
				users = data.result;
			for (i = 0; i < usersLength; i += 1) {
				$currentDd = $providers.filter("dd[data-user='"+users[i].userId+"']")
				$currentDd.text(users[i].name);
			}
			$(".loader").remove();
			$providers.closest('ul').fadeIn();
		}).error(function(data) {
			console.log("não foi possível carregar dados dos usuários");
		});
	}

});