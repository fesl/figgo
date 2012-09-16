define(['jquery', 'lodash', 'plugins/events'], function() {
  'use strict';
  var Events = require('plugins/events');

  var API = {
    getUsers: function(options) {
       options = _.extend({event: 'RolesAPI:getUsers'}, options);

      $.get('/users.json', function(data) {
        Events.trigger(options.event, (data[options.key] || data));
      });
    },

    getActiveUsers: function() {
      this.getUsers({event: 'RolesAPI:getActiveUsers', key: 'active'})
    },

    getPendingUsers: function() {
      this.getUsers({event: 'RolesAPI:getPendingUsers', key: 'pending'})
    },

    getRoles: function() {
      $.get('/roles.json', function(data) {
        Events.trigger('RolesAPI:getRoles', data);
      });
    },

    updateUserRole: function(role, user, status) {
      var action = status ? 'add' : 'remove';
      $.post('/roles/'+role+'/users/'+user+'/'+action);
    },

    updateRole: function(role, activity, status) {
      var action = status ? 'add' : 'remove';
      $.post('/roles/'+role+'/activity/'+activity+'/'+action);
    }
  };

  return API;
});