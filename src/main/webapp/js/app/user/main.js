define(function(require) {
  var Sidebar      = require('user/views/sidebar'),
      Search       = require('user/views/search'),
      Preferences  = require('user/views/preferences'),
      $sidebar     = $('#user-sidebar'),
      $search      = $('.user-list'),
      $preferences = $('#user-preferences');

  if ($sidebar.length) new Sidebar({el: $sidebar}).init();
  if ($search.length) new Search({el: $search}).init();
  if ($preferences.length) new Preferences({el: $preferences}).init();
});