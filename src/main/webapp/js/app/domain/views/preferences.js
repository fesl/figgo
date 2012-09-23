define(['plugins/showdown', 'domain/api'], function() {
  'use strict';

  var Showdown = require('plugins/showdown'),
      DomainAPI = require('domain/api');

  var Preferences = function(options) {
    this.$el = options.el;
  };

  Preferences.prototype.init = function() {
    this.cacheElements();
    this.attachEvents();
  };

  Preferences.prototype.attachEvents = function() {
    var switchToTextarea = $.proxy(this.switchToTextarea, this),
        switchToMarkdown = $.proxy(this.switchToMarkdown, this),
        updateModule     = $.proxy(this.updateModule, this);

    this.$el.find('#viewText').on('click', switchToTextarea);
    this.$el.find('#viewPreview').on('click', switchToMarkdown);
    this.$el.find('input[type=checkbox]').on('change', updateModule);
  };


  Preferences.prototype.cacheElements = function() {
    this.$description = this.$el.find('#description');
    this.$preview = this.$el.find('#markdown-preview');
  };

  Preferences.prototype.switchToMarkdown = function(e) {
    var convertedDescription = Showdown.makeHtml(this.$description.val());

    e.preventDefault();
    this._changeSelection(e.target);
    this.$description.hide();
    this.$preview.html(convertedDescription).show();
  };

  Preferences.prototype.switchToTextarea = function(e) {
    e.preventDefault();
    this._changeSelection(e.target);
    this.$preview.hide();
    this.$description.show();
  };

  Preferences.prototype.updateModule = function(e) {
    DomainAPI.updateModule(e.target.name.toLowerCase(), e.target.checked);
  };

  Preferences.prototype._changeSelection = function(target) {
    this.$el.find('.selected').removeClass('selected');
    $(target).addClass('selected');
  };

  return Preferences;
});