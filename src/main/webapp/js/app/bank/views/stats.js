define(['jquery', 'bank/api', 'user/api', 'plugins/events', 'plugins/template', 'plugins/accounting', 'plugins/jquery-ui.custom.min', 'util'], function() {
  'use strict';

  var BankAPI = require('bank/api'),
      UserAPI = require('user/api'),
      Util = require('util'),
      Events  = require('plugins/events'),
      Accounting = require('plugins/accounting'),
      TemplateManager = require('plugins/template');

  var Stats = function(options) {
    this.$el = options.el;
  };

  Stats.prototype.init = function() {
    this.attachEvents();
    this._setupDatepicker();

    Events.on('BankAPI:getStats', this.render, this);
  };

  Stats.prototype.attachEvents = function() {
    var getStats = $.proxy(this._getStats, this);
    this.$el.find('#stats-form').on('submit', getStats);
  };

  Stats.prototype.render = function(e, stats) {
    var startDate = this.$el.find('#startDate').val(),
        endDate = this.$el.find('#endDate').val(),
        that = this;

    TemplateManager.get('bank/stats', {
      data: {
        startDate: startDate,
        endDate: endDate,
        circulation: stats.creditAmount,
        amount: stats.amountTransactions,
        formatMoney: this._formatMoney
      },
      callback: function(template) {
        that.$el.find('.loader').remove();
        that.$el.append(template);
      }
    });
  };

  Stats.prototype._setupDatepicker = function() {
    var dates = $('#startDate, #endDate').datepicker({
      defaultDate: "+1w",
      numberOfMonths: 2,
      dateFormat: "dd/mm/yy",
      onSelect: function( selectedDate ) {
        var option = this.id == "startDate" ? "minDate" : "maxDate",
            instance = $( this ).data( "datepicker" ),
            date = $.datepicker.parseDate(instance.settings.dateFormat || $.datepicker._defaults.dateFormat,
                selectedDate, instance.settings );
        dates.not( this ).datepicker( "option", option, date );
      }
    });
  };

  Stats.prototype._getStats = function(e) {
    var startDate = this.$el.find('#startDate').val(),
        endDate = this.$el.find('#endDate').val();

    e.preventDefault();
    BankAPI.getStats(startDate, endDate);
    this.$el.find('#dynamic-stats').remove();
    this.$el.append('<img class="loader" src="/img/ajax-loader.gif" />');
  };

  // extract this to i10n
  Stats.prototype._formatMoney = function(amount) {
    return Accounting.formatMoney(amount, 'MR$', 2, ".", ",");
  };

  return Stats;
});