define(function() {
  'use strict';

  // inspired by Backbone.Events
  var Events = {
    _listeners: {},

    // callback is the default context if undefined
    on: function(event, callback, context) {
      var list;

      // event and callback are required
      if (!event || !callback) return this;

      list = this._listeners[event] || (this._listeners[event] = []);
      list.push([event, callback, (context || callback)]);

      return this;
    },

    // event and callback are required and _listeners cannot be empty
    off: function(event, callback) {
      var listeners, i;

      if (!event || !callback || !(listeners = this._listeners[event])) return this;

      for (i = listeners.length; i >= 0; i--) {
        listener = listeners[i];
        if (callback === listener[1]) {
          callback.slice(i, 1)
        }
      }

      return this;
    },

    trigger: function(event) {
      var listeners, listener, callback, i;

      // _listeners cannot be empty
      if (!event || !(listeners = this._listeners[event])) return this;

      // avoiding an error if users try to get any property
      // from an undefined object
      for (i = listeners.length-1; i >= 0; i--) {
        listener = listeners[i];
        callback = listener[1];
        callback.apply(listener[2] || callback, arguments);
      }

      return this;
    }
  };

  return Events;
});