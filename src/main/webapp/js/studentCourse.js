$(document).ready(function() {

        window.addEventListener("load",function() {
               loadCourseListToNavBar();
               if(localStorage.getItem("prevLink") == "loadLab"){
                    localStorage.setItem("prevLink","");
                    loadMarks();
               }
               else {
                    doAjaxPost();
               }
            });


  $('[data-toggle="tooltip"]').tooltip();

  $("#toMainPage").click(function(e){
     event.preventDefault();
     document.location.href = "/mainStudent";
  });

  $("#hrefSchedule").click(function(e){
    event.preventDefault();
    loadSchedule();
  });

  $("#hrefDeleteCourse").click(function(e){
    event.preventDefault();
    if(confirm('Удалить курс?')){
        idCourse = localStorage.getItem('idCourse');
        doAjaxPostToDelete(idCourse);
    }
  });

  $("#hrefGrade").click(function(e){
    event.preventDefault();
    loadMarks();
  });

  $('body').on("click", function() {
     $(".context-menu").removeClass("show").hide();
  });

  $(".context-menu a").on("click", function() {
    $(this).parent().removeClass("show").hide();
  });

  $("#loadLab").on("click", function() {
       event.preventDefault();

       var dirToLab = localStorage.getItem("dirToLab");

       openFile("studentLab", dirToLab);
  });

  $("#deleteLab").on("click", function() {
       event.preventDefault();
       if(confirm('Удалить загруженную л/р?')){
          var idLab = localStorage.getItem("idLab");

          var tdIdLab = $(".marksTable td:first-child:contains('"+idLab+"')");


          var studentLab = {};

          studentLab["idLab"] = idLab;
          studentLab["idMark"] = $(tdIdLab.siblings()[0]).text();
          studentLab["path"] = $($($(tdIdLab.siblings()[2]).children()[1]).children()[1]).text();
          studentLab["labTitle"] = "";
          studentLab["mark"] = "0";

          deleteStudentLab(localStorage.getItem("idCourse"), studentLab);
       }
  });

});

$(document).on('click', 'a.courseHref, a.courseLink, a.dropdownCourse', function(){
   localStorage.setItem('idCourse',$(this).attr("id"));
   document.location.href = "/mainStudent/course";
});

$(document).on('click', '.upload_link', function(e){
   e.preventDefault();
   $($(this).siblings("input")).trigger('click');
});

$(document).on('change', '.upload', function(e){

           var fileName = $(this).val().split("\\").pop();

           var idLab = $($($(this).parents("tr")[0]).children()[0]).text();
           var idMark = $($($(this).parents("tr")[0]).children()[1]).text();
           var labTitle = $($($(this).parents("tr")[0]).children()[2]).text();
           var path = fileName;
           var mark = $($($(this).parents("tr")[0]).children()[4]).text();

           var labWithMarkInfo = {};
           labWithMarkInfo["idLab"] = idLab;
           labWithMarkInfo["idMark"] = idMark;
           labWithMarkInfo["labTitle"] = labTitle;
           labWithMarkInfo["path"] = path;
           labWithMarkInfo["mark"] = mark;

           uploadFileOnServer(labWithMarkInfo, this.files[0], $(this).siblings("a").children("span"));
});

$(document).on('click','a.lectureTitle', function(e){
     event.preventDefault();
     openFile("courseMaterial", $(this).attr("id"));
});

$(document).on('contextmenu', '.upload_link', function(e) {
          var labPath = $($(this).children()[1]).text();

          if(labPath != "Загрузить л/р"){
            var idLab = $($($(this).parents("tr")[0]).children()[1]).text();
            var dirToLab = $($($(this).parents("tr")[0]).children()[0]).text() + "//" + labPath;
            localStorage.setItem("dirToLab", dirToLab);
            localStorage.setItem("idLab", idLab);

            var top = e.pageY - 10;
            var left = e.pageX - 90;
            $(".context-menu").attr("id", $(this).find("a").attr("id"));
            $(".context-menu").css({
              display: "block",
              top: top,
              left: left
            }).addClass("show");
            return false; //blocks default Webbrowser right click menu
          }
});

function doAjaxPost() {
    var idCourse = localStorage.getItem('idCourse');

   $.ajax({
           type: "POST",
           url: "/mainStudent/course",
           data: {"idCourse" : idCourse},
           dataType: 'json',
           success: function (data) {
           if(data == {}){
                $("#mainContent").html('Ошибка: курс не загружен!');
           }
           // меняем заголовок сайта
           document.title = document.title +' ' + data["title"];
            // отображаем выбранный курс
                $("#hrefThisCourse").text(data["title"]);
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
                                            materialHref.attr("id",data["teacherLogin"]+"\\"+data["id"]+"\\"+ material["id"]+"\\"+material["path"]);
                                            materialHref.html(imgMaterialType + material["type"] + ":    " + material["title"]);

                                            listElement.append(materialHref);
                                            $("#themesContainer").find("ol.themeMaterials").last().append(listElement);
                                      });
                  });
                  $("#mainContent").html($("#courseDescriptionSection").html());
           }
       });
}
function loadCourseListToNavBar(){
   $.ajax({
           type: "POST",
           url: "/mainStudent",
           dataType: 'json',
           success: function (data) {
            // отображаем курсы, на которые подписан студент

                $.each(data, function(index,value) {
                    // добавляем список курсов в navBar
                    var dropDownHref = $("<a></a>");
                    dropDownHref.addClass("dropdown-item dropdownCourse");
                    dropDownHref.attr('id',value['idCourse']);
                    dropDownHref.attr('href','#');
                    dropDownHref.text(value['courseTitle']);
                    $("#afterStudentCourseList").before(dropDownHref);
                  });
           }
       });
}

