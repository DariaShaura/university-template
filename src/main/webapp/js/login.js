$(document).ready(function(){
    if($("#error").text() == "invalid login or password!"){
        $("#inputLogin").addClass("is-invalid");
        $("#inputPassword").addClass("is-invalid");
    }
    else{
                $("#inputLogin").removeClass("is-invalid");
                $("#inputPassword").removeClass("is-invalid");
            }
});