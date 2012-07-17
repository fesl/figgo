/*!
 * accounting.js v0.3.2, copyright 2011 Joss Crowcroft, MIT license, http://josscrowcroft.github.com/accounting.js
 */
(function(p,z){function q(a){return!!(""===a||a&&a.charCodeAt&&a.substr)}function m(a){return u?u(a):"[object Array]"===v.call(a)}function r(a){return"[object Object]"===v.call(a)}function s(a,b){var d,a=a||{},b=b||{};for(d in b)b.hasOwnProperty(d)&&null==a[d]&&(a[d]=b[d]);return a}function j(a,b,d){var c=[],e,h;if(!a)return c;if(w&&a.map===w)return a.map(b,d);for(e=0,h=a.length;e<h;e++)c[e]=b.call(d,a[e],e,a);return c}function n(a,b){a=Math.round(Math.abs(a));return isNaN(a)?b:a}function x(a){var b=c.settings.currency.format;"function"===typeof a&&(a=a());return q(a)&&a.match("%v")?{pos:a,neg:a.replace("-","").replace("%v","-%v"),zero:a}:!a||!a.pos||!a.pos.match("%v")?!q(b)?b:c.settings.currency.format={pos:b,neg:b.replace("%v","-%v"),zero:b}:a}var c={version:"0.3.2",settings:{currency:{symbol:"$",format:"%s%v",decimal:".",thousand:",",precision:2,grouping:3},number:{precision:0,grouping:3,thousand:",",decimal:"."}}},w=Array.prototype.map,u=Array.isArray,v=Object.prototype.toString,o=c.unformat=c.parse=function(a,b){if(m(a))return j(a,function(a){return o(a,b)});a=a||0;if("number"===typeof a)return a;var b=b||".",c=RegExp("[^0-9-"+b+"]",["g"]),c=parseFloat((""+a).replace(/\((.*)\)/,"-$1").replace(c,"").replace(b,"."));return!isNaN(c)?c:0},y=c.toFixed=function(a,b){var b=n(b,c.settings.number.precision),d=Math.pow(10,b);return(Math.round(c.unformat(a)*d)/d).toFixed(b)},t=c.formatNumber=function(a,b,d,i){if(m(a))return j(a,function(a){return t(a,b,d,i)});var a=o(a),e=s(r(b)?b:{precision:b,thousand:d,decimal:i},c.settings.number),h=n(e.precision),f=0>a?"-":"",g=parseInt(y(Math.abs(a||0),h),10)+"",l=3<g.length?g.length%3:0;return f+(l?g.substr(0,l)+e.thousand:"")+g.substr(l).replace(/(\d{3})(?=\d)/g,"$1"+e.thousand)+(h?e.decimal+y(Math.abs(a),h).split(".")[1]:"")},A=c.formatMoney=function(a,b,d,i,e,h){if(m(a))return j(a,function(a){return A(a,b,d,i,e,h)});var a=o(a),f=s(r(b)?b:{symbol:b,precision:d,thousand:i,decimal:e,format:h},c.settings.currency),g=x(f.format);return(0<a?g.pos:0>a?g.neg:g.zero).replace("%s",f.symbol).replace("%v",t(Math.abs(a),n(f.precision),f.thousand,f.decimal))};c.formatColumn=function(a,b,d,i,e,h){if(!a)return[];var f=s(r(b)?b:{symbol:b,precision:d,thousand:i,decimal:e,format:h},c.settings.currency),g=x(f.format),l=g.pos.indexOf("%s")<g.pos.indexOf("%v")?!0:!1,k=0,a=j(a,function(a){if(m(a))return c.formatColumn(a,f);a=o(a);a=(0<a?g.pos:0>a?g.neg:g.zero).replace("%s",f.symbol).replace("%v",t(Math.abs(a),n(f.precision),f.thousand,f.decimal));if(a.length>k)k=a.length;return a});return j(a,function(a){return q(a)&&a.length<k?l?a.replace(f.symbol,f.symbol+Array(k-a.length+1).join(" ")):Array(k-a.length+1).join(" ")+a:a})};if("undefined"!==typeof exports){if("undefined"!==typeof module&&module.exports)exports=module.exports=c;exports.accounting=c}else"function"===typeof define&&define.amd?define([],function(){return c}):(c.noConflict=function(a){return function(){p.accounting=a;c.noConflict=z;return c}}(p.accounting),p.accounting=c)})(this);

accounting.settings = {
    currency: {
        symbol : "$",   // default currency symbol is '$'
        format: "%v", // controls output: %s = symbol, %v = value/number (can be object: see below)
        decimal : ",",  // decimal point separator
        thousand: ".",  // thousands separator
        precision : 2   // decimal places
    },
    number: {
        precision : 0,  // default precision on numbers is 0
        thousand: ".",
        decimal : ","
    }
}

