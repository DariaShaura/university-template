<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="rus">
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
	<meta name="author" content="Daria Shaura">
	<title>Главная страница</title>
  <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@300;400;500;700&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css" integrity="sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2" crossorigin="anonymous">
	<link rel="stylesheet" href="/css/mainTeacher.css">
    <link rel="icon" href="images/favicon.ico" type="image/x-icon" />
  <link rel="shortcut icon" href="images/favicon.ico" type="image/x-icon" />
  <script src="https://use.fontawesome.com/450e77e423.js"></script>
</head>
<body>
  <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <a href="#" id="toMainPage"><i class="fa fa-graduation-cap fa-2x" aria-hidden="true"></i></a>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
      <ul class="navbar-nav mr-auto">
        <li class="nav-item dropdown">
          <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
            Курсы
          </a>
          <div class="dropdown-menu" aria-labelledby="navbarDropdown">
            <div class="dropdown-divider" id="afterTeacherCourseList"></div>
            <a class="dropdown-item" href="/mainTeacher/courseAdd">Создать курс</a>
          </div>
        </li>
      </ul>
      <ul class="navbar-nav justify-content-end">
         <li class="nav-item dropdown">
          <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
            <i class="fa fa-user-circle-o fa-lg" aria-hidden="true"></i>
            ${login}
          </a>
          <div class="dropdown-menu" aria-labelledby="navbarDropdown">
            <a class="dropdown-item" href="/logout">Выход</a>
        </li>
      </ul>
    </div>
  </nav>
  <div class="jumbotron">
    <div class="container">
      <h1 class="display-3">Курсы</h1>
      <p><a class="btn btn-primary btn-lg" href="mainTeacher/courseAdd" role="button">Добавить курс »</a></p>
    </div>
  </div>
 <div class="album py-5 bg-light">
    <div class="container">
      <div class="row" id="coursesContainer">
      <%--
        <div class="col-md-4">
          <div class="card mb-4 shadow-sm">
              <img src="images/image1.jpg">
            <div class="card-body">
              <a class="stretched-link" href="mainTeacher/course" id="course1">Курс1</a>
            </div>
          </div>
        </div>
        <div class="col-md-4">
          <div class="card mb-4 shadow-sm">
            <img src="images/image2.jpg">
            <div class="card-body">
              <a class="stretched-link" href="mainTeacher/course" id="course2">Курс2</a>
            </div>
          </div>
        </div>
        <div class="col-md-4">
          <div class="card mb-4 shadow-sm">
            <img src="images/image3.jpg">
            <div class="card-body">
              <a class="stretched-link" href="mainTeacher/course" id="course3">Курс3</a>
            </div>
          </div>
        </div>--%>
      </div>
    </div>
  </div>
  <div id="hiddenCourse">
            <div class="col-md-4 courseLink">
              <div class="card mb-4 shadow-sm">
                  <img class="courseImage" src="">
                <div class="card-body">
                  <a class="stretched-link courseHref" href="#" id=""></a>
                </div>
              </div>
            </div>
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
    <script src="/js/mainTeacher.js"></script>
</body>
</html>