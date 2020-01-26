

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
                helpers.alert("Unfortunately we couldn't register you. We need to work on exceptions, so we can't say you what's wrong.", "danger", 5000);
            }
        });

        return false;
    });
});