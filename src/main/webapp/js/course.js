$(document).ready(function() {
        window.addEventListener("load",function() {
               doAjaxPost();
            });


  $('[data-toggle="tooltip"]').tooltip();

  $("#mainContent").html($("#courseDescriptionSection").html());	

  $("#hrefStudents").click(function(e){
  		$("#mainContent").html($("#tableStudentsSection").html());	
  });

  $("#hrefThisCourse").click(function(e){
  		$("#mainContent").html($("#courseDescriptionSection").html());	
  });

  $("#hrefGrade").click(function(e){
  		$("#mainContent").html($("#gradeStudentsSection").html());	
  });

});
function doAjaxPost() {
    var idCourse = localStorage.getItem('idCourse');


   $.ajax({
           type: "POST",
           url: "/mainTeacher/course",
           data: {"idCourse" : idCourse},
           dataType: 'json',
           success: function (data) {
           console.log(data["title"]);
            console.log(JSON.stringify(data));
            // отображаем выбранный курс
                $("#courseTitle").text(data["title"]);
                $("#courseDescription").text(data["description"]);
                $("#hours").append(data["hours"]);
                $.each(data["themes"], function(index,theme) {
                    $("#themesContainer").append($("#theme").html());
                    $("#themesContainer").find("h5.themeTitle").last().text(theme["title"]);

                    $.each(theme["materials"], function(index,material) {
                                            var imgMaterialType;
                                            switch (material["type"]) {
                                              case "Лекция":
                                                imgMaterialType = '<i class="fa fa-book fa-lg" aria-hidden="true"></i>';
                                                break;
                                              case "Лабораторная":
                                                imgMaterialType ='<i class="fa fa-pencil-square-o fa-lg" aria-hidden="true"></i>';
                                                break;
                                            }
                                            var listElement = $("<li></li>");
                                            listElement.addClass("mb-3");

                                            var materialHref = $("<a></a>");
                                            materialHref.addClass("lectureTitle");
                                            materialHref.attr("href","#");
                                            materialHref.html(imgMaterialType + material["type"] + ":    " + material["title"]);

                                            listElement.append(materialHref);
                                            $("#themesContainer").find("ol.themeMaterials").last().append(listElement);
                                      });
                  });
           }
       });
}