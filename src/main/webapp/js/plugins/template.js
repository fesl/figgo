// heavily inspired by http://lostechies.com/derickbailey/2012/02/09/asynchronously-load-html-templates-for-backbone-views/
define(['jquery', 'lodash'], function() {
    'use strict';

    var TemplateManager = {

      _compiledTemplates: {},
      _jqXHRs: {},

      get: function(templateId, options) {
        if (!templateId || !options.callback) return;

        if (this._compiledTemplates[templateId]) {
          var compiledTemplate = this._compiledTemplates[templateId];
          return options.callback(compiledTemplate(options.data));
        }

        this._loadTemplate(templateId, options);
      },

      _loadTemplate: function(templateId, options) {
        var that = this,
            compiler = _,
            jqXHR = this._jqXHRs[templateId] || $.get('/js/templates/' + templateId + '.html');

        this._jqXHRs[templateId] = jqXHR;
        jqXHR.done(function(resp) {
          var compiledTemplate = that._compiledTemplates[templateId] = compiler.template(resp);
          options.callback(compiledTemplate(options.data));
        });
      }
  }

    return TemplateManager;
});