define(['jquery', 'services/api', 'plugins/events'], function() {
  'use strict';

  var ServiceAPI = require('services/api'),
      Events     = require('plugins/events');

  var ThumbsUp = function(options) {
    this.$el = options.el;
  };

  ThumbsUp.prototype.init = function() {
    this.attachEvents();

    Events.on('ServiceAPI:updateProvider', this._updateThumbsUp, this);
  };

  ThumbsUp.prototype.attachEvents = function(e, users) {
    var updateProvider = $.proxy(this.updateProvider, this);
    this.$el.on('click', updateProvider);
  };

  ThumbsUp.prototype.updateProvider = function(e) {
    var $target = $(e.target);
    ServiceAPI.updateProvider($target.data('service'), $target.hasClass('not'), $target);
  };

  ThumbsUp.prototype._updateThumbsUp = function(e, status, $target) {
    if (status) {
      this._activateThumbsUp($target);
    } else {
      this._deactivateThumbsUp($target);
    }
  };

  ThumbsUp.prototype._activateThumbsUp = function($target) {
    $target.removeClass('not');
    $target.attr('title', 'Eu faço!');
    $target.attr('alt', 'Eu faço!');
  };

  ThumbsUp.prototype._deactivateThumbsUp = function($target) {
    $target.addClass('not');
    $target.attr('title', 'Não faço!');
    $target.attr('alt', 'Não faço!');
  };

  return ThumbsUp;
});