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

    $("#statement-form").submit(function(e) {
        var $this = $(this),
        $table = $("table"),
        $transactionsSection = $("#statement-transactions"),
        startDate = $this.find('input[name=startDate]').val(),
        endDate = $this.find('input[name=endDate]').val();
        $table.show();
        $.ajax({
            type: "POST",
            url: "/bank/statement",
            data: {startDate: startDate, endDate: endDate}
        }).success(function(data) {
            var $tbody = $table.find("tbody"); 
            $tbody.empty();
            if (data.transactions.length > 0) {
                for (var i = 0, length = data.transactions.length; i < length; i+=1) {
                    $tbody.append("<tr><td>" + data.transactions[i].date + "</td>\n" +
                                  "<td>" + data.transactions[i].accountDest + "</td>\n" +
                                  "<td>" + data.transactions[i].comment + "</td></tr>");
                }
            } else {
                $tbody.append("<tr><td colspan=\"4\">N&atilde;o houve transa&ccedil;&otilde;es nesse per&iacute;odo.</td></tr>");
            }
            $transactionsSection.show();
            $transactionsSection.find('h3').html("Transa&ccedil;&otilde;es entre " + startDate + " e " + endDate);
            // TODO set balance
        }).error(function(data) {
            console.log('N&atilde;o foi poss&iacute;vel buscar extrato');
            console.log(data);
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
            $dynamicStats.find("h3").text("Informa&ccedil;&otilde;es no intervalo de " + $startDate.val() + " a " + $endDate.val());
            $("#circulation").text(data.circulation);
            $("#amount").text(data.creditAmount);
            $dynamicStats.show();
        }).error(function(data) {
            console.log('N&atilde;o foi poss&iacute;vel recuperar stats do banco');
        });
        e.preventDefault();
    });

    var dates = $( "#startDate, #endDate" ).datepicker({
        defaultDate: "+1w",
        numberOfMonths: 2,
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
    
});