$(document).ready(function(){

    window.addEventListener("load",function() {
          var previousPage = document.referrer;
          console.log(previousPage);
          if(previousPage == "http://localhost:8080/mainStudent/PossibleCourses/info"){
            $("#loadPossibleCourses").hide();
            loadPossibleCourses();
          }
          else{
            $("#loadPossibleCourses").show();
            doAjaxPost();
          }
        });

    $("#loadPossibleCourses").click(function(e){
        event.preventDefault();
        $(this).hide();
        loadPossibleCourses();
    });

    $("#linkToAllPossibleCourses").click(function(e){
         event.preventDefault();
         $("#loadPossibleCourses").hide();
         loadPossibleCourses();
    });

    $("#toMainPage").click(function(e){
        event.preventDefault();
        document.location.href = "/mainStudent";
    });

    $('body').on("click", function() {
       $(".context-menu").removeClass("show").hide();
    });

    $(".context-menu a").on("click", function() {
      $(this).parent().removeClass("show").hide();
    });

    $("#deleteCourse").on("click", function(){
        event.preventDefault();

        var idCourse = $(this).parent().attr("id");


        if(confirm('Удалить курс?')){
                doAjaxPostToDelete(idCourse);
        }
    });

    $("#loadLab").on("click", function(){
        event.preventDefault();

        var idCourse = $(this).parent().attr("id");
        localStorage.setItem("idCourse", idCourse);
        localStorage.setItem("prevLink", "loadLab");

        document.location.href = "/mainStudent/course";
    });
});
$(document).on('contextmenu', '.courseLink', function(e) {
          var top = e.pageY - 10;
          var left = e.pageX - 90;
          $(".context-menu").attr("id", $(this).find("a").attr("id"));
          $(".context-menu").css({
              display: "block",
              top: top,
              left: left
          }).addClass("show");
          return false; //blocks default Webbrowser right click menu
});

$(document).on('click', 'a.courseHref, a.courseLink, a.dropdownCourse', function(){
                    localStorage.setItem('idCourse',$(this).attr("id"));
                    document.location.href = "/mainStudent/course";
});
$(document).on('click', 'a.details', function(){
    event.preventDefault();

    var idCourse = $($(this).parents(".col")[0]).find(".possibleCourseId").text();
    localStorage.setItem('idPossibleCourse',idCourse);
     window.open('/mainStudent/PossibleCourses/info', '');
});

$(document).on('click','button.addPossibleCourse',function(){
    var idCourse = $($(this).parents(".col")[0]).find(".possibleCourseId").text();

    addStudentCourse(idCourse);
});

function doAjaxPost() {
   $.ajax({
           type: "POST",
           url: "/mainStudent",
           dataType: 'json',
           success: function (data) {
            // отображаем курсы, на которые подписан студент
                localStorage.setItem('coursesCount', data.length);
                var coursesContainer = $("#hiddenCoursesContainer").clone();
                coursesContainer.attr("id","coursesContainer");

                // очищаем navBar
                $("#afterTeacherCourseList").prevAll().remove();

                $.each(data, function(index,value) {
                    console.log('id = ' + value['idCourse'] + '; Название = '+ value['courseTitle']);

                    coursesContainer.append($("#hiddenCourse").html());
                    var imageIndex = index + 1;
                    coursesContainer.find(".courseLink").last().find("img").attr("src", "images/image"+imageIndex+".jpg");
                    coursesContainer.find(".courseLink").last().find("a").text(value['courseTitle']);
                    coursesContainer.find(".courseLink").last().find("a").attr("id", value['idCourse']);

                    // добавляем список курсов в navBar
                    var dropDownHref = $("<a></a>");
                    dropDownHref.addClass("dropdown-item dropdownCourse");
                    dropDownHref.attr('id',value['idCourse']);
                    dropDownHref.attr('href','#');
                    dropDownHref.text(value['courseTitle']);
                    $("#afterTeacherCourseList").before(dropDownHref);
                    // добавляем в localStorage
                    localStorage.setItem('courseId'+index, value['idCourse']);
                    localStorage.setItem('courseTitle'+index, value['courseTitle']);
                  });

                  $("#mainContainer").empty();
                  coursesContainer.appendTo($("#mainContainer"));

                  console.log(localStorage);
           }
       });
}

function loadPossibleCourses() {
   $.ajax({
           type: "POST",
           url: "/mainStudent/PossibleCourses",
           dataType: 'json',
           success: function (data) {
            if(data.length == 0){
                var p = $("<p></p>").text("Нет доступных курсов для добавления");
                $("#mainContainer").empty();
                $("#mainContainer").append(p);
            }
            else{
            // отображаем курсы, на которые еще не подписан студент
                possibleCoursesContainer = $("#hiddenPossibleCoursesContainer").clone();
                possibleCoursesContainer.attr("id","possibleCoursesContainer");

                $.each(data, function(index,value) {
                    let possibleCourse = $($("#possibleHiddenCourse").children()[0]).clone();
                    possibleCourse.find(".possibleCourseId").text(value["idCourse"]);
                    possibleCourse.find(".possibleCourseTitle").text(value["courseTitle"]);
                    possibleCourse.find(".teacherName").text(value["teacherName"]);
                    possibleCourse.find(".hours").append(value["hours"]);

                    possibleCourse.appendTo(possibleCoursesContainer);
                  });

                $("#mainContainer").empty();
                possibleCoursesContainer.appendTo($("#mainContainer"));
            }
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
                     doAjaxPost();
                     loadPossibleCourses();
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
                        doAjaxPost();
                    }
                }
               });
}