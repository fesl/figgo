$(function() {
	
	$(".dropdown > a").click(function(e) {
		var $this = $(this);
		$(".dropdown ul").toggle();

		if ($this.css("display") == "none") {
			$this.removeClass("selected");
		} else {
			$this.addClass("selected");
		}
		
		e.preventDefault();
	});

	$(".dropdown > ul li a").click(function() {
	    $(".dropdown > ul").hide();
	});
	
	$(document).bind('click', function(e) {
	    // Lets hide the menu when the page is clicked anywhere but the menu.
	    var $clicked = $(e.target);
	    if (!$clicked.parents().hasClass("dropdown")) {
	        $(".dropdown ul").hide();
			$(".dropdown a").removeClass("selected");
		}
	});

});