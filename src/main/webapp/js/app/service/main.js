require(['jquery', 'app/service/views/contracts', 'app/service/views/newService', 'app/service/views/service', 'app/service/views/thumbsUp'], function() {
  var Contracts  = require('app/service/views/contracts'),
      NewService = require('app/service/views/newService'),
      Service    = require('app/service/views/service'),
      ThumbsUp   = require('app/service/views/thumbsUp'),
      $contracts = $('#contracts'),
      $service   = $('.service-details'),
      $newService = $('.new-service'),
      $thumbsUp  = $('.thumbs-up');

  if ($contracts.length) new Contracts({el: $contracts}).init();
  if ($newService.length) new NewService({el: $newService}).init();
  if ($service.length) new Service({el: $service}).init();
  if ($thumbsUp.length) new ThumbsUp({el: $thumbsUp}).init();
});