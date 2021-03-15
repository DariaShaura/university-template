<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="author" content="Daria Shaura">
    <title>Создание курса</title>
  <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@300;400;500;700&display=swap" rel="stylesheet"/>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css" integrity="sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2" crossorigin="anonymous"/>

    <link rel="stylesheet" href="/css/teacherCourseAdd.css"/>
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
      <h1 class="display-4">Добавить курс</h1>
    </div>
  </div>
 <div class="album py-5 bg-light">
    <div class="container">
        <form id="courseAddForm">
        <div class="form-group">
            <label for="courseTitle">Название курса</label>
            <input type="text" path="title" class="form-control" id="courseTitle" placeholder="Введите название курса" required=""/>
        </div>
        <div class="form-group">
            <label for="courseDescription">Описание курса</label>
             <textarea path="description" class="form-control" id="courseDescription" rows="5"></textarea>
        </div>
        <div class="form-group">
            <input path="hours" type="number" class="form-control" id="hours" placeholder="Количество часов" required=""/>
        </div>
        <%--
        <div class="form-group row">
    		<div class="col-auto">
    			<h4>Темы</h4>
    		</div>
    		<div class="col-auto">
      			<button type="button" class="btn btn-secondary" data-toggle="modal" data-target="#exampleModal">Добавить</button>
    		</div>
  		</div>
  		<div class="form-group mb-5" id="tableThemes">
         </div>--%>
         <div class="form-group row">
              <div class="col-auto">
                <h4>Темы</h4>
              </div>

              <div class="input-group mb-3">
                     <select class="form-control" id="editThemeList">
                            <option id="choose">Выберите тему</option>
                     </select>
                    <div class="input-group-append">
                        <button class="btn btn-outline-secondary" type="button" id="editCourseAddTheme">Добавить тему</button>
                    </div>
              </div>
         </div>
         <div class="form-group mb-5" id="tableThemes">
         </div>
        <button type="submit" class="btn btn-primary" id="addCourse">Добавить курс</button>
        </form>
    </div>
  </div>

    <div class="editTheme">
      <div class="input-group mb-3">
          <input type="text" class="form-control editThemeTitle">
          <div class="input-group-append">
            <button class="btn btn-outline-danger buttonDeleteTheme" type="button">Удалить тему</button>
          </div>
      </div>
    </div>

         <div class="materials">
            <div class="form-row">
                <div class="col text-right">
                    <a href="#" id="editCourseAddMaterial"><i class="fa fa-plus-square-o fa-2x" aria-hidden="true"></i></a>
                </div>
            </div>
            <div class="form-row">
                <table class="table text-center" id="editMaterialsTable">
                    <thead>
                        <tr>
                            <th class="invisible materialIndex">ID</th>
                            <th class="text-center">Описание</th>
                            <th class="text-center">Вид</th>
                            <th class="text-center">Файл</th>
                            <th class="text-center">Удалить</th>
                        </tr>
                    </thead>
                    <tbody class="editMaterialsTbody">
                    </tbody>
                </table>
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
    <!-- jQuery and Botstrap Bundle (includes Popper) -->

    <script src="/js/jquery-3.5.1.js"></script>
    <script src="/bootstrap/js/bootstrap.bundle.min.js"></script>
    <script src="/js/teacherCourseAdd.js"></script>
</body>
</html>