require(['jquery', 'app/user/views/sidebar', 'app/user/views/search', 'app/user/views/preferences'], function() {
  var Sidebar      = require('app/user/views/sidebar'),
      Search       = require('app/user/views/search'),
      Preferences  = require('app/user/views/preferences'),
      $sidebar     = $('#user-sidebar'),
      $search      = $('.user-list'),
      $preferences = $('#user-preferences');

  if ($sidebar.length) new Sidebar({el: $sidebar}).init();
  if ($search.length) new Search({el: $search}).init();
  if ($preferences.length) new Preferences({el: $preferences}).init();
});