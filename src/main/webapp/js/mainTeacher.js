$(document).ready(function(){

    'use strict';

    var coursesList = $("#coursesList").val();

    console.log(coursesList[1]);

    window.addEventListener('load', function() {
        doAjaxPost();
      }, false);

	//$.each(coursesList,function(key, data){

    //   $each(data, function(index, value){

                    //$.each(value, function(i, v){
                    //    console.log('Индекс: ' + v['id'] + '; Значение: ' + v['title']);
                    //});
       //});

    //});
});

function doAjaxPost() {
    $.ajax({
        type: "POST",
        url: "/mainTeacher",
        dataType: "json"}).
        done( function(response){
            console.log(response.length);
         });
    }