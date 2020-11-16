$(document).ready(function() {
  $('[data-toggle="tooltip"]').tooltip();

  $("#mainContent").html($("#courseDescriptionSection").html());	

  $("#hrefStudents").click(function(e){
  		$("#mainContent").html($("#tableStudentsSection").html());	
  });

  $("#hrefThisCourse").click(function(e){
  		$("#mainContent").html($("#courseDescriptionSection").html());	
  });

  $("#hrefGrade").click(function(e){
  		$("#mainContent").html($("#gradeStudentsSection").html());	
  });

});