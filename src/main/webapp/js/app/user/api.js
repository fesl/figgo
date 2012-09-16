define(['jquery', 'lodash', 'plugins/events'], function() {
  'use strict';

  var Events = require('plugins/events');

  var API = {
    getUserDomains: function(options) {
      $.get('/user/domains', function(data) {
        Events.trigger('UserAPI:getUserDomains', data.domains);
      });
    },

    getUsersDetails: function(users, options) {
      options = _.extend({event: 'UserAPI:getUsersDetails'}, options);

      if (!users) {
        Events.trigger(options.event, {});
        return;
      }

      $.get('/users/search', {users: users}, function(data) {
        Events.trigger(options.event, data.result);
      });
    }
  };

  return API;
});