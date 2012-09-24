define(function(require) {
  var SearchUser = require('bank/views/searchUser'),
      MyAccount = require('bank/views/myAccount'),
      Stats = require('bank/views/stats'),
      $myAccount = $('.my-account'),
      $searchUser = $('.autocomplete'),
      $stats = $('.stats');

  if ($searchUser.length) new SearchUser({el: $searchUser}).init();
  if ($myAccount.length) new MyAccount({el: $myAccount}).init();
  if ($stats.length) new Stats({el: $stats}).init();
});