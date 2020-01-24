$(function(){
    $.ajaxSetup({
        headers: {
            'api-key': "a"
        }
    });

    $("")

    {

    }

    function getAlgorithmData() {

        $.ajax({
            type: "GET",
            url: "http://localhost:8081/recommendation",
            accept: "application/json",
            body:{
                "UserId": "2"
            }
        }).done(function (data) {
            $("#algorithmData").html(data.userId);
            $("#algorithmData").html(data.amount);
        });
    }
});