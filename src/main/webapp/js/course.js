var courseInfo={};

$(document).ready(function() {



        window.addEventListener("load",function() {
               loadCourseListToNavBar();
               doAjaxPost();
            });


  $('[data-toggle="tooltip"]').tooltip();

  $("#hrefStudents").click(function(e){
        event.preventDefault();
  		loadCourseStudents();
  });

  $("#hrefThisCourse").click(function(e){
        event.preventDefault();
  		$("#mainContent").html($("#courseDescriptionSection").html());	
  });

  $("#hrefGrade").click(function(e){
        event.preventDefault();
  		loadCourseMarks();
  });

  $("#hrefSchedule").click(function(e){
          event.preventDefault();
          loadSchedule();
    });

  $("#hrefDeleteCourse").click(function(e){
    event.preventDefault();
    if(confirm('Удалить курс?')){
        doAjaxPostToDelete();
    }
  });

  $("#hrefEditCourse").click(function(e){
    event.preventDefault();
    showEditForm();
  });

});

  $(document).on('click', '.table-remove', function () {

       var materialIndex = +($(this).parents('td').siblings('.materialIndex').text());
       themeIndex = $("#editThemeList").children(":selected").attr("id");

       courseInfo["themes"][themeIndex]["materials"][materialIndex]["needAction"] = "DELETE";
       $(this).parents('tr').detach();
  });

  $(document).on('click', '.buttonDeleteTheme', function(){
     themeIndex = $("#editThemeList").children(":selected").attr("id");
     $("#editThemeList").children(":selected").attr('disabled','disabled');
     editCourseDeleteTheme(themeIndex);
  });

  $(document).on('change', '#editThemeList', function(){
    if($(this).children(":selected").attr("id") != "choose"){
        showThemeCourseMaterialsOnEdit($(this).children(":selected").attr("id"));
    }
  });

    $(document).on('click', 'a.dropdownCourse', function(){
        if($("a.courseHref")){
            console.log($(this).attr("id"));
            localStorage.setItem('idCourse',$(this).attr("id"));
            document.location.href = "/mainTeacher/course";
        }
    });

    $(document).on('change', 'input[type="file"]', function(e){
           var fileName = e.target.files[0].name;
           $('.custom-file-label').html(fileName);
    });

    $(document).on('click', '#editCourseAddMaterial', function(e){
        event.preventDefault();
        // Добавить материал
        editCourseAddMaterial();
    });

    $(document).on('click', '.upload_link', function(e){
            e.preventDefault();
            $(".upload:hidden").trigger('click');
    });

    $(document).on('change', '.upload', function(e){
       var file = $(this).val().split("\\").pop();
       $(this).siblings("a").children('span').text(file);
    });


  $(document).on('click','#editCourseAddTheme',function(e){
    event.preventDefault();
    editCourseAddTheme();
    $("#editThemeList").change();
  });

  $(document).on('click', '#updateCourse', function(e){
      event.preventDefault();
      doAjaxPostToEdit();
  });

  $(document).on('blur','.editThemeTitle', function(e){
    editCourseChangeThemeTitle($(this).val());
  });

  $(document).on('blur','table#editMaterialsTable > tbody > tr > td:not(:has(button))', function(e){
    editCourseSaveMaterialsTable($(this));
  });

  $(document).on('click','.scheduleUpdate', function(e){
    updateSchedule();
  });

function doAjaxPost() {
    var idCourse = localStorage.getItem('idCourse');

   $.ajax({
           type: "POST",
           url: "/mainTeacher/course",
           data: {"idCourse" : idCourse},
           dataType: 'json',
           success: function (data) {
           // TODO
           // проверить, что data не пустой объект!!!
           courseInfo = data;
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
                                            materialHref.html(imgMaterialType + material["type"] + ":    " + material["title"]);

                                            listElement.append(materialHref);
                                            $("#themesContainer").find("ol.themeMaterials").last().append(listElement);
                                      });
                  });
                  $("#mainContent").html($("#courseDescriptionSection").html());
           }
       });
}

function showEditForm(){

    $("#editCourseTitle").attr("value",courseInfo["title"])
    $("#editCourseDescription").text(courseInfo["description"]);
    $("#editCoursehours").attr("value",courseInfo["hours"]);

    $("#editThemeList").empty;
    $("#editThemeList").html('<option id="choose">Выберите тему</option>');

    $.each(courseInfo["themes"], function(index,theme) {
            $("<option></option>").attr("id", index).text(theme["title"]).appendTo($("#editThemeList"));
    });


    $("#mainContent").html($("#editCourseForm").html());
}