/* figgo bank.js */
$(function() {
    var $searchUserInput = $(".autocomplete");
    if ($searchUserInput.length) {
        var cache = {},
            lastXhr;
        $searchUserInput.autocomplete({
            minLength: 2,
            source: function( request, response ) {
                        var term = request.term;
                        if ( term in cache ) {
                            response(  
                                    $.map( cache[term].result, function( item ) {
                                        return {
                                            label: item.name + " <" + item.userId + ">",
                                            value: item.userId
                                        };
                                    })
                            );
                            return;
                        }
        
                        lastXhr = $.getJSON( "/user/search/" + term, 
                            function( data, status, xhr ) {
                            cache[ term ] = data;
                            if ( xhr === lastXhr ) {
                                response( 
                                    $.map( data.result, function( item ) {
                                        return {
                                            label: item.name + " <" + item.userId + ">",
                                            value: item.userId
                                        };
                                    })
                                );
                            }}
                        );
                    }
        });
    }

    var $transactionsTable = $("table"),
        users = $transactionsTable.find(".user").map(function() { return this.innerHTML }).get();
    if (users.length) {
        $.get('/users/search/', {'users': users.join(",")}, function(data) {
            var i, currentLi,
                usersLength = data.result.length,
                users = data.result;
            for (i = 0; i < usersLength; i += 1) {
                currentLi = $transactionsTable.find("td[data-user='" + users[i].userId+"']");
                currentLi.text(users[i].name);
            }
            $(".loader").remove();
            $transactionsTable.fadeIn();
        }).error(function(data) {
            console.log("Não foi possível carregar dados dos usuários");
        });
    } else {
        $transactionsTable.fadeIn();
    }

    $("#statement-form").submit(function(e) {
        var $this = $(this),
        $table = $("table"),
        $transactionsSection = $("#statement-transactions"),
        startDate = $this.find('input[name=startDate]').val(),
        endDate = $this.find('input[name=endDate]').val();
        
        $table.hide();
        $.ajax({
            type: "POST",
            url: "/bank/statement",
            data: {startDate: startDate, endDate: endDate}
        }).success(function(data) {
            var $tbody = $table.find("tbody"); 
            $tbody.empty();
            if (data.transactions.length > 0) {
                var dateTmp, amountClass, isDebit,
                    tableRows = '';
                
                for (var i = 0, length = data.transactions.length; i < length; i+=1) {
                    dateTmp = new Date(data.transactions[i].date);
                    isDebit = (data.transactions[i].accountOrig === data.user.userId) ? true : false;
                    amountType = isDebit ? 'red' : 'blue';
                    accountToShow = isDebit ? data.transactions[i].accountDest : data.transactions[i].accountOrig;
                    tableRows += "<tr> \
                                    <td>" + dateTmp.getDate() + "/" + (dateTmp.getMonth()+1) + "/" + dateTmp.getFullYear() + "</td>\n \
                                    <td class=\"user\" data-user=\"" + accountToShow + "\">" + accountToShow + "</td>\n \
                                    <td>" + ((data.transactions[i].comment) ? data.transactions[i].comment : '') + "</td>\n \
                                    <td class=\"" + amountType + "\">" + ((isDebit) ? "-" : "") + $table.data('symbol') + " " + accounting.formatMoney(data.transactions[i].amount) + "</td> \
                                 </tr>";
                }
                $tbody.append(tableRows)

                var users = $tbody.find(".user").map(function() { return this.innerHTML }).get();
                if (users.length) {
                    $.get('/users/search/', {'users': users.join(",")}, function(data) {
                        var i, currentLi,
                            usersLength = data.result.length,
                            users = data.result;
                        for (i = 0; i < usersLength; i += 1) {
                            currentLi = $tbody.find("td[data-user='" + users[i].userId+"']");
                            currentLi.text(users[i].name || users[i].userId);
                        }
                        $table.fadeIn();
                    }).error(function(data) {
                        console.log("Não foi possível carregar dados dos usuários");
                    });
                }
            } else {
                $tbody.append("<tr><td colspan=\"4\">Não houve transações nesse período.</td></tr>");
            }
            $transactionsSection.show();
            $transactionsSection.find('h3').text("Transações entre " + startDate + " e " + endDate);
        }).error(function(data) {
            console.log('Não foi possível recuperar extrato', data);
        });
        e.preventDefault();
    });
    
    $("#stats-form").submit(function(e) {
        var $this = $(this),
            $startDate = $this.find('input[name=startDate]'),
            $endDate = $this.find('input[name=endDate]'),
            $dynamicStats = $("#dynamic-stats");
        $.ajax({
            type: "POST",
            url: "/bank/stats",
            data: {startDate: $startDate.val(), endDate: $endDate.val()}
        }).success(function(data) {
            $dynamicStats.find("h3").text("Informações no intervalo de " + $startDate.val() + " a " + $endDate.val());
            $("#circulation").text(accounting.formatMoney(data.amountTransactions));
            $("#amount").text(accounting.formatMoney(data.creditAmount));
            $dynamicStats.show();
        }).error(function(data) {
            console.log('Não foi possível recuperar stats do banco', data);
        });
        e.preventDefault();
    });

    if ($( "#startDate, #endDate" ).length) {
        var dates = $( "#startDate, #endDate" ).datepicker({
            defaultDate: "+1w",
            numberOfMonths: 2,
            dateFormat: "dd/mm/yy",
            onSelect: function( selectedDate ) {
                var option = this.id == "startDate" ? "minDate" : "maxDate",
                    instance = $( this ).data( "datepicker" ),
                    date = $.datepicker.parseDate(
                        instance.settings.dateFormat ||
                        $.datepicker._defaults.dateFormat,
                        selectedDate, instance.settings );
                dates.not( this ).datepicker( "option", option, date );
            }
        });
    }
    
});