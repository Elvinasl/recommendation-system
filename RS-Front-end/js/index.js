$( document ).ready(function() {
    $("#login").click(function(){
        handleLogin();
    });

    $("#register").click(function(){
        handleRegister();
    });

    $("#algorithm").click(function(){
        $("#htmlDiv").load("algorithm.html");
    });
    $("#behaviour").click(function(){
        $("#htmlDiv").load("behaviour.html");
    });
    $("#client").click(function(){
        $("#htmlDiv").load("client.html");
    });
    $("#import").click(function(){
        $("#htmlDiv").load("import.html");
    });
    $("#project").click(function(){
        $("#htmlDiv").load("project.html");
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