class Helpers{




    ajax(options){

        let headers = {};
        if(navigator.authentication !== null){
            headers["Authorization"] = navigator.authentication;
        }
        $.ajax({
            headers: headers,
            method: typeof options['method'] === "undefined" ? "GET" : options['method'],
            url: host + options['url'],
            data: JSON.stringify(typeof options['data'] === "undefined" ? "" : options['data']),
            crossDomain: true,
            contentType: "application/json; charset=utf-8",
            dataType: 'json',
            success: function (data) {
                if(typeof options['success'] !== "undefined"){
                    options['success'](data);
                }
            },
            error: function (response){
                if(response.status === 403){
                    navigator.setAuthentication(null);
                    navigator.updateNavigation(function(navigator){
                        navigator.load("index");
                    });
                }

                if(typeof options['error'] !== "undefined"){
                    options['error'](response);
                }
            }
        });
    }


    alert(msg, type, time = 3000){
        let alert = $("<div class='alert alert-"+type+"'>"+msg+"</div>");

        $("#alerts").append(alert);

        setTimeout(function(){
            alert.slideUp(300, function(){
                $(this).remove();
            });
        }, time);
    }

    clearTableData(table){
        let resultElement = table.find("tbody");
        if(typeof resultElement !== "undefined") resultElement.html("");
    }
    addTableData(table, rows, columnOrder = null, reverse = false){
        let resultElement = table.find("tbody");
        for(let row in rows){
            if(!rows.hasOwnProperty(row)) continue;
            let tr = $("<tr></tr>");
            columnOrder = columnOrder ? columnOrder : Object.keys(rows[row]);
            for(let i=0;i<columnOrder.length;i++){
                let column = columnOrder[i];
                if(!rows[row].hasOwnProperty(column)) continue;
                let td = $("<td></td>");
                td.append(rows[row][column]);
                tr.append(td);
            }
            if(reverse){
                resultElement.prepend(tr);
            }else{
                resultElement.append(tr);
            }
        }
    }

}
let helpers = new Helpers();
