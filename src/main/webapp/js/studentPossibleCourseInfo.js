$(document).ready(function() {

  window.addEventListener("load",function() {
        loadCoursesList();
        loadPossibleCoursesList();
        var idCourse =  localStorage.getItem("idPossibleCourse");
        loadPossibleCourseInfo(idCourse);
  });

  $('[data-toggle="tooltip"]').tooltip();

  $("#toMainPage").click(function(e){
     event.preventDefault();
     document.location.href = "/mainStudent";
  });

  $("#hrefThisCourse").click(function(e){
     event.preventDefault();
  });
});

$(document).on('click', 'a.dropdownCourse', function(){
                    event.preventDefault();
                    console.log($(this).attr("id"));
                    localStorage.setItem('idCourse',$(this).attr("id"));
                    console.log(localStorage.getItem('idCourse'));
                    document.location.href = "/mainStudent/course";
});

$(document).on('click', 'a.courseScheduleLink', function(){
    event.preventDefault();

    var idCourse = $(this).attr("id");
    localStorage.setItem('idPossibleCourse',idCourse);

    loadPossibleCourseSchedule(idCourse);
});

$(document).on('click', 'a.courseTitleLink', function(){
    event.preventDefault();

    var idCourse = $(this).attr("id");
    localStorage.setItem('idPossibleCourse',idCourse);

    loadPossibleCourseInfo(idCourse);
});

$(document).on('click', 'a.addPossibleCourse', function(){
    event.preventDefault();

    var idCourse = localStorage.getItem('idPossibleCourse');

    addStudentCourse(idCourse);
});

function loadCoursesList(){
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
                       $("#afterTeacherCourseList").before(dropDownHref);
                     });
              }
          });
}

function loadPossibleCoursesList(){
        $.ajax({
                  type: "POST",
                  url: "/mainStudent/PossibleCourses",
                  dataType: 'json',
                  success: function (data) {
                   // отображаем курсы, на которые подписан студент
                       $.each(data, function(index,value) {
                          var courseTitle = $("#hiddenCourseTitleSchedule").find("li.hiddenCourseTitle").clone();
                          courseSchedule = courseTitle.find("li.hiddenCourseSchedule");
                          courseTitle.find("a").attr("id", value["idCourse"]);
                          courseTitle.find("a").html('<i class="fa fa-folder-o fa-lg" aria-hidden="true"></i> '+value["courseTitle"]);
                          courseSchedule.find("a").attr("id", value["idCourse"]);
                          courseSchedule.find("a").html('<i class="fa fa-calendar fa-lg" aria-hidden="true"></i> Расписание');

                          $("#possibleCoursesList").append(courseTitle);
                       });
                  }
              });
}

function loadPossibleCourseInfo(idCourse){

            $.ajax({
                      type: "POST",
                      url: "/mainStudent/PossibleCourses/info",
                      data: {"idCourse" : idCourse},
                      dataType: 'json',
                      success: function (data) {
                      $("#hrefThisCourse").text(data["title"]);
                      var courseDescrDiv = $("#courseDescriptionSectionTemp").clone();
                      courseDescrDiv.attr("id", "#courseDescriptionSection");

                      courseDescrDiv.find("#courseTitle").text(data["title"]);
                      courseDescrDiv.find("#courseDescription").text(data["description"]);
                      courseDescrDiv.find("#hours").append(data["hours"]);

                      $.each(data["themes"], function(index,theme) {
                             courseDescrDiv.find("#themesContainer").append($("#theme").html());
                             courseDescrDiv.find("#themesContainer").find("h5.themeTitle").last().text(theme["title"]);

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

                                 var materialHref = $("<span></span>");
                                 materialHref.addClass("lectureTitle");
                                 materialHref.text(material["type"] + ":    " + material["title"]);
                                 listElement.append(imgMaterialType).append(materialHref);
                                 courseDescrDiv.find("#themesContainer").find("ol.themeMaterials").last().append(listElement);
                             });
                      });

                      $("#mainContent").empty().append(courseDescrDiv);
                      }
            });
}

function loadPossibleCourseSchedule(idCourse){
    $.ajax({
                      type: "POST",
                      url: "/mainStudent/PossibleCourses/info/schedule",
                      data: {"idCourse" : idCourse},
                      dataType: 'json',
                      success: function (data) {
                        var courseScheduleDiv = $("#courseScheduleTemp").clone();
                        courseScheduleDiv.attr("id", "#courseSchedule");
                        var tableBody = courseScheduleDiv.find("table").find(".scheduleTbody");

                         $.each(data, function(index,themeSchedule) {
                            var row = $("<tr></tr>");
                            $("<td></td>").addClass("invisible").addClass("idSchedule").text(themeSchedule["id"]).appendTo(row);
                            $("<td></td>").addClass("invisible").text(themeSchedule["idTheme"]).appendTo(row);
                            $("<td></td>").text(themeSchedule["themeTitle"]).appendTo(row);
                            $("<td></td>").text(themeSchedule["startDate"]).appendTo(row);
                            $("<td></td>").text(themeSchedule["endDate"]).appendTo(row);

                            row.appendTo(tableBody);
                         });
                       $("#mainContent").empty().append(courseScheduleDiv);
                      }
    });
}

function addStudentCourse(idCourse){
     $.ajax({
               type: "POST",
               url: "/mainStudent/PossibleCourses/AddCourse",
               data: {"idCourse" : idCourse},
               dataType: 'json',
               success: function (data) {
                     window.close();
               }
     });
}