function addSelectElementForEditCourseTab(){
        var selectType = $("<select></select>");
        selectType.addClass('form-control');
        $("<option/>", {value: "Лекция", text:"Лекция"}).appendTo(selectType);
        $("<option/>", {value: "Лабораторная", text:"Лабораторная"}).appendTo(selectType);
        return selectType;
}

function showThemeCourseMaterialsOnEdit(themeIndex){
    $("#tableThemes").empty();

    var selectType = addSelectElementForEditCourseTab();
    var addFile = '<input class="upload" type="file"/>'+
                       '<a href="#" class="upload_link"><i class="fa fa-download" aria-hidden="true"></i> <span></span></a>';

    var formTheme = $(".editTheme").clone();
    formTheme.removeClass("editTheme");
    formTheme.find(".editThemeTitle").attr("value", courseInfo["themes"][themeIndex]["title"]);
    var materialTable = $(".materials").clone();
    materialTable.removeClass("materials");

    $.each(courseInfo["themes"][themeIndex]["materials"], function(index,material) {
        if(material["needAction"] != "DELETE"){
                                 var row = $("<tr></tr>");
                                 $("<td></td>").addClass("invisible").text(index).appendTo(row);
                                 $("<td></td>").attr("contenteditable","true").text(material["title"]).appendTo(row);
                                 selectType.val(material["type"]);

                                 $("<td></td>").html(selectType).appendTo(row);
                                 $(addFile).find("span").text(material["path"]);
                                 $("<td></td>").html(addFile).appendTo(row);
                                 $("<td></td>").html('<span class="table-remove"><button type="button"'+
                                                                       'class="btn btn-outline-danger btn-rounded btn-sm my-0">Удалить</button></span>').appendTo(row);


                                 row.appendTo(materialTable.find("tbody"));
        };
    });
    formTheme.append(materialTable);
    $("#tableThemes").append(formTheme);
}

function setCourseObjectInitState(){
    courseInfo["needAction"] = "NONE";
    $.each(courseInfo["themes"], function(index,theme) {
        theme["needAction"] = "NONE";
        $.each(theme["materials"], function(index,material) {
            material["needAction"] = "NONE";
        });
    });
}

function editCourseDeleteTheme(themeIndex){
    courseInfo["themes"][themeIndex]["needAction"] = "DELETE";

    $("#tableThemes").empty();
}

function editCourseChangeThemeTitle(newTitle){
    var themeIndex = $("#editThemeList").children(":selected").attr("id");
    courseInfo["themes"][themeIndex]["title"] = newTitle;
    courseInfo["themes"][themeIndex]["needAction"] = "UPDATE";

    $("#editThemeList").children(":selected").text(newTitle);
}

function editCourseAddTheme(){
    var newTheme = {};
    newTheme["id"] = -1;
    newTheme["title"] = "Новая тема";
    newTheme["materials"] = [];
    newTheme["needAction"] = "ADD";

    var themesCount = courseInfo["themes"].length;
    courseInfo["themes"].push(newTheme);

    // отобразить
    $("<option></option>").attr("id", themesCount).text(newTheme["title"]).appendTo($("#editThemeList"));
    $("#editThemeList").val(newTheme["title"]);
}

function editCourseAddMaterial(){

    var themeIndex = $("#editThemeList").children(":selected").attr("id");
    var newMaterial = {};
    newMaterial["id"] = -1;
    newMaterial["title"] = "Описание материала";
    newMaterial["type"] = "Лекция";
    newMaterial["path"] = "";
    newMaterial["needAction"] = "ADD";

    var materialsCount = courseInfo["themes"][themeIndex]["materials"].length;
    courseInfo["themes"][themeIndex]["materials"].push(newMaterial);

    //отобразить
    var selectType = addSelectElementForEditCourseTab();

    var addFile = '<input class="upload" type="file"/>'+
                   '<a href="#" class="upload_link"><i class="fa fa-download" aria-hidden="true"></i> <span></span></a>';

    var row = $("<tr></tr>");
    $("<td></td>").addClass("invisible").text(materialsCount).appendTo(row);
    $("<td></td>").attr("contenteditable","true").text(newMaterial["title"]).appendTo(row);
    $("<td></td>").html(selectType).appendTo(row);
    $("<td></td>").html(addFile).appendTo(row);
    $("<td></td>").html('<span class="table-remove"><button type="button"'+
                                  'class="btn btn-outline-danger btn-rounded btn-sm my-0 editCourseRemoveMaterial">Удалить</button></span>').appendTo(row);
    row.appendTo($("#editMaterialsTable").find("tbody"));
}

