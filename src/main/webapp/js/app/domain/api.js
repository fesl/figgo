define(['jquery', 'plugins/events'], function() {
  'use strict';
  var Events = require('plugins/events');

  var API = {
    getDomainsInfo: function(data) {
      $.get('/domains', {domains: data}, function(data) {
        Events.trigger('domainapi:getDomainsInfo', data.result);
      });
    }
  };

  return API;
});