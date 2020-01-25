

$(function(){

    $('#login-btn').click(function(e){
        e.preventDefault();

        var body = {
            "email": $("#email").val(),
            "password": $("#password").val()
        };

        helpers.ajax({
            method: "POST",
            url: "http://localhost:8081/login",
            data: body,
            success: function (data) {
                console.log(data);
            },
            error: function (response) {
                console.log(response);
            }
        });

        return false;
    });
});