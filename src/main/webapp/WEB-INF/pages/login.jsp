<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
	<meta name="author" content="Daria Shaura">
	<title>Вход</title>
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css" integrity="sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2" crossorigin="anonymous">
	<link rel="stylesheet" href="/css/login.css">
	 <link rel="icon" href="/images/favicon.ico" type="image/x-icon" />
      <link rel="shortcut icon" href="/images/favicon.ico" type="image/x-icon" />
</head>
<body class="text-center">
 <p id="error">
    ${error_login_placeholder}
 </p>
  <form class="form-signin" action="/login" method="post">
    <nav class="nav nav-pills flex-column flex-sm-row">
      <a class="flex-sm-fill text-sm-center nav-link active" href="">Вход</a>
      <a class="flex-sm-fill text-sm-center nav-link" id="toRegistration" href="/registration">Регистрация</a>
    </nav>
    <label for="inputLogin" class="sr-only">Login</label>
    <input type="text" id="inputLogin" class="form-control" name="login" placeholder="Логин" required="true" autofocus="" minlength="5"/>
    <label for="inputPassword" class="sr-only">Password</label>
    <input type="password" id="inputPassword" class="form-control" name="password" placeholder="Пароль" required="true"/>
    <button class="btn btn-lg btn-primary btn-block" type="submit">Войти</button>
  </form>
   <script src="/js/jquery-3.5.1.js"></script>
   <script src="/bootstrap/js/bootstrap.bundle.min.js"></script>
   <script src="/js/login.js"></script>
</body>
</html>
