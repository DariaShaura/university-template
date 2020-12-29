$(document).ready(function(){

    window.addEventListener("load",function() {
          loadUsers();
        });
});

$(document).on('click', '.table-remove', function () {

  if(confirm('Удалить пользователя?')){

    var idUser = $($(this).parents('tr').children()[0]).text();
    var login = $($(this).parents('tr').children()[1]).text();

    deleteUser(idUser, login, $(this).parents('tr'));
   }
});

function loadUsers(){
       $.ajax({
               type: "POST",
               url: "/mainAdmin",
               dataType: 'json',
               success: function (data) {

                    var tableBody = $("#usersTable").find("tbody");

                    $.each(data, function(index, user){
                        var row = $("<tr></tr>");

                        $("<td></td>").addClass("invisible").text(user["idUser"]).appendTo(row);
                        $("<td></td>").text(user["login"]).appendTo(row);
                        $("<td></td>").text(user["role"]).appendTo(row);
                        if(user["active"]){
                            $("<td></td>").css("color","blue").text("+").appendTo(row);
                        }
                        else{
                            $("<td></td>").text("-").appendTo(row);
                        }
                        $("<td></td>").text(user["lastName"]).appendTo(row);
                        $("<td></td>").text(user["firstName"]).appendTo(row);
                        $("<td></td>").text(user["secondName"]).appendTo(row);
                        $("<td></td>").text(user["birthDate"]).appendTo(row);
                        if(!user["active"]){
                            $("<td></td>").html('<span class="table-remove"><button type="button"'+
                              'class="btn btn-outline-danger btn-rounded btn-sm my-0">Удалить</button></span>').appendTo(row);
                        }

                        row.appendTo(tableBody);
                    });
                }
               });
}

function deleteUser(idUser, login, row){
       $.ajax({
               type: "POST",
               url: "/mainAdmin/deleteUser",
               data: {"idUser" : idUser, "login" : login},
               dataType: 'json',
               success: function (data) {
                    row.detach();
               }
       });
}
