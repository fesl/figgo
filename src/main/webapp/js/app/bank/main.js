define(function(require) {
  var SearchUser = require('bank/views/searchUser'),
      MyAccount = require('bank/views/myAccount'),
      Stats = require('bank/views/stats'),
      Statement = require('bank/views/statement'),
      $myAccount = $('.my-account'),
      $searchUser = $('.autocomplete'),
      $stats = $('.stats'),
      $statement = $('.statement');

  if ($searchUser.length) new SearchUser({el: $searchUser}).init();
  if ($myAccount.length) new MyAccount({el: $myAccount}).init();
  if ($stats.length) new Stats({el: $stats}).init();
  if ($statement.length) new Statement({el: $statement}).init();
});