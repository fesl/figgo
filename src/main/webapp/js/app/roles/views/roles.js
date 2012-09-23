define(['jquery', 'roles/api'], function() {
  'use strict';

  var RolesAPI = require('roles/api');

  var Roles = function(options) {
    this.$el = options.el;
  };

  Roles.prototype.init = function() {
    this.attachEvents();
  };

  Roles.prototype.attachEvents = function(e, users) {
    var updateRole = $.proxy(this.updateRole, this),
        showForm   = $.proxy(this.showForm, this);
    this.$el.on('change', 'input[type=checkbox]', updateRole);
    this.$el.on('click', '#add-role', showForm);
    this.$el.on('click', '.delete', this._deleteRole);
  };

  Roles.prototype.updateRole = function(e) {
    var $checkbox = $(e.target);
    RolesAPI.updateRole($checkbox.data('role'), $checkbox.data('activity'), e.target.checked);
  };

  Roles.prototype.showForm = function(e) {
    e.preventDefault();
    this.$el.find('#new-role').show()
    $(e.target).hide();
  };

  Roles.prototype._deleteRole = function(e) {
    e.preventDefault();
    // clarifying: the context here is the anchor tag, not Roles
    var $this = $(this);
    if (confirm($this.data('confirm'))) {
      var form = $('<form method="post" action="' + $this.attr('href') + '"></form>');
      form.appendTo('body');
      form.submit();
    }
  };

  return Roles;
});