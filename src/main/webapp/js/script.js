HTMLElement.prototype.remove = function() {
	this.parentNode.removeChild(this);
}

$(document).delegate('a[data-type="url"]', 'click', function(e) {
	e.preventDefault();
	if (confirm(this.dataset['confirm'])) {
		var form = $('<form method="post" action="' + this.href + '"></form>');
		form.appendTo('body');
		form.submit();
	}
});