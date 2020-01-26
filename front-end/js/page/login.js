

$(function(){

    $('#login-btn').click(function(e){
        e.preventDefault();

        var body = {
            "email": $("#email").val(),
            "password": $("#password").val()
        };

        helpers.ajax({
            method: "POST",
            url: "/login",
            data: body,
            success: function (data) {
                // Not coming here, because there's no content
            },
            error: function (response) {
                if(response.status === 200){
                    helpers.alert('Logged in successfully', 'success');
                    navigator.setAuthentication(response.getResponseHeader('Authorization'));
                    navigator.load('index');
                }else{
                    helpers.alert('Credentials are wrong', 'danger');
                }
            }
        });

        return false;
    });
});