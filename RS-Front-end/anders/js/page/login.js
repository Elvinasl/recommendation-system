

$(function(){

    $('#login-btn').click(function(e){
        e.preventDefault();

        var body = {
            "email": $("#email").val(),
            "password": $("#password").val()
        };

        $.ajax({
            url: "http://localhost:8080/register",
            method: "get",
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json',
                // 'api-key': 'a'
            },
            data: body,
            success: function (data) {
                console.log(data);
            }
        });

        return false;
    });
});