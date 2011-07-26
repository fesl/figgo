$(function() {
	var userId = $("form").data('user');
	
	$("form").delegate('input:checkbox', 'click', function() {
		var $this = $(this);
		if ($this.is(":checked")) {
			console.log($("form").data('user'));
			$.ajax({
				type: "POST",
				url: "/domain/user/role/add",
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
				url: "/domain/user/role/del",
				dataType: "json",
				data: {role: $this.attr('id'), user: userId}
			}).success(function(data) {
				console.log("removeu!");
			}).error(function(data) {
				alert('não foi possível remover role do usuário')
			});
		}
	});
	
	$("a.remove-roles").click(function(e) {
		if (confirm("Você tem certeza que deseja remover esse usuário do domínio?")) {
			$.ajax({
				type: "POST",
				url: $(this).attr('href'),
				dataType: "json",
				data: {user: userId}
			}).success(function(data) {
				window.location.replace("/domain/users");
			}).error(function(data) {
				alert('não foi possível remover role do usuário')
			});
		}
		e.preventDefault();
	});
});