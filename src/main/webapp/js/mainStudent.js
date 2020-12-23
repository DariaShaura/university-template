$(document).ready(function(){

    window.addEventListener("load",function() {
          doAjaxPost();
        });
});
$(document).on('click', 'a.courseHref, a.dropdownCourse', function(){

                if($("a.courseHref")){
                    console.log($(this).attr("id"));
                    localStorage.setItem('idCourse',$(this).attr("id"));
                    console.log(localStorage.getItem('idCourse'));
                    document.location.href = "/mainStudent/course";
                }
});

function doAjaxPost() {
   $.ajax({
           type: "POST",
           url: "/mainStudent",
           dataType: 'json',
           success: function (data) {
            // отображаем курсы, на которые подписан студент
                localStorage.setItem('coursesCount', data.length);
                $.each(data, function(index,value) {
                    console.log('id = ' + value['id'] + '; Название = '+ value['title']);
                    $("#coursesContainer").append($("#hiddenCourse").html());
                    var imageIndex = index + 1;
                    $("#coursesContainer").find(".courseLink").last().find("img").attr("src", "images/image"+imageIndex+".jpg");
                    $("#coursesContainer").find(".courseLink").last().find("a").text(value['title']);
                    $("#coursesContainer").find(".courseLink").last().find("a").attr("id", value['id']);
                    // добавляем список курсов в navBar
                    var dropDownHref = $("<a></a>");
                    dropDownHref.addClass("dropdown-item dropdownCourse");
                    dropDownHref.attr('id',value['id']);
                    dropDownHref.attr('href','#');
                    dropDownHref.text(value['title']);
                    $("#afterTeacherCourseList").before(dropDownHref);
                    // добавляем в localStorage
                    localStorage.setItem('courseId'+index, value['id']);
                    localStorage.setItem('courseTitle'+index, value['title']);
                  });
                  console.log(localStorage);
           }
       });
}