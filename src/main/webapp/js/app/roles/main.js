require(['jquery', 'app/roles/views/users', 'app/roles/views/rolesUsers', 'app/roles/views/roles'], function() {
  var Users      = require('app/roles/views/users'),
      RolesUsers = require('app/roles/views/rolesUsers'),
      Roles      = require('app/roles/views/roles'),
      $users     = $('#users-domain'),
      $rolesUsers = $('.roles-users'),
      $roles      = $('.roles');

  if ($users.length) new Users({el: $users}).init();
  if ($rolesUsers.length) new RolesUsers({el: $rolesUsers}).init();
  if ($roles.length) new Roles({el: $roles}).init();
});