function loadMarks(){
    var idCourse = localStorage.getItem('idCourse');

           $.ajax({
                   type: "POST",
                   url: "/mainStudent/course/marks",
                   data: {"idCourse" : idCourse},
                   dataType: 'json',
                   success: function (data) {
                            // отображаем оценки и сданные лабораторные
                            var courseMarksDiv = $("#courseMarksTemp").clone();
                            courseMarksDiv.attr("id", "#courseMarks");
                            var table = courseMarksDiv.find("table");

                            $.each(data, function(index, mark){
                                   var row = $("<tr></tr>");
                                       $("<td></td>").addClass("invisible").text(mark["idLab"]).appendTo(row);
                                       $("<td></td>").addClass("invisible").addClass("idMark").text(mark["idMark"]).appendTo(row);
                                       $("<td></td>").text(mark["labTitle"]).appendTo(row);
                                       if(mark["mark"] == 0){
                                         var pathToLab = mark["path"]==null ? "Загрузить л/р" : mark["path"];
                                         var addFile = '<input class="upload" type="file" accept=".pdf"/>'+
                                                          '<a href="#" class="upload_link"><i class="fa fa-download" aria-hidden="true"></i> <span>'+pathToLab+'</span></a>';

                                         $("<td></td>").html(addFile).appendTo(row);
                                       }
                                       else{
                                        $("<td></td>").text(mark["path"]).appendTo(row);
                                       }
                                       $("<td></td>").text(mark["mark"]).appendTo(row);

                                       row.appendTo(table.find("tbody"));
                            });

                            $("#mainContent").empty().append(courseMarksDiv);
                    }
                   });
}

function loadSchedule(){
        var idCourse = localStorage.getItem('idCourse');

               $.ajax({
                       type: "POST",
                       url: "/mainStudent/course/schedule",
                       data: {"idCourse" : idCourse},
                       dataType: 'json',
                       success: function (data) {
                                // отображаем расписание
                                var courseScheduleDiv = $("#courseScheduleTemp").clone();
                                courseScheduleDiv.attr("id", "#courseSchedule");
                                var table = courseScheduleDiv.find("table");

                                $.each(data, function(index, mark){
                                       var row = $("<tr></tr>");
                                           $("<td></td>").text(mark["themeTitle"]).appendTo(row);
                                           $("<td></td>").text(mark["startDate"]).appendTo(row);
                                           $("<td></td>").text(mark["endDate"]).appendTo(row);
                                           var attended = mark["attended"] == true ? "+" : "-";
                                           $("<td></td>").text(attended).appendTo(row);

                                       row.appendTo(table.find("tbody"));
                                });
                                $("#mainContent").empty().append(courseScheduleDiv);
                       }
               });

}

function uploadFileOnServer(labWithMarkInfo, file, span){
    if( typeof file == 'undefined' ) return;

    // создадим объект данных формы
    var data = new FormData();
    data.append('file',file);
    data.append('idLab',labWithMarkInfo["idLab"]);
    data.append('idCourse',localStorage.getItem('idCourse'));

    $.ajax({
                       type: "POST",
                       url: "/mainStudent/course/lab/upload",
                       data: data,
                       enctype: 'multipart/form-data',
                       cache: false,
                       processData: false,
                       contentType: false,
                       success: function (data) {
                            console.log('Файл загружен');
                            $(span).text(labWithMarkInfo["path"]);
                            updateLabInfo(labWithMarkInfo);
                        },
                       error: function(data){
                            alert('Ошибка загрузки');
                       }
    });
}

function updateLabInfo(labWithMarkInfo){
    $.ajax({
               type: "POST",
               url: "/mainStudent/course/labs/update",
               data: labWithMarkInfo,
               dataType: 'json',
               success: function (data) {
               },
               error: function(data){
                  alert('Ошибка загрузки файла в базу данных');
               }
    });
}

function openFile(fileType, materialPath){
    $.ajax({
        type: "POST",
        url: "/mainStudent/pdf",
        data: {"fileType": fileType,"materialPath" : materialPath},
        xhrFields: {
              responseType: 'blob'
        },
        success: function(data, textStatus, jqXHR) {
            let blob = new Blob([data], {type: 'application/pdf'}); //mime type is important here
                            let link = document.createElement('a'); //create hidden a tag element
                            link.href = window.URL.createObjectURL(blob); //obtain the url for the pdf file
                            link.target = '_blank'; //opens the pdf file in  new tab
                            link.click(); //imitating the click event for opening in new tab
        },
        error: function(jqXHR) {
            alert('Ошибка при открытии файла');
        }
    });
}

function doAjaxPostToDelete(idCourse){

       $.ajax({
               type: "POST",
               url: "/mainStudent/deleteCourse",
               data: {"idCourse" : idCourse},
               dataType: 'json',
               success: function (data) {
                    if(data){
                        document.location.href = "/mainStudent";
                    }
                }
               });
}

function deleteStudentLab(idCourse, studentLab){
           $.ajax({
                   type: "POST",
                   url: "/mainStudent/deleteStudentLab?idCourse="+idCourse,
                   data: studentLab,
                   dataType: 'json',
                   success: function (data) {
                        loadMarks();
                    }
                   });
}