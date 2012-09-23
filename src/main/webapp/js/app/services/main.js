define(function(require) {
  var Contracts  = require('services/views/contracts'),
      NewService = require('services/views/newService'),
      Service    = require('services/views/service'),
      ThumbsUp   = require('services/views/thumbsUp'),
      $contracts = $('#contracts'),
      $service   = $('.service-details'),
      $newService = $('.new-service'),
      $thumbsUp  = $('.thumbs-up');

  if ($contracts.length) new Contracts({el: $contracts}).init();
  if ($newService.length) new NewService({el: $newService}).init();
  if ($service.length) new Service({el: $service}).init();
  if ($thumbsUp.length) new ThumbsUp({el: $thumbsUp}).init();
});