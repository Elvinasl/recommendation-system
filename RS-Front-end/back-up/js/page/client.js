$(document).ready(function(){

    $("#send").click(function(){
       sendClientData();
    });

    function sendClientData(){
        var client = new XMLHttpRequest();
        client.open("POST", "http://localhost:8081/client");
        client.setRequestHeader("Access-Control-Allow-Origin", true);
        client.send();

        var projectArray = $("[name*=project]").map(function(i,el){return el.value});

        $.post("http://localhost:8081/client", {
            email: $("#email").val(),
            password: $("#password").val(),
            project: projectArray
        }).done(function(data){
            console.info(data);
        });
    }
});