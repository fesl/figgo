define(['jquery', 'plugins/events'], function() {
  'use strict';
  var Events = require('plugins/events');

  var API = {
    getMyAccount: function() {
      $.get('/bank/myaccount.json', function(data) {
        Events.trigger('BankAPI:getMyAccount', data);
      });
    },

    getStats: function(startDate, endDate) {
      // TODO implement on the controller
      $.get('/bank/stats.json', {startDate: startDate, endDate: endDate}, function(data) {
        Events.trigger('BankAPI:getStats', data);
      });
    }
  };

  return API;
});