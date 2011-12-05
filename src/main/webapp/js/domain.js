$(function() {
	// used on /domain/edit
	$('input[type=checkbox]').change(function(e) {
		var $this = $(this),
			self = this;
		if ($this.is(":checked")) {
			$.ajax({
				type: "POST",
				url: self.dataset["url"] + "enable",
			}).error(function(data) {
				$this[0].checked = false;
			});
		} else {
			$.ajax({
				type: "POST",
				url: self.dataset["url"] + "disable",
			}).error(function(data) {
				$this[0].checked = true;
			});
		}
	});

	// used on /coletivos
	var $domainsTable = $("#domains");
	$("#domain-search").keyup(function(e) {
    	$.uiTableFilter( $domainsTable, this.value, "Nome" );
	});
});