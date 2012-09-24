define(['jquery', 'bank/api', 'user/api', 'plugins/events', 'plugins/template', 'plugins/accounting', 'util'], function() {
  'use strict';

  var BankAPI = require('bank/api'),
      UserAPI = require('user/api'),
      Util = require('util'),
      Events  = require('plugins/events'),
      Accounting = require('plugins/accounting'),
      TemplateManager = require('plugins/template');

  var MyAccount = function(options) {
    this.$el = options.el;
  };

  MyAccount.prototype.init = function() {
    this._getMyAccountInfo();

    Events.on('BankAPI:getMyAccount', this.setMyAccountInfo, this);
    Events.on('UserAPI:getUsersDetails', this.render, this);
  };

  MyAccount.prototype.render = function(e, users) {
    var users = Util.arrayToObject(users, 'userId');
    var that = this;

    TemplateManager.get('bank/myAccount', {
      data: {
        users: users,
        user: this.user,
        transactions: this.transactions,
        balance: this.balance,
        formatMoney: this._formatMoney,
        formatDate: Util.formatDate
      },
      callback: function(template) {
        that.$el.find('.loader').remove();
        that.$el.append(template);
      }
    });
  };

  MyAccount.prototype.setMyAccountInfo = function(e, result) {
    this.user = result.user;
    this.balance = result.balance;
    this.transactions = result.transactions;
    this._getUsersDetails();
  };

  MyAccount.prototype._getMyAccountInfo = function() {
    BankAPI.getMyAccount();
  };

  // extract this to i10n
  MyAccount.prototype._formatMoney = function(amount, symbol) {
    symbol = symbol || '';
    return Accounting.formatMoney(amount, symbol, 2, ".", ",");
  };

  MyAccount.prototype._getUsersDetails = function() {
    var users = this._extractUsers(this.transactions);
    UserAPI.getUsersDetails(users.join(','));
  };

  MyAccount.prototype._extractUsers = function(transactions) {
    var result = [];

    _.each(transactions , function(transaction) {
      result.push(transaction.accountDest);
      result.push(transaction.accountOrig);
    });

    return _.uniq(result);
  };

  return MyAccount;
});