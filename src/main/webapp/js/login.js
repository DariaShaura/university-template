$(document).ready(function(){
    if($("#error").text().includes("invalid login or password!")){
        $("#inputLogin").addClass("is-invalid");
        $("#inputPassword").addClass("is-invalid");
    }
    else{
                $("#inputLogin").removeClass("is-invalid");
                $("#inputPassword").removeClass("is-invalid");
            }
});