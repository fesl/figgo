define(['jquery', 'services/api', 'user/api', 'plugins/events', 'plugins/template'], function() {
  'use strict';

  var ServiceAPI = require('services/api'),
      UserAPI    = require('user/api'),
      Events     = require('plugins/events'),
      TemplateManager = require('plugins/template');

  var Service = function(options) {
    this.$el = options.el;
    this.$providerList = this.$el.find('#providers');
    this.service = this.$el.data('service');
    this.userId = this.$el.data('user');
    this.userName = this.$el.data('username');
  };

  Service.prototype.init = function() {
    this._getProviders();

    Events.on('ServiceAPI:updateProvider', this._updateProvidersList, this);
    Events.on('ServiceAPI:getProviders', this._getUsersDetails, this);
    Events.on('UserAPI:getUsersDetails', this.renderProvidersList, this);
  };

  Service.prototype.renderProvidersList = function(e, providers) {
    var that = this;
    TemplateManager.get('service/providers', {
      data: {
        providers: providers,
        service: this.service,
        me: this.userId
      },
      callback: function(template) {
        that.$providerList.find('.loader').remove();
        that.$providerList.append(template);
      }
    });
  };

  Service.prototype._getProviders = function() {
    ServiceAPI.getProviders(this.service);
  };

  Service.prototype._updateProvidersList = function(e, status) {
    if (status) {
      this._addToList();
    } else {
      this._removeFromList();
    }
  };

  Service.prototype._addToList = function() {
    this.$providerList.find('ul').append('<li data-user="'+this.userId+'"><span>'+this.userName+'</span></li>')
  };

  Service.prototype._removeFromList = function() {
    this.$providerList.find('li[data-user="'+this.userId+'"]').remove();
  };

  Service.prototype._getUsersDetails = function(e, providers) {
    UserAPI.getUsersDetails(providers.join(','));
  };

  return Service;
});