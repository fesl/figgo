define(function(require) {
  var Users      = require('roles/views/users'),
      RolesUsers = require('roles/views/rolesUsers'),
      Roles      = require('roles/views/roles'),
      $users     = $('#users-domain'),
      $rolesUsers = $('.roles-users'),
      $roles      = $('.roles');

  if ($users.length) new Users({el: $users}).init();
  if ($rolesUsers.length) new RolesUsers({el: $rolesUsers}).init();
  if ($roles.length) new Roles({el: $roles}).init();
});