$(document).ready(function() {
  $('[data-toggle="tooltip"]').tooltip();

    $('.custom-file-input').on('change', function() {
       let fileName = $(this).val().split('\\').pop();
       $(this).next('.custom-file-label').addClass("selected").html(fileName);
    });

	$("#addClassInCourse").click(function(event){

	    // записать данные в таблицу на главной форме
	    var themeTitle = $("#themeTitle").val();
	    var themeTitleButton ='<div class="btn-group">'+
              '<button type="button" class="btn btn-outline-primary themeTitle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">'+
              themeTitle + '</button>' +
              '<div class="dropdown-menu">'+
               '<a class="dropdown-item" href="#" id="themeDelete">Удалить тему</a>'+
              '</div></div>';
	    var content = $('<table></table>');
	    content.addClass("table");
	    content.addClass("table-hover");
        for(var i=0; i<1; i++){
            var materialType = $("#selectType").val();
            var filePath = $(".custom-file-input").val().split("\\").pop();
            var description = $("#description").val();
            var row = $('<tr></tr>');
            var column1 = $('<td></td>').text(description);
            var column2 = $('<td></td>').text(materialType);
            var column3 = $('<td></td>').text(filePath);
            row.append(column1,column2,column3);
            content.append(row);
        }

        $("#tableThemes").append(themeTitleButton, content);

		$("#exampleModal").modal("hide");
	});

    $("#themeDelete").click(function(e){
        alert("sdfdfg");
    });

    $("#courseAddForm").submit(function( event ) {
      event.preventDefault();
      doAjaxPost();
    });

});

function doAjaxPost() {
            // подготовим данные для отправки
            var course = {};
            var theme = [];

            $("#courseAddForm").find("button.themeTitle").each(function(indx, element){
                var elementJ = $(element);
                var materials = [];
                var table = $(elementJ.parent().next());
                table.find('tr').each(function(row){
                	var tds = $(this).children('td');
                        materials.push({
                            title: $(tds[0]).text(),
                            type: $(tds[1]).text(),
                            path: $(tds[2]).text()
                        });
                });
                theme.push({
                    title: elementJ.text(),
                    materials: materials
                });
            });

             course["title"]= $("#courseTitle").val();
             course["description"]= $("#courseDescription").val();
             course["hours"] = $("#hours").val();
             course["teacherLogin"]= "";
             course["themes"] = theme;//,
            /*course.push({
                  title: $("#courseTitle").val(),
                  description: $("#courseDescription").val(),
                  hours: $("#hours").val(),
                  teacherLogin: ""//,
                  //themes: theme
            });*/

            let courseStr = JSON.stringify(course);



           $.ajax({
                   type: "POST",
                   contentType: "application/json",
                   url: "/mainTeacher/courseAdd/proceed",
                   data: JSON.stringify(course),
                   dataType: 'json',
                   success: function (data) {
                        console.log(data);
                        localStorage.setItem('idCourse',data);
                        document.location.href = "/mainTeacher/course";
                   }
               });
        }

