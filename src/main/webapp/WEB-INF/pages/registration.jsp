<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
	<meta name="author" content="Daria Shaura">
	<title>Registration form</title>
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css" integrity="sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2" crossorigin="anonymous">
	<link rel="stylesheet" href="css/login.css">
</head>
<body class="text-center">
        <form:form modelAttribute="registrationForm" method="POST" action="registration/proceed" class="form-signin" validate="true">
              <nav class="nav nav-pills flex-column flex-sm-row">
                <a class="flex-sm-fill text-sm-center nav-link" href="login">Вход</a>
                <a class="flex-sm-fill text-sm-center nav-link active" href="">Регистрация</a>
              </nav>
          <form:input type="text" path="login" id="login" class="form-control" placeholder="Логин" required="" autofocus="" minlength="5"/>
          <form:input type="password" path="password" class="form-control" placeholder="Пароль" required=""/>
          <form:input type="text" path="firstName" class="form-control" placeholder="Имя" required=""/>
          <form:input type="text" path="secondName" class="form-control" placeholder="Отчество" required=""/>
          <form:input type="text" path="lastName" class="form-control" placeholder="Фамилия" required=""/>
          <form:input type="date" path="birthDate" class="form-control" placeholder="Дата рождения" required=""/>
          <form:select path="role" items="${roles}" class="custom-select" required=""/>
          <button class="btn btn-lg btn-primary btn-block" type="submit">Регистрация</button>
        </form:form>
    <!-- jQuery and Bootstrap Bundle (includes Popper) -->
    <script src="js/jquery-3.5.1.js"></script>
    <script src="bootstrap/js/bootstrap.bundle.min.js"></script>
    <script src="js/registration.js"></script>
</body>
</html>