function editCourseSaveMaterialsTable(cell){
    var themeIndex = $("#editThemeList").children(":selected").attr("id");
    var materialIndex = +($(cell.siblings()[0]).text());
    var columnIndex = cell.index();

    if((columnIndex>=1)&&(columnIndex <=3)){
        courseInfo["themes"][themeIndex]["materials"][materialIndex]["needAction"] = "UPDATE";
    }

    switch (columnIndex) {
      case 1:
        courseInfo["themes"][themeIndex]["materials"][materialIndex]["title"] = $(cell).text();
        break;
      case 2:
        courseInfo["themes"][themeIndex]["materials"][materialIndex]["type"] = $($(cell).children()[0]).val();
        break;
      case 3:
        courseInfo["themes"][themeIndex]["materials"][materialIndex]["path"] = $(cell).find("span").text();
        break;
    }
}

function doAjaxPostToDelete(){
    var idCourse = localStorage.getItem('idCourse');

       $.ajax({
               type: "POST",
               url: "/mainTeacher/course/delete",
               data: {"idCourse" : idCourse},
               dataType: 'json',
               success: function (data) {
                    if(data){
                        document.location.href = "/mainTeacher";
                    }
                }
               });
}

function doAjaxPostToEdit(){
    courseInfo["title"] = $("#editCourseTitle").val();
    courseInfo["description"] = $("#editCourseDescription").val();
    courseInfo["hours"] = $("#editCoursehours").val();
    courseInfo["needAction"] = "UPDATE";

    var indexDeleted = [];
    var indexMaterialsDeleted = [];

    $.each(courseInfo["themes"], function(index,theme) {
        if((theme["id"] == -1) && (theme["needAction"] == "DELETE")){
            indexDeleted.push(index);
        }
        else{
            indexMaterialsDeleted = [];
            $.each(theme["materials"], function(index,material) {
                        if((material["id"] == -1) && (material["needAction"] == "DELETE")){
                            indexMaterialsDeleted.push(index);
                        }
                    });
             indexMaterialsDeleted.forEach(element => theme["materials"].splice(element, 1)); //удалить ненужные материалы
        }
    });
    indexDeleted.forEach(element => courseInfo["themes"].splice(element, 1));  //удалить ненужные темы

    console.log(JSON.stringify(courseInfo));

       $.ajax({
               type: "POST",
               url: "/mainTeacher/course/edit",
               contentType: "application/json",
               data: JSON.stringify(courseInfo),
               dataType: 'json',
               success: function (data) {
                        courseInfo = data;

                        alert("Курс обновлен!");
                        // отобразить измененную информацию о курсе

                        $('#hrefThisCourse').trigger('click');
                }
               });
}

function loadCourseListToNavBar(){
    var courseListLength = localStorage.getItem('coursesCount');
    for(let i=0; i<courseListLength; i++){
        var dropDownHref = $("<a></a>");
        dropDownHref.addClass("dropdown-item dropdownCourse");
        dropDownHref.attr('id',localStorage.getItem('courseId'+i));
        dropDownHref.attr('href','#');
        dropDownHref.text(localStorage.getItem('courseTitle'+i));
        $("#afterTeacherCourseList").before(dropDownHref);
    }
}

function loadSchedule(){

       var idCourse = courseInfo["id"];

       $.ajax({
               type: "POST",
               url: "/mainTeacher/course/schedule/load",
               data: {"idCourse" : idCourse},
               dataType: 'json',
               success: function (data) {
                        // отображаем расписание
                        var courseScheduleDiv = $("#courseScheduleTemp").clone();
                        courseScheduleDiv.attr("id", "#courseSchedule");
                        var table = courseScheduleDiv.find("table");

                        $.each(data, function(index, themeSchedule){
                               var row = $("<tr></tr>");
                                   $("<td></td>").addClass("invisible").addClass("idSchedule").text(themeSchedule["id"]).appendTo(row);
                                   $("<td></td>").addClass("invisible").text(themeSchedule["idTheme"]).appendTo(row);
                                   $("<td></td>").text(themeSchedule["themeTitle"]).appendTo(row);
                                   var startDate = $("<input></input>").addClass("form-control").attr("type","date").attr("id","startDate").val(themeSchedule["startDate"]);
                                   $("<td></td>").attr("contenteditable","true").append(startDate).appendTo(row);
                                   var endDate = $("<input></input>").addClass("form-control").attr("type","date").attr("id","endDate").val(themeSchedule["endDate"]);
                                   $("<td></td>").attr("contenteditable","true").append(endDate).appendTo(row);

                                   row.appendTo(table.find("tbody"));
                        });

                        $("#mainContent").empty().append(courseScheduleDiv);
                }
               });
}

