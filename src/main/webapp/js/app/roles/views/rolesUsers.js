define(['jquery', 'lodash', 'roles/api', 'user/api', 'plugins/template'], function() {
  'use strict';

  var Events = require('plugins/events'),
      RolesAPI = require('roles/api'),
      UserAPI  = require('user/api'),
      TemplateManager = require('plugins/template');

  var RolesUsers = function(options) {
    this.$el = options.el;
  };

  RolesUsers.prototype.init = function() {
    this.attachEvents();
    this._getUsers();
    this._getRoles();

    Events.on('RolesAPI:getActiveUsers', this._getUsersDetails, this);
    Events.on('RolesAPI:getRoles', this.setRoles, this);
    Events.on('UserAPI:getActiveUsersDetails', this.setUsers, this);
  };

  RolesUsers.prototype.attachEvents = function(e, users) {
    var updateUserRole = $.proxy(this.updateUserRole, this);
    this.$el.on('change', 'input[type=checkbox]', updateUserRole);
  };

  RolesUsers.prototype.setRoles = function(e, result) {
    this.roles = result.roles;
    this.rolesReady = true;
    if (this.usersReady) this.render();
  };

  RolesUsers.prototype.setUsers = function(e, users) {
    this.users = users;
    this.usersReady = true;
    if (this.rolesReady) this.render();
  };

  RolesUsers.prototype.updateUserRole = function(e) {
    var $checkbox = $(e.target);
    RolesAPI.updateUserRole($checkbox.data('role'), $checkbox.data('user'), e.target.checked);
  };

  // ATTENTION: Temporary! We must have a i18n on the client-side
  RolesUsers.prototype.i18n = function(role) {
    var map = {
        'ADMINS': 'Administrador',
        'USERS': 'Usuário'
      }

    if (map[role]) {
      return map[role];
    }
    return role;
  }

  RolesUsers.prototype.render = function() {
    if (!this.rendered) {
      var that = this;
      TemplateManager.get('roles/rolesUsers', {
        data: {
          users: this.users,
          roles: this.roles,
          hasRole: this._hasRole,
          i18n: this.i18n
        },
        callback: function(template) {
          that.$el.find('.loader').remove();
          that.$el.append(template);
        }
      });
      this.rendered = true;
    }
  };

  RolesUsers.prototype._hasRole = function(role, user) {
    if (_.include(role.users, user.userId)) {
      return 'checked';
    }
  };

  RolesUsers.prototype._getUsers = function() {
    RolesAPI.getActiveUsers();
  };

  RolesUsers.prototype._getRoles = function() {
    RolesAPI.getRoles();
  };

  RolesUsers.prototype._getUsersDetails = function(e, users) {
    users = _.map(users, function(user) { return user.userId });
    UserAPI.getUsersDetails(users.join(','), {event: 'UserAPI:getActiveUsersDetails'});
  };

  return RolesUsers;
});