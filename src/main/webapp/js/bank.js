$(function() {
	$("#statement-form").submit(function(e) {
		var $this = $(this);
		$.ajax({
			type: "POST",
			url: "/bank/statement",
			data: {startDate: $this.find('input[name=startDate]').val(), endDate: $this.find('input[name=endDate]').val()}
		}).success(function(data) {
			var $tbody = $("#my-services > table > tbody"); 
			$tbody.empty();
			for (var i = 0, length = data.transactions.length; i < length; i++) {
				$tbody.append("<tr><td>" + data.transactions[i].date + "</td>\n" +
							  "<td>" + data.transactions[i].accountDest + "</td>\n" +
							  "<td>" + data.transactions[i].comment + "</td></tr>");
			}
			console.log(data);
		}).error(function(data) {
			alert('não foi possível buscar extrato')
		});
		e.preventDefault();
	});
	
	
	$("#stats-form").submit(function(e) {
		var $this = $(this);
		var $startDate = $this.find('input[name=startDate]');
		var $endDate = $this.find('input[name=endDate]');
		$.ajax({
			type: "POST",
			url: "/bank/stats",
			data: {startDate: $startDate.val(), endDate: $endDate.val()}
		}).success(function(data) {
			$("#dynamic-stats h3").html("Informações no intervalo de" + $endDate.val() + " a " + $startDate.val());
			$("dl > dd:first").html(data.circulation);
			$("dl > dd:eq(2)").html(data.creditAmount);
		}).error(function(data) {
			alert('não foi possível recuperar stats do banco')
		});
		e.preventDefault();
	});
	
});