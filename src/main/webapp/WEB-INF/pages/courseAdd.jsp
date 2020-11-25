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

    <link rel="stylesheet" href="/css/courseAdd.css"/>
    <link rel="icon" href="images/favicon.ico" type="image/x-icon" />
  <link rel="shortcut icon" href="images/favicon.ico" type="image/x-icon" />
  <script src="https://use.fontawesome.com/450e77e423.js"></script>
</head>
<body>
  <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <i class="fa fa-graduation-cap fa-2x" aria-hidden="true"></i>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
      <ul class="navbar-nav mr-auto">
        <li class="nav-item active">
          <a class="nav-link" href="#">Курсы <span class="sr-only">(current)</span></a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="#">Link</a>
        </li>
        <li class="nav-item dropdown">
          <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
            Курсы
          </a>
          <div class="dropdown-menu" aria-labelledby="navbarDropdown">
            <a class="dropdown-item" href="#">Курс1</a>
            <a class="dropdown-item" href="#">Курс2</a>
            <div class="dropdown-divider"></div>
            <a class="dropdown-item" href="#">Создать курс</a>
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
            <a class="dropdown-item" href="logout">Выход</a>
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
        <div class="form-group row">
    		<div class="col-auto">
    			<h4>Темы</h4>
    		</div>
    		<div class="col-auto">
      			<button type="button" class="btn btn-secondary" data-toggle="modal" data-target="#exampleModal">Добавить</button>
    		</div>
  		</div>
  		<div class="form-group mb-5" id="tableThemes">
         </div>
        <button type="submit" class="btn btn-primary">Добавить курс</button>
        </form>
    </div>
  </div>

<%-- MODAL FORM --%>
<div class="modal fade" id="exampleModal"  data-backdrop="static" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="exampleModalLabel">Тема</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
        <form id="classAddForm">
        	<div class="form-group">
            	<label for="themeTitle">Название</label>
            	<input type="text" class="form-control" id="themeTitle" placeholder="Введите название темы" required="">
        	</div>
        	<div class="form-group">
        	     <h6>Материал к теме</h6>
        	</div>
        	<div class="form-group">
        	    <select class="custom-select" id="selectType">
                                        <option value="Лекция">Лекция</option>
                                        <option value="Лабораторная">Лабораторная</option>
                </select>
        	</div>
        	<div class="input-group mb-3">
  				<div class="custom-file">
    				<input type="file" class="custom-file-input" id="inputGroupFile01" aria-describedby="inputGroupFileAddon01">
    				<label class="custom-file-label" for="inputGroupFile01">Выбрать файл</label>
  				</div>
			</div>
			<div class="form-group">
                 <input type="text" class="form-control" id="description" placeholder="Описание материала">
            </div>
    	</form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-dismiss="modal">Отмена</button>
        <button type="button" class="btn btn-primary" id="addClassInCourse">Добавить</button>
      </div>
    </div>
  </div>
</div>
<!--end modal-->
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
    <script src="/js/courseAdd.js"></script>
</body>
</html>