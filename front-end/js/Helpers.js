class Helpers{




    ajax(options){

        $.ajax({
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

}
let helpers = new Helpers();
