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
	
	var theTable = $('#domains > table');
	$('#domain-search').keyup(function() {
		$.uiTableFilter( theTable, this.value, "Nome" );
	});
	
	var cache = {},
	lastXhr;
	$(".autocomplete").autocomplete({
		minLength: 2,
		source: function( request, response ) {
			var term = request.term;
			if ( term in cache ) {
				response( cache[ term ] );
				return;
			}
	
			lastXhr = $.getJSON( "/user/search/" + term, function( data, status, xhr ) {
				cache[ term ] = data;
				if ( xhr === lastXhr ) {
					response( $.map( data, function( item ) {
						return {
							label: item.name + " <" + item.userId + ">",
							value: item.userId
						}
					}));
				}
			});
		}
	});
	
});