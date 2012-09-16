define(function()  {
  function getApplicationDomain() {
    var host = document.location.host;
    return host.substr(host.indexOf('.')+1);
  }

  return {
    getApplicationDomain: getApplicationDomain
  }
});

// String.prototype.normalize = function() {
//     var url = this;
//     var preserveNormalForm = /[,_`;\':-]+/gi
//     url = url.replace(preserveNormalForm, ' ');

//     // strip accents
//     url = url.stripAccents();

//     //replace spaces with a -
//     url = url.replace(/\s+/gi, '-');
//     return url;
// }

// String.prototype.stripAccents = function() {
//     var str = this,
//         from = "ÃÀÁÄÂÈÉËÊÌÍÏÎÒÓÖÔÙÚÜÛãàáäâèéëêìíïîòóöôùúüûÑñÇç",
//         to   = "AAAAAEEEEIIIIOOOOUUUUaaaaaeeeeiiiioooouuuunncc",
//         mapping = {},
//         fromLength = from.length,
//         strLength = str.length,
//         ret = [],
//         i;

//     for (i = 0; i < fromLength; i += 1) {
//         mapping[from.charAt(i)] = to.charAt(i);
//     }

//     for (i = 0; i < strLength; i += 1) {
//         var c = str.charAt(i)
//         if (mapping.hasOwnProperty(str.charAt(i))) {
//             ret.push(mapping[c]);
//         } else {
//             ret.push(c);
//         }
//     }

//     return ret.join('').replace(/[^-A-Za-z0-9]+/g, '-').toLowerCase();
// }