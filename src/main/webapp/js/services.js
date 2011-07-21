$(function() {
	$("tr.service").delegate('input:checkbox', 'click', function() {
		if ($(this).is(":checked")) {
			$.ajax({
				type: "POST",
				url: "/service/" + $(this).attr('id') +"/provider/new",
			}).success(function(data) {
				$("#my-services > table > tbody").append("<tr><td>" + data.service.name + "</td></tr>");
			}).error(function(data) {
				alert('não foi possível adicioná-lo como provedor do serviço')
			});
		} else {
			$.ajax({
				type: "POST",
				url: "/service/" + $(this).attr('id') + "/provider/delete",
			}).success(function(data) {
				// get <tr data-id="test"> and remove it!
				//$("#my-services > table > tbody").remove();
			}).error(function(data) {
				alert('não foi possível removê-lo como provedor do serviço')
			});
		}
	});
	
	$("a.contract-link").click(function(e) {
		e.preventDefault();
		$.ajax({
			type: "POST",
			url: $(this).attr('href'),
		}).success(function(data) {
			window.location.replace("/services/contracts");
		}).error(function(data) {
			alert('não foi possível contratar o serviço')
		});
	});
});