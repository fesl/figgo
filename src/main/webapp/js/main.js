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
    'jquery': 'vendor/jquery',
    'lodash': 'vendor/lodash',
    'modernizr': 'vendor/modernizr',
    'domain': 'app/domain',
    'user': 'app/user',
    'services': 'app/services',
    'roles': 'app/roles',
    'bank': 'app/bank'
  }
});

define(['jquery'], function() {
  var module;
  // every page refers to its module and every module has its own javascript main code
  if (module = $(document.body).data('module'))  {
    require([module + '/main']);
  }

  // this must go somewhere else
  $(document).on('click', 'a[data-type=url]', function(e) {
    e.preventDefault();
    if (confirm(this.dataset['confirm'])) {
      var form = $('<form method="post" action="' + this.href + '"></form>');
      form.appendTo('body');
      form.submit();
    }
  });
});