define(['jquery', 'services/api', 'user/api', 'util', 'plugins/events', 'plugins/accounting', 'plugins/template'], function() {
  'use strict';

  var ServiceAPI = require('services/api'),
      UserAPI    = require('user/api'),
      Util       = require('util'),
      Events     = require('plugins/events'),
      Accounting = require('plugins/accounting'),
      TemplateManager = require('plugins/template');

  var Contracts = function(options) {
    this.$el = options.el;
  };

  Contracts.prototype.init = function() {
    this.attachEvents();
    this._getContracts();

    Events.on('ServiceAPI:getContracts', this.setContracts, this);
    Events.on('UserAPI:getUsersDetails', this.render, this);
  };

  Contracts.prototype.attachEvents = function(e, users) {
    var viewOpened = $.proxy(this._viewOpened, this),
        viewProvided = $.proxy(this._viewProvided, this),
        viewHired = $.proxy(this._viewHired, this);

    this.$el.on('click', '#viewOpened', viewOpened);
    this.$el.on('click', '#viewProvided', viewProvided);
    this.$el.on('click', '#viewHired', viewHired);
  };

  Contracts.prototype.render = function(e, users) {
    var users = Util.arrayToObject(users, 'userId');

    var that = this;
    TemplateManager.get('service/contracts', {
      data: {
        hiredOpened: this.contracts.hiredOpened,
        providedOpened: this.contracts.providedOpened,
        provided: this.contracts.provided,
        hired: this.contracts.hired,
        users: users,
        formatMoney: this._formatMoney,
        i18n: this.i18n
      },
      callback: function(template) {
        that.$el.find('.loader').remove();
        that.$el.append(template);
      }
    });
  };

  Contracts.prototype._formatMoney = function(amount) {
    return Accounting.formatMoney(amount, "MR$", 2, ".", ",");
  };

  Contracts.prototype._getContracts = function() {
    ServiceAPI.getContracts();
  };

  Contracts.prototype.setContracts = function(e, contracts) {
    this.contracts = contracts;
    this._getUsersDetails();
  };

  Contracts.prototype._getUsersDetails = function() {
    var users = this._extractUsers(this.contracts);
    UserAPI.getUsersDetails(users.join(','));
  };

  Contracts.prototype._extractUsers = function(contracts) {
    var contracts = _.flatten(_.union([contracts.hiredOpened, contracts.providedOpened, contracts.provided, contracts.hired])),
        result = [];

    _.each(contracts , function(contract) {
      result.push(contract.contractor);
      result.push(contract.provider);
    });

    return _.uniq(result);
  };

  Contracts.prototype._viewOpened = function(e) {
    this._changeSelection(e);
    this.$el.find('#opened').show();
  };

  Contracts.prototype._viewProvided = function(e) {
    this._changeSelection(e);
    this.$el.find('#provided').show();
  };

  Contracts.prototype._viewHired = function(e) {
    this._changeSelection(e);
    this.$el.find('#hired').show();
  };

  Contracts.prototype._changeSelection = function(e) {
    e.preventDefault();
    this.$el.find('.pill-container').hide();
    this.$el.find('.selected').removeClass('selected');
    $(e.target).addClass('selected');
  };

  Contracts.prototype.i18n = function(role) {
    var map = {
      'IN_PROGRESS': 'Em andamento',
      'PENDING': 'Aguardando confirmação',
      'COMPLETED': 'Concluído',
      'CANCELED': 'Cancelado'
    }

    if (map[role]) {
      return map[role];
    }
    return role;
  }

  return Contracts;
});