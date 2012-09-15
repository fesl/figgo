$(function() {
	var $searchCategoryInput = $(".autocomplete");
    if ($searchCategoryInput.length) {
        var cache = {},
            lastXhr;
        $searchCategoryInput.autocomplete({
            minLength: 1,
            source: function( request, response ) {
                        var term = request.term;
                        if ( term in cache ) {
                            response(  
                                    $.map( cache[term].result, function( item ) {
                                        return {
                                            label: item.name,
                                            value: item.name
                                        };
                                    })
                            );
                            return;
                        }
        
                        lastXhr = $.getJSON( "/services/category/search/" + term.normalize(), 
                            function( data, status, xhr ) {
                            cache[ term ] = data;
                            if ( xhr === lastXhr ) {
                                response( 
                                    $.map( data.result, function( item ) {
                                        return {
                                            label: item.name,
                                            value: item.name
                                        };
                                    })
                                );
                            }}
                        );
                    }
        });
    }

	$("#providers .thumbs-up").bind('click', function() {
		var $this = $(this),
			$providers = $("#providers > ul"),
			$service = $("#service"),
			$notice = $(".notice");
		
		if ($this.hasClass('not')) {
			$.ajax({
				type: "POST",
				url: "/service/" + $service.data('id') + "/provider/new",
			}).success(function(data) {
				$this.removeClass('not');
				$this.attr('title', 'Eu faço!');
				$this.attr('alt', 'Eu faço!');
				$providers.prepend('<li data-user="' + data.userId + '">' + $providers.data('username') + ' <a class="contract-link" href="/service/' + data.serviceId
					+ '/contract/' + data.userId + '">contratar</a></li>');
				$notice.hide();
				$providers.fadeIn();
			});
		} else {
			$.ajax({
				type: "POST",
				url: "/service/" + $service.data('id') + "/provider/delete",
			}).success(function(data) {
				$this.addClass('not');
				$this.attr('title', 'Não faço!');
				$this.attr('alt', 'Não faço!');
				$providers.find("li[data-user='" + $providers.data("userid") + "']")[0].remove();
			});
		}
	});

	$(".service").find(".thumbs-up").bind('click', function() {
		var $this 	 = $(this),
			$service = $this.closest('.service');
		
		if ($this.hasClass('not')) {
			$.ajax({
				type: "POST",
				url: "/service/" + $service.data('id') + "/provider/new",
			}).success(function(data) {
				$this.removeClass('not');
				$this.attr('title', 'Eu faço!');
				$this.attr('alt', 'Eu faço!');
			});
		} else {
			$.ajax({
				type: "POST",
				url: "/service/" + $service.data('id') + "/provider/delete",
			}).success(function(data) {
				$this.addClass('not');
				$this.attr('title', 'Não faço!');
				$this.attr('alt', 'Não faço!');
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

	// pills on /services/contracts
	var $pills = $("#services-type");
	$pills.delegate('a', 'click', function(e) {
		var $this = $(this),
			$currentLi = $pills.find(".active"),
			$nextLi = $this.parent();
			$nextDiv = $(this.dataset['div']);
		$currentLi.removeClass("active");
		$nextLi.addClass("active");
		$('.pill-container').hide();
		$nextDiv.show();
		e.preventDefault();
	});

});