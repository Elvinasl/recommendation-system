$( document ).ready(function() {
    $("#login").click(function(){
        handleLogin();
    });

    $("#register").click(function(){
        handleRegister();
    });

    function handleLogin(){
        // $.ajax({
        //     method:"post",
        //     data:{
        //         username: $("#username").val(),
        //         password: $("#password").val()
        //     }
        // });

        $.post("localhost:8081/login", {
            username: $("#username").val(),
            password: $("#password").val()
        }).done(function(data){
            console.info(data);
        });
    }

    function handleRegister(){
        $.ajax({
            method:"post",
            data:{
                username: $("#username").val(),
                password: $("#password").val()
            }
        })
    }
});