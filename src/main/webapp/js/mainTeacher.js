$(document).ready(function(){

    window.addEventListener("load",function() {
          doAjaxPost();
        });

        $(document).on('click', 'a.courseHref', function(){
                var gettingIdCourse = {};
                if($("a.courseHref")){
                    console.log($(this).attr("id"));
                    localStorage.setItem('idCourse',$(this).attr("id"));
                    console.log(localStorage.getItem('idCourse'));
                    document.location.href = "/mainTeacher/course";
                }
                });
});

function doAjaxPost() {
   $.ajax({
           type: "POST",
           url: "/mainTeacher",
           dataType: 'json',
           success: function (data) {
            // отображаем курсы преподавателя
                $.each(data, function(index,value) {
                    console.log('id = ' + value['id'] + '; Название = '+ value['title']);
                    $("#coursesContainer").append($("#hiddenCourse").html());
                    var imageIndex = index + 1;
                    $("#coursesContainer").find(".courseLink").last().find("img").attr("src", "images/image"+imageIndex+".jpg");
                    $("#coursesContainer").find(".courseLink").last().find("a").text(value['title']);
                    $("#coursesContainer").find(".courseLink").last().find("a").attr("id", value['id']);
                  });
           }
       });
}