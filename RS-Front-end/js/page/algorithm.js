$(function(){

    var body = {
        "user_id":"2",
        "amount":10
    };

    helpers.ajax({
        method: "GET",
        url: "http://localhost:8081/recommendation",
        data: body,
        contentType: "application/json; charset=utf-8",
        dataType: 'json',
        success: function (data) {
            if(data.size() > 0){
                console.log(data);
            }else{
                $("#algorithmData").append("<li>No items found</li>");
            }
        },
        error: function (response) {
            console.log(response);
        }
    });
})