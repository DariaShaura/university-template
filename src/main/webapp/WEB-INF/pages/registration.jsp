<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
	<meta name="author" content="Daria Shaura">
	<title>Регистрация</title>
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css" integrity="sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2" crossorigin="anonymous">
	<link rel="stylesheet" href="/css/registration.css">
	<link rel="icon" href="/images/favicon.ico" type="image/x-icon" />
    <link rel="shortcut icon" href="/images/favicon.ico" type="image/x-icon" />
</head>
<body>
        <form:form modelAttribute="registrationForm" method="POST" action="/registration/proceed" class="form-registration row g-3">
            <div class="col-12 mb-4">
              <nav class="nav nav-pills flex-column flex-sm-row">
                <a class="flex-sm-fill text-sm-center nav-link" href="login">Вход</a>
                <a class="flex-sm-fill text-sm-center nav-link active" href="">Регистрация</a>
              </nav>
            </div>
            <div class="col-6">
                <label for="login" class="form-label">Логин</label>
                <form:input type="text" path="login" id="login" class="form-control" required="true" autofocus="" minlength="5"/>
                <div class="invalid-feedback" id="login-invalid-feedback"> </div>
            </div>
            <div class="col-6">
                <label class="form-label">Пароль</label>
                <form:input type="password" path="password" class="form-control" required="true"/>
            </div>
          <div class="col-12">
            <label class="form-label">Имя</label>
            <form:input type="text" path="firstName" class="form-control" required="true"/>
          </div>
          <div class="col-12">
            <label class="form-label">Отчество</label>
            <form:input type="text" path="secondName" class="form-control" required="true"/>
          </div>
          <div class="col-12">
            <label class="form-label">Фамилия</label>
            <form:input type="text" path="lastName" class="form-control" required="true"/>
          </div>
          <div class="col-12">
            <label class="form-label">Дата рождения</label>
            <form:input type="date" path="birthDate" id="birthDate" class="form-control" required="true"/>
            <div class="invalid-feedback" id="birthDate-invalid-feedback"></div>
          </div>
          <div class="col-12" >
            <label class="form-label">Роль</label>
            <form:select path="role" items="${roles}" class="custom-select" required="true"/>
          </div>
          <div class="col-12 mt-3" >
            <button class="btn btn-lg btn-primary btn-block" type="submit">Регистрация</button>
          </div>
        </form:form>
    <!-- jQuery and Bootstrap Bundle (includes Popper) -->
    <script src="/js/jquery-3.5.1.js"></script>
    <script src="/bootstrap/js/bootstrap.bundle.min.js"></script>
    <script src="/js/registration.js"></script>
</body>
</html>
