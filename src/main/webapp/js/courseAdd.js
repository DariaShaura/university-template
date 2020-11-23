$(document).ready(function() {
  $('[data-toggle="tooltip"]').tooltip();

    var themeCount = 0;

    $('.custom-file-input').on('change', function() {
       let fileName = $(this).val().split('\\').pop();
       $(this).next('.custom-file-label').addClass("selected").html(fileName);
    });

	$("#addClassInCourse").click(function(event){
	    themeCount = themeCount + 1;

	    // записать данные в таблицу на главной форме
	    var themeTitle = $("#themeTitle").val();
	    var themeTitleButton ='<div class="btn-group">'+
              '<button type="button" class=btn btn-outline-primary" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">'+
              themeTitle + '</button>' +
              '<div class="dropdown-menu">'+
               '<a class="dropdown-item" href="#" id="themeDelete">Удалить тему</a>'+
              '</div></div>';
        var hiddenInputTitle = $('<input>').attr({
                                   type: 'hidden',
                                   name: 'themeTitle',
                                   value: themeTitle
                               });
	    var content = $('<table></table>');
	    content.addClass("table");
	    content.addClass("table-hover");
        for(var i=0; i<1; i++){
            var materialType = $("#selectType").val();
            var filePath = $(".custom-file-input").val().split("\\").pop();
            var description = $("#description").val();
            var hiddenDecr = $('<input>').attr({
                                               type: 'hidden',
                                               name: 'description'+themeCount,
                                               value: description
                                           });
            var hiddenMaterialType = $('<input>').attr({
                                                           type: 'hidden',
                                                           name: 'materialType'+themeCount,
                                                           value: materialType
                                                       });
            var hiddenFilePath = $('<input>').attr({
                                                           type: 'hidden',
                                                           name: 'filePath'+themeCount,
                                                           value: filePath
                                                       });
            var row = $('<tr></tr>');
            var column1 = $('<td></td>').text(description);
            var column2 = $('<td></td>').text(materialType);
            var column3 = $('<td></td>').text(filePath);
            column1.append(hiddenDecr);
            column2.append(hiddenMaterialType);
            column3.append(hiddenFilePath);
            row.append(column1,column2,column3);
            content.append(row);
        }

        $("#tableThemes").append(themeTitleButton, hiddenInputTitle, content);

		$("#exampleModal").modal("hide");
	});

    $("#themeDelete").click(function(e){
        alert("sdfdfg");
    });

});