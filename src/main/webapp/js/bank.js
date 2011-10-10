$(function() {
	$("#statement-form").submit(function(e) {
		var $this = $(this),
		$table = $("table");
		$table.show();
		$.ajax({
			type: "POST",
			url: "/bank/statement",
			data: {startDate: $this.find('input[name=startDate]').val(), endDate: $this.find('input[name=endDate]').val()}
		}).success(function(data) {
			var $tbody = $table.find("tbody"); 
			$tbody.empty();
			if (data.transactions.length > 0) {
				for (var i = 0, length = data.transactions.length; i < length; i+=1) {
					$tbody.append("<tr><td>" + data.transactions[i].date + "</td>\n" +
								  "<td>" + data.transactions[i].accountDest + "</td>\n" +
								  "<td>" + data.transactions[i].comment + "</td></tr>");
				}
			} else {
				$tbody.append("<tr><td colspan=\"4\">Nenhuma transação nesse período.</td></tr>");
			}
			// TODO set balance
		}).error(function(data) {
			console.log('Não foi possível buscar extrato');
			console.log(data);
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
			console.log('Não foi possível recuperar stats do banco');
		});
		e.preventDefault();
	});
	
});