<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
	<meta name="author" content="Daria Shaura">
	<title>Курc:</title>
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css" integrity="sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2" crossorigin="anonymous">
	<link rel="stylesheet" href="/css/studentCourse.css">
  <link rel="icon" href="/images/favicon.ico" type="image/x-icon" />
  <link rel="shortcut icon" href="/images/favicon.ico" type="image/x-icon" />
  <link href="https://unpkg.com/bootstrap-table@1.18.1/dist/bootstrap-table.min.css" rel="stylesheet">
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
          <div class="dropdown-menu" aria-labelledby="navbarDropdown" id="studentCourseList">
            <div class="dropdown-divider" id="afterStudentCourseList"></div>
            <a class="dropdown-item addCourseLink" href="/mainStudent">Добавить курс</a>
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
            <ul class="nav flex-column">
              <li class="nav-item">
                <a class="nav-link" href="#" id="hrefGrade"><i class="fa fa-bars" aria-hidden="true"></i> Оценки/Сдать л/р</a>
              </li>
              <li class="nav-item">
                <a class="nav-link" href="#" id="hrefSchedule"><i class="fa fa-calendar fa-lg" aria-hidden="true"></i> Расписание</a>
              </li>
              <li class="nav-item">
                <hr>
              </li>
              <li class="nav-item">
                <a class="nav-link" href="#" id="hrefDeleteCourse"><i class="fa fa-times" aria-hidden="true"></i> Удалить курс</a>
              </li>
            </ul>
          </div>
        </nav>
    <div class="col-md-8">
        <div class="col p-4 bg-light rounded" id="mainContent">

        </div>
  </div>
  </div>

  <div id="courseDescriptionSection">
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
  </div>

  <div id="courseScheduleTemp">
  <div class="table-responsive">
    <table class="table scheduleTable table-hover">
                    <thead>
                        <tr>
                            <th class="text-center">Название темы</th>
                            <th class="text-center">Начало</th>
                            <th class="text-center">Конец</th>
                            <th class="text-center">Посещение</th>
                        </tr>
                    </thead>
                    <tbody class="scheduleTbody">
                    </tbody>
    </table>
  </div>
  </div>

<div id="courseMarksTemp">
  <div class="table-responsive mb-3">
    <table class="table marksTable table-hover">
                    <thead>
                        <tr>
                            <th scope="col" class="invisible .col- labIndex">ID_lab</th>
                            <th scope="col" class="invisible .col- markIndex">ID_mark</th>
                            <th class="text-center">Лабораторная</th>
                            <th class="text-center">Файл</th>
                            <th class="text-center">Оценка</th>
                        </tr>
                    </thead>
                    <tbody class="marksTbody">
                    </tbody>
    </table>
  </div>
  </div>
    <div class="dropdown-menu dropdown-menu-sm context-menu">
              <a class="dropdown-item" href="#" id="loadLab">Открыть</a>
              <div class="dropdown-divider"></div>
              <a class="dropdown-item" href="#" id="deleteLab">Удалить</a>
    </div>

  <footer class="text-muted">
  <div class="container">
    <p class="float-right">
      <a href="#mainContent">Вверх</a>
    </p>
    <p> © 2020</p>
  </div>
</footer>
    <!-- jQuery and Bootstrap Bundle (includes Popper) -->
    <script src="/js/jquery-3.5.1.js"></script>
    <script src="/bootstrap/js/bootstrap.bundle.min.js"></script>
    <script src="/js/studentCourse.js"></script>
    <script src="https://cdn.datatables.net/1.10.22/js/jquery.dataTables.min.js"></script>
    <script src="https://cdn.datatables.net/1.10.22/js/dataTables.bootstrap4.min.js"></script>
    <script src="https://unpkg.com/bootstrap-table@1.18.1/dist/bootstrap-table.min.js"></script>
</body>
</html>