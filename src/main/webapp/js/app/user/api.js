define(['jquery', 'plugins/events'], function() {
  'use strict';

  var Events = require('plugins/events');

  var API = {
    getUserDomains: function(options) {
      $.get('/user/domains', function(data) {
        Events.trigger('userapi:getUserDomains', data.domains);
      });
    }
  };

  return API;
});