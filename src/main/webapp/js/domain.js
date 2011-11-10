$(function() {
	$('input[type=checkbox]').change(function(e) {
		var $this = $(this);
		if ($this.is(":checked")) {
			$.ajax({
				type: "POST",
				url: $this.data("url") + "enable",
			}).error(function(data) {
				$this[0].checked = false;
			});
		} else {
			$.ajax({
				type: "POST",
				url: $this.data("url") + "disable",
			}).error(function(data) {
				$this[0].checked = true;
			});
		}
	});
});