function updateSchedule(){
    var courseSchedule = [];
    var dataCorrect = true;

    $(".scheduleTable > tbody  > tr").each(function() {
         var row = $(this).find('td');

         let endDate = Date.parse($($(row[4]).children()[0]).val());
         let startDate = Date.parse($($(row[3]).children()[0]).val());

         if(endDate - startDate < 0){
            $($(row[4]).children()[0]).addClass("is-invalid");
            dataCorrect = false;
            return false;
         }
         else{
            $($(row[4]).children()[0]).removeClass("is-invalid");
         }

         courseSchedule.push({
         "id":$(row[0]).text(),
         "idTheme":$(row[1]).text(),
         "themeTitle":$(row[2]).text(),
         "startDate":$($(row[3]).children()[0]).val(),
         "endDate":$($(row[4]).children()[0]).val()
         })
    });

    if(dataCorrect){
        $.ajax({
                   type: "POST",
                   url: "/mainTeacher/course/schedule/update",
                   data: JSON.stringify(courseSchedule),
                   contentType: "application/json",
                   dataType: 'json',
                   success: function (data) {
                        $(".scheduleTable .is-invalid").each(function(){$(this).removeClass("is-invalid");})
                        alert('Расписание обновлено');
                    },
                   error: function(data){
                        var incorrectTd = $(".scheduleTable td.idSchedule").filter(function() {
                            return $(this).text() === data.responseText;
                        });
                        //TODO
                        console.log(incorrectTd);
                        $($(incorrectTd.siblings()[3]).children()[0]).addClass("is-invalid");
                   }
                   });
     }

}

function loadCourseStudents(){
    var idCourse = courseInfo["id"];

           $.ajax({
                   type: "POST",
                   url: "/mainTeacher/course/schedule/load",
                   data: {"idCourse" : idCourse},
                   dataType: 'json',
                   success: function (data) {
                            // отображаем расписание
                            var courseScheduleDiv = $("#courseScheduleTemp").clone();
                            courseScheduleDiv.attr("id", "#courseSchedule");
                            var table = courseScheduleDiv.find("table");

                            $.each(data, function(index, themeSchedule){
                                   var row = $("<tr></tr>");
                                       $("<td></td>").addClass("invisible").addClass("idSchedule").text(themeSchedule["id"]).appendTo(row);
                                       $("<td></td>").addClass("invisible").text(themeSchedule["idTheme"]).appendTo(row);
                                       $("<td></td>").text(themeSchedule["themeTitle"]).appendTo(row);
                                       var startDate = $("<input></input>").addClass("form-control").attr("type","date").attr("id","startDate").val(themeSchedule["startDate"]);
                                       $("<td></td>").attr("contenteditable","true").append(startDate).appendTo(row);
                                       var endDate = $("<input></input>").addClass("form-control").attr("type","date").attr("id","endDate").val(themeSchedule["endDate"]);
                                       $("<td></td>").attr("contenteditable","true").append(endDate).appendTo(row);

                                       row.appendTo(table.find("tbody"));
                            });

                            $("#mainContent").empty().append(courseScheduleDiv);
                    }
                   });
}

function loadCourseMarks(){
    var idCourse = courseInfo["id"];

           $.ajax({
                   type: "POST",
                   url: "/mainTeacher/course/marks",
                   data: {"idCourse" : idCourse},
                   dataType: 'json',
                   success: function (data) {
                            // отображаем расписание
                            var courseMarksDiv = $("#courseMarksTemp").clone();
                            courseMarksDiv.attr("id", "#courseMarks");
                            var table = courseMarksDiv.find("table");

                            $.each(data, function(index, mark){
                                   var row = $("<tr></tr>");
                                       $("<td></td>").addClass("invisible").addClass("idMark").text(mark["id"]).appendTo(row);
                                       $("<td></td>").addClass("invisible").text(mark["idLab"]).appendTo(row);
                                       $("<td></td>").addClass("invisible").text(mark["idStudent"]).appendTo(row);
                                       $("<td></td>").text(mark["lastName"]).appendTo(row);
                                       $("<td></td>").text(mark["firstName"]).appendTo(row);
                                       $("<td></td>").text(mark["labDescription"]).appendTo(row);
                                       $("<td></td>").text(mark["pathToLab"]).appendTo(row);
                                       var mark = $("<input></input>").addClass("form-control").attr("type","number").attr("min","2").attr("max","5").val(mark["mark"]);
                                       $("<td></td>").attr("contenteditable","true").attr("id","mark").append(mark).appendTo(row);

                                       row.appendTo(table.find("tbody"));
                            });

                            table.DataTable( {
                                                 "paging": false,
                                                 "info": false,
                                                 "language": {
                                                             "search": "Найти:"
                                                         }
                                             });
                            $("#mainContent").empty().append(courseMarksDiv);
                    }
                   });
}