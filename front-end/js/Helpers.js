class Helpers{




    ajax(options){

        let headers = {};
        if(navigator.authentication !== null){
            headers["Authorization"] = navigator.authentication;
        }
        if(typeof options["api-key"] !== "undefined"){
            headers["api-key"] = options["api-key"];
        }
        // console.log(headers);
        // console.log(options);
        let data = "";
        let method = typeof options['method'] === "undefined" ? "GET" : options['method'];
        if(typeof options['data'] !== "undefined"){
            data = options['data'];
            if(method.toLowerCase() !== "get"){
                data = JSON.stringify(data);
            }
        }

        $.ajax({
            headers: headers,
            method: method,
            url: host + options['url'],
            data: data,
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


    alert(msg, type, time = 3000, element){
        let alert = $("<div class='alert alert-"+type+"'>"+msg+"</div>");

        if(typeof element !== "undefined"){
            element.append(alert);
        }else{
            $("#alerts").append(alert);
        }

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
        console.log(rows);
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
    objectSize(obj) {
        let size = 0, key;
        for (key in obj) {
            if (obj.hasOwnProperty(key)) size++;
        }
        return size;
    };
}
let helpers = new Helpers();