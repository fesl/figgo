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
            console.log("não foi possível carregar dados dos usuários");
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
                var dateTmp, 
                    amountClass, 
                    isDebit;
                
                for (var i = 0, length = data.transactions.length; i < length; i+=1) {
                    dateTmp = new Date(data.transactions[i].date);
                    isDebit = (data.transactions[i].accountOrig === data.user.userId) ? true : false;
                    amountType = isDebit ? 'red' : 'blue';
                    accountToShow = isDebit ? data.transactions[i].accountDest : data.transactions[i].accountOrig;
                    console.log(data.transactions[i]);
                    $tbody.append("<tr><td>" + dateTmp.getDate() + "/" + (dateTmp.getMonth()+1) + "/" + dateTmp.getFullYear() + "</td>\n" +
                                  "<td class=\"user\" data-user=\"" + accountToShow + "\">" + accountToShow + "</td>\n" +
                                  "<td>" + ((data.transactions[i].comment) ? data.transactions[i].comment : '') + "</td>\n" +
                                  "<td class=\"" + amountType + "\">" + ((isDebit) ? "-" : "") + "M$ " + data.transactions[i].amount.toFixed(2) + "</td></tr>");
                }

                var users = $tbody.find(".user").map(function() { return this.innerHTML }).get();
                if (users.length) {
                    $.get('/users/search/', {'users': users.join(",")}, function(data) {
                        var i, currentLi,
                            usersLength = data.result.length,
                            users = data.result;
                        for (i = 0; i < usersLength; i += 1) {
                            currentLi = $tbody.find("td[data-user='" + users[i].userId+"']");
                            currentLi.text(users[i].name);
                        }
                        $table.fadeIn();
                    }).error(function(data) {
                        console.log("não foi possível carregar dados dos usuários");
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
            $("#circulation").text(data.circulation);
            $("#amount").text(data.creditAmount);
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