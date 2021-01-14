<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
	<meta name="author" content="Daria Shaura">
	<title>Курc:</title>
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css" integrity="sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2" crossorigin="anonymous">
	<link rel="stylesheet" href="/css/studentPossibleCourseInfo.css">
  <link rel="icon" href="/images/favicon.ico" type="image/x-icon" />
  <link rel="shortcut icon" href="/images/favicon.ico" type="image/x-icon" />
  <script src="https://use.fontawesome.com/450e77e423.js"></script>
</head>
<body>
  <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <a href="#" id="toMainPage" data-toggle='tooltip' title='Главная страница'><i class="fa fa-graduation-cap fa-2x" id="bigCap" aria-hidden="true"></i></a>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
      <ul class="navbar-nav mr-auto">
        <li class="nav-item dropdown">
          <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
            Курсы
          </a>
          <div class="dropdown-menu" aria-labelledby="navbarDropdown" id="teachersCourseList">
            <div class="dropdown-divider" id="afterTeacherCourseList"></div>
            <a class="dropdown-item" href="/mainTeacher/courseAdd">Добавить курс</a>
          </div>
        </li>
      </ul>
      <ul class="navbar-nav justify-content-end">
         <li class="nav-item dropdown">
          <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
            <i class="fa fa-user-circle-o fa-lg" id="idCircle" aria-hidden="true"></i>
            ${login}
          </a>
          <div class="dropdown-menu" aria-labelledby="navbarDropdown">
            <a class="dropdown-item" href="/logout">Выход</a>
        </li>
      </ul>
    </div>
  </nav>
  <div class="container-fluid">
    <nav class="navbar navbar-expand-lg navbar-light">
      <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
          <li class="nav-item active">
            <a class="nav-link" href="/mainStudent">Курсы > <span class="sr-only">(current)</span></a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="#" id="hrefThisCourse"></a>
          </li>
        </ul>
      </div>
    </nav>
    <!-- main regions -->
    <div class="row mb-2">
      <nav class="col-md-2 d-none d-md-block bg-light sidebar">
          <div class="sidebar-sticky">
            <ul class="nav flex-column" id="possibleCoursesList">

            </ul>
          </div>
        </nav>
    <div class="col-md-8">
        <div class="col p-4 bg-light rounded" id="mainContent">

        </div>
  </div>
  </div>

<div id="hiddenCourseTitleSchedule">
 <li class="nav-item hiddenCourseTitle">
       <a class="nav-link active courseTitleLink" href="#" id=""><i class="fa fa-folder-o fa-lg" aria-hidden="true"></i> </a>
       <ul id="schedule">
            <li class="hiddenCourseSchedule">
                        <a class="nav-link active courseScheduleLink" href="#" id=""><i class="fa fa-calendar fa-lg" aria-hidden="true"></i> </a>
            </li>
       </ul>
 </li>
</div>


  <div id="courseDescriptionSectionTemp">
     <h3 class="pb-1 text-center" id="courseTitle"></h3>
           <%--<a href="#" data-toggle="tooltip" title="Some tooltip text!">Hover over me</a>--%>
           <div>
              <h4 class="courseParts">Описание курса</h4>
              <p id="courseDescription">
              </p>
              <p id="hours">
                <span class="hours">Количество часов</span>:
              </p>
              <hr>
              <div id="themesContainer">
              <div id="theme">
              <h5 class="themeTitle"></h5>
              <ol class="list-unstyled themeMaterials">
              </ol>
              <hr>
              </div>
              </div>
           </div>
     <p><a class="btn btn-primary btn-lg addPossibleCourse" href="#" role="button">Добавить курс</a></p>
  </div>

  <div id="courseScheduleTemp">
  <div class="table-responsive">
    <table class="table scheduleTable table-hover">
                    <thead>
                        <tr>
                            <th scope="col" class="invisible .col- scheduleIndex">ID_schedule</th>
                            <th scope="col" class="invisible .col- themeIndex">ID_theme</th>
                            <th class="text-center">Название темы</th>
                            <th class="text-center">Начало</th>
                            <th class="text-center">Конец</th>
                        </tr>
                    </thead>
                    <tbody class="scheduleTbody">
                    </tbody>
    </table>
  </div>
  <p><a class="btn btn-primary btn-lg addPossibleCourse" href="#" role="button">Добавить курс</a></p>
  </div>

  <footer class="text-muted">
  <div class="container">
    <p class="float-right">
      <a href="#">Вверх</a>
    </p>
    <p> © 2020</p>
  </div>
</footer>
    <!-- jQuery and Bootstrap Bundle (includes Popper) -->
    <script src="/js/jquery-3.5.1.js"></script>
    <script src="/bootstrap/js/bootstrap.bundle.min.js"></script>
    <script src="/js/studentPossibleCourseInfo.js"></script>
</body>
</html>