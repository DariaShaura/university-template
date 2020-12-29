var themeList = [];
var errorInMaterials = false;

$(document).ready(function() {
  $('[data-toggle="tooltip"]').tooltip();

   window.addEventListener("load",function() {
        loadInfo();
   });

    $("#toMainPage").click(function(e){
       event.preventDefault();
       document.location.href = "/mainTeacher";
    });

    $("#addCourse").click(function(e){
       event.preventDefault();
       doAjaxPost();
    });

});

$(document).on('click', 'a.courseHref, a.dropdownCourse', function(){
     localStorage.setItem('idCourse',$(this).attr("id"));
     document.location.href = "/mainTeacher/course";
});

$(document).on('click','#editCourseAddTheme',function(e){
  event.preventDefault();

  editCourseAddTheme();
  $("#editThemeList").change();
});

$(document).on('change', '#editThemeList', function(e){

    if($(this).children(":selected").attr("id") != "choose"){
        var themeIndex = $("#editThemeList option:selected").index() - 1;
        showThemeCourseMaterialsOnEdit(themeIndex);
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

    uploadFileOnServer(fileName, this.files[0], $(this).siblings("a").children("span"));
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

$(document).on('click', '.table-remove', function () {

   var materialIndex = $(this).parents('tr').prevAll().length;
   var themeIndex = $("#editThemeList option:selected").index() - 1;

   themeList[themeIndex]["materials"].splice(materialIndex, 1);

   if($(this).parents('tr').prevAll().length == ($(this).parents('tbody').children('tr').length-1)){
       errorInMaterials = false;
       $(".uploadFileName").removeClass("is-invalid");
       $("#editThemeList").prop('disabled',false);
       $("#editCourseAddTheme").prop('disabled',false);
       $("#addCourse").prop('disabled',false);
   }

   $(this).parents('tr').detach();
});

$(document).on('click', '.buttonDeleteTheme', function(){

     themeIndex = $("#editThemeList option:selected").index() - 1;
     $("#editThemeList").children(":selected").remove();

     themeList.splice(themeIndex, 1);

     $("#tableThemes").empty();

     if(errorInMaterials){
        errorInMaterials = false;
        $(".uploadFileName").removeClass("is-invalid");
        $("#editThemeList").prop('disabled',false);
        $("#editCourseAddTheme").prop('disabled',false);
        $("#updateCourse").prop('disabled',false);
     }
});

function editCourseAddTheme(){
    var newTheme = {};
    newTheme["title"] = "Новая тема";
    newTheme["materials"] = [];

    var themesCount = themeList.length;
    themeList.push(newTheme);

    // отобразить
    $("<option></option>").attr("id", themesCount).text(newTheme["title"]).appendTo($("#editThemeList"));
    $("#editThemeList").val(newTheme["title"]);
}

function addSelectElementForEditCourseTab(){
        var selectType = $("<select></select>");
        selectType.addClass('form-control');
        $("<option/>", {value: "Лекция", text:"Лекция"}).appendTo(selectType);
        $("<option/>", {value: "Лабораторная", text:"Лабораторная"}).appendTo(selectType);
        return selectType;
}

function editCourseChangeThemeTitle(newTitle){
    var themeIndex = $("#editThemeList").children(":selected").attr("id");
    themeList[themeIndex]["title"] = newTitle;

    $("#editThemeList").children(":selected").text(newTitle);
}

function editCourseAddMaterial(){

    // все предыдущие строки таблицы с материалами должны быть заполнены
    if(errorInMaterials){
        return false;
    }

    errorInMaterials = true;
    $("#editThemeList").prop('disabled',true);
    $("#editCourseAddTheme").prop('disabled',true);
    $("#addCourse").prop('disabled',true);

    var themeIndex = $("#editThemeList").children(":selected").attr("id");
    var newMaterial = {};
    newMaterial["title"] = "Описание материала";
    newMaterial["type"] = "Лекция";
    newMaterial["path"] = "";

    var materialsCount = themeList[themeIndex]["materials"].length;
    themeList[themeIndex]["materials"].push(newMaterial);

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

function showThemeCourseMaterialsOnEdit(themeIndex){
    $("#tableThemes").empty();

    var selectType = addSelectElementForEditCourseTab();

    var formTheme = $(".editTheme").clone();
    formTheme.removeClass("editTheme");
    formTheme.find(".editThemeTitle").attr("value", themeList[themeIndex]["title"]);
    var materialTable = $(".materials").clone();
    materialTable.removeClass("materials");

    $.each(themeList[themeIndex]["materials"], function(index,material) {
                                 var row = $("<tr></tr>");
                                 $("<td></td>").addClass("invisible").text(index).appendTo(row);
                                 $("<td></td>").attr("contenteditable","true").text(material["title"]).appendTo(row);
                                 selectType.val(material["type"]);

                                 $("<td></td>").html(selectType).appendTo(row);
                                  var addFile = '<input class="upload" type="file" accept=".pdf"/>'+
                                                        '<a href="#" class="upload_link"><i class="fa fa-download" aria-hidden="true"></i> <span>'+material["path"]+'</span></a>';
                                 $("<td></td>").html(addFile).appendTo(row);
                                 $("<td></td>").html('<span class="table-remove"><button type="button"'+
                                                                       'class="btn btn-outline-danger btn-rounded btn-sm my-0">Удалить</button></span>').appendTo(row);


                                 row.appendTo(materialTable.find("tbody"));
    });
    formTheme.append(materialTable);
    $("#tableThemes").append(formTheme);
}

function editCourseSaveMaterialsTable(cell){
    var themeIndex = $("#editThemeList").children(":selected").attr("id");
    var materialIndex = $(cell).parents('tr').prevAll().length;//+($(cell.siblings()[0]).text());
    var columnIndex = cell.index();

    switch (columnIndex) {
      case 1:
        themeList[themeIndex]["materials"][materialIndex]["title"] = $(cell).text();
        break;
      case 2:
        themeList[themeIndex]["materials"][materialIndex]["type"] = $($(cell).children()[0]).val();
        break;
      case 3:
        themeList[themeIndex]["materials"][materialIndex]["path"] = $(cell).find("span").text();
        break;
    }
}

function loadInfo() {
   $.ajax({
           type: "POST",
           url: "/mainTeacher",
           dataType: 'json',
           success: function (data) {
            // отображаем курсы преподавателя
                $.each(data, function(index,value) {
                    // добавляем список курсов в navBar
                    var dropDownHref = $("<a></a>");
                    dropDownHref.addClass("dropdown-item dropdownCourse");
                    dropDownHref.attr('id',value['id']);
                    dropDownHref.attr('href','#');
                    dropDownHref.text(value['title']);
                    $("#afterTeacherCourseList").before(dropDownHref);
                  });
                  console.log(localStorage);
           }
       });
}

function doAjaxPost() {
            // подготовим данные для отправки
            var course = {};

             course["id"] = 0;
             course["title"]= $("#courseTitle").val();
             course["description"]= $("#courseDescription").val();
             course["hours"] = $("#hours").val();
             course["teacherLogin"]= "";
             course["themes"] = themeList;

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

function uploadFileOnServer(fileName,file, span){
    if( typeof file == 'undefined' ) return;

    // создадим объект данных формы
    var data = new FormData();
    data.append('file',file);
    data.append('idMaterial','');
    data.append('idCourse','');
    data.append('tempFile',true);

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
                            errorInMaterials = false;
                            $("#editThemeList").prop('disabled',false);
                            $("#editCourseAddTheme").prop('disabled',false);
                            $(".uploadFileName").removeClass("is-invalid");
                            $("#addCourse").prop('disabled',false);

                            $(span).text(fileName);
                       },
                       error: function(data){
                            alert('Ошибка загрузки');
                       }
    });
}

