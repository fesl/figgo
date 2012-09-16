require.config({
  baseUrl: '../js',

  shim: {
    'jquery': {
      exports: '$'
    },
    'lodash': {
      exports: '_'
    },
    'modernizr': {
      exports: 'Modernizr'
    },
  },

  paths: {
    jquery: 'jquery',
    lodash: 'lodash',
    modernizr: 'modernizr',
  }
});

define(['jquery'], function() {
  $(document).on('click', 'a[data-type=url]', function(e) {
    e.preventDefault();
    if (confirm(this.dataset['confirm'])) {
      var form = $('<form method="post" action="' + this.href + '"></form>');
      form.appendTo('body');
      form.submit();
    }
  });
});