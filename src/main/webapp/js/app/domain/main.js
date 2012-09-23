define(function(require) {
  var Search       = require('domain/views/search'),
      Preferences  = require('domain/views/preferences'),
      $search      = $('.domain-list'),
      $preferences = $('#domain-preferences');

  if ($search.length) new Search({el: $search}).init();
  if ($preferences.length) new Preferences({el: $preferences}).init();
});