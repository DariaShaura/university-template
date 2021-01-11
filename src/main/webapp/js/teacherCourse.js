var courseInfo={};
var errorInMaterials = false;

$(document).ready(function() {

        window.addEventListener("load",function() {
               loadCourseListToNavBar();
               doAjaxPost();
            });
  $('[data-toggle="tooltip"]').tooltip();

  $("#toMainPage").click(function(e){
     event.preventDefault();
     document.location.href = "/mainTeacher";
  });

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

       var materialIndex = $(this).parents('tr').prevAll().length;
       themeIndex = $("#editThemeList option:selected").index() - 1;

       if(courseInfo["themes"][themeIndex]["materials"][materialIndex]["id"] != "-1"){
        courseInfo["themes"][themeIndex]["materials"][materialIndex]["needAction"] = "DELETE";
        $(this).parents('tr').find("a").addClass('disabled').bind('click', false);
        $(this).parents('tr').find("button").prop('disabled', true);
       }
       else{
        courseInfo["themes"][themeIndex]["materials"].splice(materialIndex, 1);
        $(this).parents('tr').detach();
       }

       if($(this).parents('tr').prevAll().length == ($(this).parents('tbody').children('tr').length-1)){
            errorInMaterials = false;
            $(".uploadFileName").removeClass("is-invalid");
            $("#editThemeList").prop('disabled',false);
            $("#editCourseAddTheme").prop('disabled',false);
            $("#updateCourse").prop('disabled',false);
       }
  });

  $(document).on('click', '.buttonDeleteTheme', function(){

     themeIndex = $("#editThemeList option:selected").index() - 1;//$("#editThemeList").children(":selected").attr("id");

     if(editCourseDeleteTheme(themeIndex)){
        $("#editThemeList option:selected").remove();
     }
     else{
        $("#editThemeList option:selected").prop('disabled', true);
     }

    $("#tableThemes").empty();

     if(errorInMaterials){
        errorInMaterials = false;
        $(".uploadFileName").removeClass("is-invalid");
        $("#editThemeList").prop('disabled',false);
        $("#editCourseAddTheme").prop('disabled',false);
        $("#updateCourse").prop('disabled',false);
     }
  });

  $(document).on('change', '#editThemeList', function(){
    if($(this).children(":selected").attr("id") != "choose"){
        themeIndex = $("#editThemeList option:selected").index() - 1;
        showThemeCourseMaterialsOnEdit(themeIndex);
    }
  });

  $(document).on('click', 'a.dropdownCourse', function(){
        if($("a.courseHref")){
            console.log($(this).attr("id"));
            localStorage.setItem('idCourse',$(this).attr("id"));
            document.location.href = "/mainTeacher/course";
        }
  });

    $(document).on('click', '#editCourseAddMaterial', function(e){
        event.preventDefault();
        // Добавить материал
        editCourseAddMaterial();
    });

    $(document).on('click', '.upload_link', function(e){
            e.preventDefault();
            $($(this).siblings("input")).trigger('click');
    });

    $(document).on('change', '.upload', function(e){
       var fileName = $(this).val().split("\\").pop();

       var materialIndex = $(this).parents('tr').prevAll().length;

       uploadFileOnServer(materialIndex, fileName, this.files[0], $(this).siblings("a").children("span"));
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
   var newThemeTitle = $(this).val();

   if(newThemeTitle == ""){
       newThemeTitle = "Новая тема";
       $(this).val(newThemeTitle);
   }

    editCourseChangeThemeTitle(newThemeTitle);
  });

  $(document).on('blur','table#editMaterialsTable > tbody > tr > td:not(:has(button))', function(e){
    editCourseSaveMaterialsTable($(this));
  });

  $(document).on('click','.scheduleUpdate', function(e){
    updateSchedule();
  });

  $(document).on('click','.marksUpdate', function(e){
    updateMarks();
  });

  $(document).on('click','.studentsAttendenceUpdate', function(e){
      updateAttendence();
  });

  $(document).on('click','a.lectureTitle', function(e){
       event.preventDefault();
       openFile($(this).attr("id"));
  });

  $(document).on('click','a.studentLab', function(e){
      event.preventDefault();

      idStudent =  $($(this).parents('tr').children()[2]).text();
      idLab = $($(this).parents('tr').children()[1]).text();
      labPath = $(this).find('span').text();

      openLab(idStudent, courseInfo["id"], idLab, labPath);
  });


