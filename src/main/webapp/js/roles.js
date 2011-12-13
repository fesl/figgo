$(function() {
	var userId = $("form").data('user');
	
	$("form.abc").delegate('input:checkbox', 'click', function() {
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
	
	// used on /roles
	$("a.delete").click(function(e) {
		var $this = $(this);
		if (confirm($this.data('confirm'))) {
			var form = $('<form method="post" action="' + $this.attr('href') + '"></form>');
			form.appendTo('body');
			form.submit();
		}
		e.preventDefault(); 
	});

	// used on /roles
	$("#add-role").click(function(e) {
		$("#new-role").show()
		$(this).hide();
		e.preventDefault(); 
	});

	// used on /roles
	$("#roles").find("input[type=checkbox]").change(function(e) {
		var $this = $(this);
		if (this.checked) {
			$.post("/roles/" + this.dataset["role"] + "/activity/" + this.dataset['activity'] + "/add")
			.error(function(data) {
				$this[0].checked = false;
				alert('Não foi possível adicionar atividade ao papel')
			});
		} else {
			$.post("/roles/" + this.dataset["role"] + "/activity/" + this.dataset["activity"] + "/remove")
			.error(function(data) {
				this.checked = true;
				console.log('Não foi possível remover atividade ao papel')
			});
		}
	});

	// used on /roles
	// used on /users
	// used on /roles/users
	var users = $("table").find('tr').map(function() { return this.dataset['user'] }).get();
	if (users.length) {
		var $providers = $("table");
		$.get('/users/search/', {'users': users.join(",")}, function(data) {
			var i, currentLi,
				usersLength = data.result.length,
				users = data.result;
			for (i = 0; i < usersLength; i += 1) {
				currentLi = $providers.find("tr[data-user='"+users[i].userId+"']")[0]
				currentLi.childNodes[1].textContent = users[i].name;
			}
			$(".loader").remove();
			$providers.fadeIn();
		}).error(function(data) {
			console.log("não foi possível carregar dados dos usuários");
		});
	}

	$("#roles-users").find("input[type=checkbox]").change(function(e) {
		var $this = $(this);
		if (this.checked) {
			$.post("/roles/" + this.dataset["role"] + "/users/" + this.dataset["user"] + "/add")
			.error(function(data) {
				$this[0].checked = false;
				alert('Não foi possível adicionar role ao usuário');
			});
		} else {
			$.post("/roles/" + this.dataset["role"] + "/users/" + this.dataset["user"] + "/remove")
			.error(function(data) {
				$this[0].checked = true;
				console.log('Não foi possível remover role do usuário')
			});
		}
	});

});