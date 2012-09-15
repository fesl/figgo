define(['jquery', 'plugins/jquery.uitablefilter'], function() {
  'use strict';

  var Search = function(options) {
    this.$el = options.el;
  };

  Search.prototype.init = function() {
    this.setupTableFilter();
  };

  Search.prototype.setupTableFilter = function() {
    var $usersTable = this.$el.find('.domains-table');
    this.$el.find('#domain-search').keyup(function(e) {
      $.uiTableFilter( $usersTable, this.value, "Nome" );
    });
  };

  return Search;
});