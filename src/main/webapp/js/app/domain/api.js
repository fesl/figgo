define(['jquery', 'plugins/events'], function() {
  'use strict';
  var Events = require('plugins/events');

  var API = {
    getDomainsInfo: function(data) {
      $.get('/domains', {domains: data}, function(data) {
        Events.trigger('DomainAPI:getDomainsInfo', data.result);
      });
    },

    updateModule: function(module, status) {
      var action = status ? 'enable' : 'disable';
      $.post('/domain/module/'+module+'/'+action, function(data) {
        Events.trigger('DomainAPI:updateModule');
      });
    }
  };

  return API;
});