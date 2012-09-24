define(['jquery', 'plugins/events'], function() {
  'use strict';
  var Events = require('plugins/events');

  var API = {
    getMyAccount: function() {
      $.get('/bank/myaccount.json', function(data) {
        Events.trigger('BankAPI:getMyAccount', data);
      });
    }
  };

  return API;
});