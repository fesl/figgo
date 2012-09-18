define(['jquery', 'plugins/jquery-ui.custom.min', 'app/service/api', 'plugins/events'], function() {
  'use strict';

  var ServiceAPI = require('app/service/api'),
      Events     = require('plugins/events');

  var NewService = function(options) {
    this.$el = options.el;
    this.cache = {};
  };

  NewService.prototype.init = function() {
    this.setupAutocomplete();

    Events.on('ServiceAPI:searchCategory', this._parseResult, this);
  };

  NewService.prototype.setupAutocomplete = function(e, users) {
    var source = $.proxy(this._source, this);
    this.$el.find('.autocomplete').autocomplete({minLength: 1, source: source});
  };

  NewService.prototype._source = function(request, response) {
    var term = request.term;

    this.response = response;
    if ( term in this.cache ) {
      this._makeResponse(this.cache[term]);
      return;
    }
    ServiceAPI.searchCategory(term.normalize());
  };

  NewService.prototype._parseResult = function(e, term, data, xhr) {
    this.cache[term] = data.result;
    this._makeResponse(this.cache[term]);
  };

  NewService.prototype._makeResponse = function(collection) {
    collection = $.map( collection, function( item ) { return {label: item.name, value: item.name}; });
    this.response(collection);
  };

  return NewService;
});