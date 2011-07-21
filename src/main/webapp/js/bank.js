$(function() {
	$("#statement-form").submit(function(e) {
		e.preventDefault();
		$.ajax({
			type: "POST",
			url: "/bank/statement",
		}).success(function(data) {
			var $tbody = $("#my-services > table > tbody"); 
			$tbody.empty();
			for (var i = 0, length = data.transactions.length; i < length; i++) {
				$tbody.append("<tr><td>" + data.transactions[i].date + "</td>\n" +
							  "<td>" + data.transactions[i].accountDest + "</td>\n" +
							  "<td>" + data.transactions[i].comment + "</td></tr>");
			}
			console.log(data)
		}).error(function(data) {
			alert('não foi possível buscar extrato')
		});
	});
	
});