function doAjaxPost() {
    var idCourse = localStorage.getItem('idCourse');

   $.ajax({
           type: "POST",
           url: "/mainTeacher/course",
           data: {"idCourse" : idCourse},
           dataType: 'json',
           success: function (data) {
           courseInfo = data;
           // меняем заголовок сайта
           document.title = document.title +' ' + data["title"];
            // отображаем выбранный курс
                $("#hrefThisCourse").text(data["title"]);
                $("#courseTitle").text(data["title"]);
                $("#courseDescription").text(data["description"]);
                $("#hours").html(data["hours"]);

                $("#themesContainer").empty();

                $.each(data["themes"], function(index,theme) {
                    var themeDiv = $("#themeTemp").clone();
                    themeDiv.attr("id", "");

                    themeDiv.find("h5.themeTitle").text(theme["title"]);

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
                                            materialHref.attr("href","/mainTeacher/pdf");
                                            materialHref.attr("id",courseInfo["id"]+"\\"+material["id"]+"\\"+material["path"]);
                                            materialHref.html(imgMaterialType + material["type"] + ":    " + material["title"]);

                                            listElement.append(materialHref);
                                            themeDiv.find("ol.themeMaterials").append(listElement);
                                      });
                    $("#themesContainer").append(themeDiv);
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
                                 var selectType = addSelectElementForEditCourseTab();
                                 selectType.val(material["type"]);

                                 $("<td></td>").html(selectType).appendTo(row);
                                 var addFile = '<input class="upload" type="file" accept=".pdf"/>'+
                                                        '<a href="#" class="upload_link"><i class="fa fa-download" aria-hidden="true"></i> <span>'+material["path"]+'</span></a>';
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

    if(courseInfo["themes"][themeIndex]["id"] != "-1"){
        courseInfo["themes"][themeIndex]["needAction"] = "DELETE";
        return false;
    }
    else{
        courseInfo["themes"].splice(themeIndex,1);
        return true;
    }
}

function editCourseChangeThemeTitle(newTitle){
    var themeIndex = $("#editThemeList option:selected").index() - 1;//$("#editThemeList").children(":selected").attr("id");
    courseInfo["themes"][themeIndex]["title"] = newTitle;
    if(courseInfo["themes"][themeIndex]["id"] != "-1"){
        courseInfo["themes"][themeIndex]["needAction"] = "UPDATE";
    }

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

    // все предыдущие строки таблицы с материалами должны быть заполнены
    if(errorInMaterials){
        return false;
    }

    errorInMaterials = true;
    $("#editThemeList").prop('disabled',true);
    $("#editCourseAddTheme").prop('disabled',true);
    $("#updateCourse").prop('disabled',true);

    var themeIndex = $("#editThemeList option:selected").index() - 1;//("#editThemeList").children(":selected").attr("id");
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

    var addFile = '<input class="upload" type="file" accept=".pdf"/>'+
                   '<a href="#" class="upload_link"><i class="fa fa-download" aria-hidden="true"></i> <span class="uploadFileName is-invalid">Загрузить файл</span></a>';

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
    var themeIndex = $("#editThemeList option:selected").index() - 1;//$("#editThemeList").children(":selected").attr("id");
    var materialIndex = $(cell).parents('tr').prevAll().length;//+($(cell.siblings()[0]).text());
    var columnIndex = cell.index();

    if((columnIndex>=1)&&(columnIndex <=3)&&(courseInfo["themes"][themeIndex]["materials"][materialIndex]["id"]!="-1")){
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

                        doAjaxPost();
                },
                error: function (data) {
                    alert("Ошибка при обновлении курса");
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
                   url: "/mainTeacher/course/participantsAttendence",
                   data: {"idCourse" : idCourse},
                   dataType: 'json',
                   success: function (data) {
                            // отображаем расписание
                            var courseStudentsDiv = $("#courseStudentsTemp").clone();
                            courseStudentsDiv.attr("id", "#courseStudents");
                            var table = courseStudentsDiv.find("table");
                            var tableHeadRow = table.find("thead > tr");

                             $.each(courseInfo["themes"], function(index,theme) {
                                $("<th></th>").text(theme["title"]).appendTo(tableHeadRow);
                             });

                            $.each(data, function(index, courseStudents){
                                   var row = $("<tr></tr>");
                                       $("<td></td>").addClass("invisible").addClass("idStudent").text(courseStudents["idStudent"]).appendTo(row);
                                       $("<td></td>").text(courseStudents["lastName"]).appendTo(row);
                                       $("<td></td>").text(courseStudents["firstName"]).appendTo(row);
                                       $("<td></td>").text(courseStudents["secondName"]).appendTo(row);
                                       $("<td></td>").text(courseStudents["birthDate"]).appendTo(row);
                                       $.each(courseInfo["themes"], function(indexT,theme) {
                                        var inputCheckbox = $("<input></input>").attr("type","checkbox");
                                        if(courseStudents["attendenceList"][indexT]["attendence"] == true){
                                            inputCheckbox.attr("checked","checked");
                                        }
                                        $("<td></td>").append(inputCheckbox).appendTo(row);
                                       });

                                       row.appendTo(table.find("tbody"));
                            });

                            table.DataTable({
                                              "paging": false,
                                              "info": false,
                                              "language": {
                                                  "search": "Найти:"
                                              }
                                            });

                            $("#mainContent").empty().append(courseStudentsDiv);
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
                                       var addFile = '<a href="#" class="studentLab"><i class="fa fa-download" aria-hidden="true"></i> <span>'+mark["pathToLab"]+'</span></a>';
                                       $("<td></td>").append(addFile).appendTo(row);
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

function updateMarks(){
    var courseMarks = [];

    $(".marksTable > tbody  > tr").each(function() {
         var row = $(this).find('td');

         courseMarks.push({
         "id":$(row[0]).text(),
         "idLab":$(row[1]).text(),
         "idStudent":$(row[2]).text(),
         "lastName":$(row[3]).text(),
         "firstName":$(row[4]).text(),
         "labDescription":$(row[5]).text(),
         "pathToLab":$(row[6]).text(),
         "mark":$($(row[7]).children()[0]).val()
         })
    });

        $.ajax({
                   type: "POST",
                   url: "/mainTeacher/course/marks/update",
                   data: JSON.stringify(courseMarks),
                   contentType: "application/json",
                   dataType: 'json',
                   success: function (data) {
                        $(".marksTable .is-invalid").each(function(){$(this).removeClass("is-invalid");})
                        alert('Оценки обновлены');
                    },
                   error: function(data){
                        var incorrectTd = $(".marksTable td.markId").filter(function() {
                            return $(this).text() === data.responseText;
                        });

                        console.log(incorrectTd);
                        $($(incorrectTd.siblings()[7]).children()[0]).addClass("is-invalid");
                   }
                   });
}

function updateAttendence(){
    var courseAttendence = [];

    $(".studentsTable > tbody  > tr").each(function() {
         var row = $(this).find('td');

        var attendenceList = [];
        $.each(courseInfo["themes"], function(index,theme) {
            var attended = ($($(row[5+index]).children()[0]).val() == "on") ? true : false;

            attendenceList.push({
                "idTheme": theme["id"],
                "attendence": attended
            });
        });

         courseAttendence.push({
         "idStudent":$(row[0]).text(),
         "lastName":$(row[1]).text(),
         "firstName":$(row[2]).text(),
         "secondName":$(row[3]).text(),
         "birthDate":$(row[4]).text(),
         "attendenceList": attendenceList
         });
    });

        $.ajax({
                   type: "POST",
                   url: "/mainTeacher/course/participantsAttendence/update",
                   data: JSON.stringify(courseAttendence),
                   contentType: "application/json",
                   dataType: 'json',
                   success: function (data) {
                        alert('Посещаемость обновлена');
                    },
                   error: function(data){

                   }
                   });
}

function uploadFileOnServer(materialIndex, fileName,file, span){
    if( typeof file == 'undefined' ) return;

    var themeIndex = $("#editThemeList option:selected").index() - 1;//$("#editThemeList").children(":selected").attr("id");

    var tempFile = true;
    var idCourse = "";
    var idMaterial = courseInfo["themes"][themeIndex]["materials"][materialIndex]["id"];

    if(idMaterial != "-1"){
        tempFile = false;
        idCourse = courseInfo["id"];
    }

    // создадим объект данных формы
    var data = new FormData();
    data.append('file',file);
    data.append('idMaterial',idMaterial);
    data.append('idCourse',idCourse);
    data.append('tempFile',tempFile);

    $.ajax({
                       type: "POST",
                       url: "/mainStudent/courseAdd/upload",
                       data: data,
                       enctype: 'multipart/form-data',
                       cache: false,
                       processData: false,
                       contentType: false,
                       success: function (data) {
                            console.log('Файл загружен');
                            $(span).text(fileName);
                            errorInMaterials = false;
                            $(".uploadFileName").removeClass("is-invalid");
                            $("#editThemeList").prop('disabled',false);
                            $("#editCourseAddTheme").prop('disabled',false);
                            $("#updateCourse").prop('disabled',false);
                       },
                       error: function(data){
                            alert('Ошибка загрузки');
                       }
    });
}


function openFile(materialPath){
    $.ajax({
        type: "POST",
        url: "/mainTeacher/pdf",
        data: {"materialPath" : materialPath},
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

function openLab(idStudent, idCourse, idLab, labPath){
        $.ajax({
            type: "POST",
            url: "/mainTeacher/studentLab/open",
            data: {"idStudent" : idStudent, "idCourse": idCourse, "idLab": idLab, "labPath": labPath},
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