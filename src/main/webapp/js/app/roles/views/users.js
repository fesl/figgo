define(['jquery', 'lodash', 'roles/api', 'user/api', 'plugins/template'], function() {
  'use strict';

  var Events = require('plugins/events'),
      RolesAPI = require('roles/api'),
      UserAPI  = require('user/api'),
      TemplateManager = require('plugins/template');

  var Users = function(options) {
    this.$el = options.el;
  };

  Users.prototype.init = function() {
    this._getUsers();

    Events.on('RolesAPI:getUsers', this._getUsersDetails, this);
    Events.on('UserAPI:getActiveUsersDetails', this.renderActiveUsers, this);
    Events.on('UserAPI:getPendingUsersDetails', this.renderPendingUsers, this);
  };

  Users.prototype.renderActiveUsers = function(e, users) {
    var $activeUsers = this.$el.find('#active-users');
    TemplateManager.get('roles/activeUsers', {
      data: {
        users: users
      },
      callback: function(template) {
        $activeUsers.find('.loader').remove();
        $activeUsers.append(template);
      }
    });
  };

  Users.prototype.renderPendingUsers = function(e, users) {
    var $pendingUsers = this.$el.find('#pending-users'),
        canAccept = $pendingUsers.data('accept'),
        canReject = $pendingUsers.data('reject');

    TemplateManager.get('roles/pendingUsers', {
      data: {
        users: users,
        canAccept: canAccept,
        canReject: canReject
      },
      callback: function(template) {
        $pendingUsers.find('.loader').remove();
        $pendingUsers.append(template);
      }
    });
  };

  Users.prototype._getUsers = function() {
    RolesAPI.getUsers();
  };

  Users.prototype._getUsersDetails = function(e, users) {
    var activeUsers = _.map(users.active, function(user) { return user.userId }),
        pendingUsers = _.map(users.pending, function(user) { return user.userId });

    UserAPI.getUsersDetails(activeUsers.join(','), {event: 'UserAPI:getActiveUsersDetails'});
    UserAPI.getUsersDetails(pendingUsers.join(','), {event: 'UserAPI:getPendingUsersDetails'});
  };

  return Users;
});