$(function() {
	var userId = $("form").data('user');
	
	$("form").delegate('input:checkbox', 'click', function() {
		var $this = $(this);
		if ($this.is(":checked")) {
			console.log($("form").data('user'));
			$.ajax({
				type: "POST",
				url: "/roles/user/add",
				dataType: "json",
				data: {role: $this.attr('id'), user: userId}
			}).success(function(data) {
				console.log("adicionou!")
			}).error(function(data) {
				alert('não foi possível adicionar role do usuário')
			});
		} else {
			$.ajax({
				type: "POST",
				url: "/roles/user/del",
				dataType: "json",
				data: {role: $this.attr('id'), user: userId}
			}).success(function(data) {
				console.log("removeu!");
			}).error(function(data) {
				alert('não foi possível remover role do usuário')
			});
		}
	});
	
	$("a.delete").click(function(e) {
		var $this = $(this);
        if (confirm($this.data('confirm'))) {
	        var form = $('<form method="post" action="' + $this.attr('href') + '"></form>');
	        form.appendTo('body');
	        form.submit();
        }
        e.preventDefault(); 
    });
});