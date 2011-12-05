HTMLElement.prototype.remove = function() {
    this.parentNode.removeChild(this);
}

String.prototype.normalize = function() {
    var url = this;
    var preserveNormalForm = /[,_`;\':-]+/gi
    url = url.replace(preserveNormalForm, ' ');

    // strip accents
    url = url.stripAccents();

    //replace spaces with a -
    url = url.replace(/\s+/gi, '-');
    return url;
}

String.prototype.trim = function () {
    return this.replace(/^\s+|\s+$/g, "");
}

String.prototype.stripAccents = function() {
    var str = this;
    var from = "ÃÀÁÄÂÈÉËÊÌÍÏÎÒÓÖÔÙÚÜÛãàáäâèéëêìíïîòóöôùúüûÑñÇç", 
        to   = "AAAAAEEEEIIIIOOOOUUUUaaaaaeeeeiiiioooouuuunncc",
        mapping = {},
        stringLength = from.length,
        i;
        
    for (i = 0; i < stringLength; i += 1) {
        mapping[from.charAt(i)] = to.charAt(i);
    }
    
    var ret = [],
        strLength = str.length,
        i;

    for (i = 0; i < strLength; i += 1) {
        var c = str.charAt(i)
        if (mapping.hasOwnProperty(str.charAt(i))) {
            ret.push(mapping[c]);
        } else {
            ret.push(c);
        }
    }

    return ret.join('').replace(/[^-A-Za-z0-9]+/g, '-').toLowerCase();
}

$(document).delegate('a[data-type="url"]', 'click', function(e) {
    e.preventDefault();
    if (confirm(this.dataset['confirm'])) {
        var form = $('<form method="post" action="' + this.href + '"></form>');
        form.appendTo('body');
        form.submit();
    }
});