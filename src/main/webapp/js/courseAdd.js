$(document).ready(function() {
  $('[data-toggle="tooltip"]').tooltip();

    $("#courseAddForm").submit(function( event ){
		event.preventDefault();
		$(location).attr('href',"course.html");
	});

	//$("#addClassInCourse").click(function(event){
	//	$("#classAddForm").validate();

		//if ($("#classAddForm").checkValidity() === false){
		//	alert( "Привет" );
		//}
		//else{
		//	$("#addClassInCourseModal").modal('hide')
		//};
	//});

});