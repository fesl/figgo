define(['jquery', 'lodash', 'plugins/events'], function() {
  'use strict';
  var Events = require('plugins/events');

  var API = {
    updateProvider: function(service, status, $target) {
      var action = status ? 'new' : 'delete';
      $.post('/service/'+service+'/provider/'+action, function(data) {
        Events.trigger('ServiceAPI:updateProvider', status, $target);
      });
    },

    getProviders: function(service) {
      $.get('/service/'+service+'/providers', function(data) {
        Events.trigger('ServiceAPI:getProviders', data.providers);
      });
    }
  };

  return API;
});