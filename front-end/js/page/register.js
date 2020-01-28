

$(function(){

    $('#register-btn').click(function(e){
        e.preventDefault();

        var body = {
            "email": $("#email").val(),
            "password": $("#password").val()
        };

        helpers.ajax({
            method: "POST",
            url: "/register",
            data: body,
            success: function (data) {
                // Do something with the data?
                helpers.alert("You've been successful registered.", "success");
                navigator.load('login');
            },
            error: function (response) {
                // Do something with the response
                if(response.status === 409){
                    let data = response.responseJSON;

                    if(typeof data['message'] !== "undefined"){
                        helpers.alert(data['message'], "danger", 5000);
                    }else{
                        helpers.alert("Something went wrong", "danger", 5000);
                    }
                }else{
                    helpers.alert("Something went wrong", "danger", 5000);
                }
            }
        });

        return false;
    });
});