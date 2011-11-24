$(function() {
	$("#providers .thumbs-up").bind('click', function() {
		var $this = $(this),
			$providers = $("#providers ul"),
			$service = $("#service");
		if ($this.hasClass('not')) {
			$.ajax({
				type: "POST",
				url: "/service/" + $service.data("id") + "/provider/new",
			}).success(function(data) {
				$this.removeClass('not');
				$providers.prepend(data.html);
			});
		} else {
			$.ajax({
				type: "POST",
				url: "/service/" + $service.data("id") + "/provider/delete",
			}).success(function(data) {
				$this.addClass('not');
				$providers.find("#" + $providers.data("user")).remove();
			});
		}
	});
	
	var $providers = $("#providers > ul");
	$providers.delegate('a', 'click', function(e) {
		$.ajax({
			type: "POST",
			url: $(this).attr('href'),
		}).success(function(data) {
			window.location.replace("/services/contracts");
		});
		e.preventDefault();
	});
	
	var users = $providers.find('li').map(function() { return this.dataset['user'] }).get();
	$.get('/users/search/', {'users': users.join(",")}, function(data) {
		var i, currentLi,
			usersLength = data.result.length,
			users = data.result;
		for (i = 0; i < usersLength; i += 1) {
			currentLi = $providers.find("li[data-user='"+users[i].userId+"']")[0]
			currentLi.childNodes[0].textContent = users[i].name;
		}
		document.getElementById("loader").remove();
		$providers.fadeIn();
	}).error(function(data) {
		console.log("não foi possível carregar dados dos usuários");
	});



});