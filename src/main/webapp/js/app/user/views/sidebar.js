define(['plugins/events', 'plugins/showdown', 'plugins/template', 'app/user/api', 'app/domain/api', 'app/util'], function() {
  'use strict';

  var Events    = require('plugins/events'),
      Showdown  = require('plugins/showdown'),
      UserAPI   = require('app/user/api'),
      DomainAPI = require('app/domain/api'),
      Util      = require('app/util'),
      TemplateManager = require('plugins/template');

  var Sidebar = function(options) {
    this.$el = options.el;
  };

  Sidebar.prototype.init = function() {
    this.renderDescription();
    this._getUserDomains();

    // TODO: rename these events names (suggestions?)
    Events.on('userapi:getUserDomains', this._getDomainsInfo, this);
    Events.on('domainapi:getDomainsInfo', this.renderDomains, this);
  };

  Sidebar.prototype.renderDescription = function() {
    var $description = this.$el.find('.user-short-description'),
        convertedDescription = Showdown.makeHtml($description.text());

    $description.html(convertedDescription).fadeIn();
  };

  Sidebar.prototype.renderDomains = function(event, domains) {
    var $userDomains = this.$el.find('.user-domains');
    TemplateManager.get('user/sidebar', {
      data: {
        domains: domains,
        parseAvatar: this._parseAvatar,
        applicationDomain: Util.getApplicationDomain()
      },
      callback: function(template) {
        $userDomains.find('.loader').remove();
        $userDomains.append(template);
      }
    });
  };

  // private functions
  Sidebar.prototype._parseAvatar = function(domain) {
    if (domain.avatarKey) {
      return '/serve/' + domains[i].avatarKey;
    } else {
      return 'http://lorempixum.com/32/32/';
    }
  };

  Sidebar.prototype._getUserDomains = function() {
    UserAPI.getUserDomains();
  };

  Sidebar.prototype._getDomainsInfo = function(event, domains) {
    DomainAPI.getDomainsInfo(domains.join(','));
  };

  return Sidebar;
});