require(['jquery', 'app/domain/views/search', 'app/domain/views/preferences'], function() {
  var Search       = require('app/domain/views/search'),
      Preferences  = require('app/domain/views/preferences'),
      $search      = $('.domain-list'),
      $preferences = $('#domain-preferences');

  if ($search.length) new Search({el: $search}).init();
  if ($preferences.length) new Preferences({el: $preferences}).init();
});