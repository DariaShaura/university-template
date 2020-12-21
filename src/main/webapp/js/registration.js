$(document).ready(function(){

    $('#login').blur(function(event){
       checkAvailability("login");
    });

    $('#birthDate').blur(function(event){
           checkAvailability("birthDate");
    });

   $('#login').keypress(function(event){
                    var keycode = (event.keyCode ? event.keyCode : event.which);
                    if(keycode == '13'){
                        event.preventDefault();
                    }
                });

    $('form').submit(function(){
         if($(".is-invalid").length > 0){
             event.preventDefault();
         }
    });
});

function checkAvailability(type) {

        var checkVal;
        if(type == "login"){
            checkVal = {"login" : $('#login').val()};
        }
        else if(type == "birthDate"){
            checkVal = {"birthDate" : $('#birthDate').val()};
        }

        $.ajax({
                   type: "POST",
                   url: "/registration/availability",
                   data: checkVal,
                   success: function (data, textStatus) {
                    if(type == "login"){
                        $('#login').removeClass("is-invalid");
                        $('#login').addClass("is-valid");
                    }
                    else if(type="birthDate"){
                        $('#birthDate').removeClass("is-invalid");
                        $('#birthDate').addClass("is-valid");
                    }
                   },
                   error: function(data, textStatus){
                    if(type == "login"){
                        $('#login').addClass("is-invalid");
                        $('#login-invalid-feedback').text("Пользователь с таким именем уже зарегистрирован");
                    }
                    else if(type="birthDate"){
                        $('#birthDate').addClass("is-invalid");
                        $('#birthDate-invalid-feedback').text("Возраст должен быть больше 15");
                    }
                   },
                   complete: function(data, textStatus){
                   }
                   });
}