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
	<link rel="stylesheet" href="/css/mainAdmin.css">
    <link rel="icon" href="images/favicon.ico" type="image/x-icon" />
  <link rel="shortcut icon" href="images/favicon.ico" type="image/x-icon" />
  <script src="https://use.fontawesome.com/450e77e423.js"></script>
</head>
<body>
  <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <a href="#" id="toMainPage"><i class="fa fa-graduation-cap fa-2x" aria-hidden="true"></i></a>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
      <ul class="navbar-nav mr-auto">

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
      <h1 class="display-3"></h1>
    </div>
  </div>
 <div class="album py-5 bg-light">
    <div class="container" id="mainContainer">
        <div id="courseStudentsTemp">
            <div class="table-responsive mb-3">
              <table  class="table table-hover" id="usersTable">
                              <thead>
                                  <tr>
                                      <th scope="col" class="invisible .col- studentIndex">ID_user</th>
                                      <th class="text-center">Логин</th>
                                      <th class="text-center">Роль</th>
                                      <th class="text-center">В сети</th>
                                      <th class="text-center">Фамилия</th>
                                      <th class="text-center">Имя</th>
                                      <th class="text-center">Отчество</th>
                                      <th class="text-center">Дата рождения</th>
                                      <th class="text-center">Удалить</th>
                                  </tr>
                              </thead>
                              <tbody class="studentsTbody">
                              </tbody>
              </table>
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
    <script src="/js/mainAdmin.js"></script>
</body>
</html>