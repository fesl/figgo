$(function() {
	
	var searchUserInput = $(".autocomplete");
	if (searchUserInput.length) {
		var cache = {},
		lastXhr
		searhUserInput.autocomplete({
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
					}); // source
			}
		});
	}
	
});