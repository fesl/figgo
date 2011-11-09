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
	
	$("a.inside-link").bind('click', function(e) {
		$.ajax({
			type: "POST",
			url: $(this).attr('href'),
		}).success(function(data) {
			window.location.replace("/services/contracts");
		});
		e.preventDefault();
	});

});