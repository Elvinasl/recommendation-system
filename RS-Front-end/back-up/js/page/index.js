$( document ).ready(function() {

    function handleLogin(){
        $.post("http://localhost:8081/login", {
            username: $("#email").val(),
            password: $("#password").val()
        }).done(function(data){
            console.info(data);
        });
    }

    function handleRegister(){
        /*
        $.ajax({
            method:"post",
            data:{
                username: $("#username").val(),
                password: $("#password").val()
            }
        })
        */
    }
});