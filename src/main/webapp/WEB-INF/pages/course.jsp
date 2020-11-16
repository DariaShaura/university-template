<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
	<meta name="author" content="Daria Shaura">
	<title>Курc:</title>
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css" integrity="sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2" crossorigin="anonymous">
	<link rel="stylesheet" href="css/course.css">
  <link rel="icon" href="images/favicon.ico" type="image/x-icon" />
  <link rel="shortcut icon" href="images/favicon.ico" type="image/x-icon" />
  <script src="https://use.fontawesome.com/450e77e423.js"></script>
</head>
<body>
  <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <i class="fa fa-graduation-cap fa-2x" id="bigCap" aria-hidden="true"></i>

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
            <i class="fa fa-user-circle-o fa-lg" id="idCircle" aria-hidden="true"></i>
            ${login}
          </a>
          <div class="dropdown-menu" aria-labelledby="navbarDropdown">
            <a class="dropdown-item" href="logout">Выход</a>
        </li>
      </ul>
    </div>
  </nav>
  <div class="container-fluid">
    <nav class="navbar navbar-expand-lg navbar-light">
      <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
          <li class="nav-item active">
            <a class="nav-link" href="#">Курсы > <span class="sr-only">(current)</span></a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="#" id="hrefThisCourse">Курс1</a>
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
                <a class="nav-link active" href="#" id="hrefStudents"><i class="fa fa-users fa-lg" aria-hidden="true"></i> Участники</a>
              </li>
              <li class="nav-item">
                <a class="nav-link" href="#" id="hrefGrade"><i class="fa fa-bars" aria-hidden="true"></i> Оценки</a>
              </li>
              <li class="nav-item">
                <a class="nav-link" href="#"><i class="fa fa-calendar fa-lg" aria-hidden="true"></i> Расписание</a>
              </li>
            </ul>

            <h6 class="sidebar-heading d-flex justify-content-between align-items-center px-3 mt-4 mb-1 text-muted">
              <span>Saved reports</span>
              <a class="d-flex align-items-center text-muted" href="#">
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-plus-circle"><circle cx="12" cy="12" r="10"></circle><line x1="12" y1="8" x2="12" y2="16"></line><line x1="8" y1="12" x2="16" y2="12"></line></svg>
              </a>
            </h6>
            <ul class="nav flex-column mb-2">
              <li class="nav-item">
                <a class="nav-link" href="#">
                  <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-file-text"><path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path><polyline points="14 2 14 8 20 8"></polyline><line x1="16" y1="13" x2="8" y2="13"></line><line x1="16" y1="17" x2="8" y2="17"></line><polyline points="10 9 9 9 8 9"></polyline></svg>
                  Current month
                </a>
              </li>
              <li class="nav-item">
                <a class="nav-link" href="#">
                  <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-file-text"><path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path><polyline points="14 2 14 8 20 8"></polyline><line x1="16" y1="13" x2="8" y2="13"></line><line x1="16" y1="17" x2="8" y2="17"></line><polyline points="10 9 9 9 8 9"></polyline></svg>
                  Last quarter
                </a>
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
     <h3 class="pb-1 text-center">Курс1</h3>
           <a href="#" data-toggle="tooltip" title="Some tooltip text!">Hover over me</a>
           <div>
              <h4 class="courseParts">Описание курса</h4>
              <p>
                Курс «<span class="courseTitle">Курс1</span>» нацелен на Формирование представления о специфике базовых и прикладных технологий, их месте среди других наук, роли в развитии информационного общества, изучение практических аспектов разработки средств реализации информационных технологий
              </p>
              <p>
                Курс соответствует Учебному плану «Информационные системы и технологии» (очное, 2017)
              </p>
              <p>
                <span class="hours">Количество часов</span>: 108 ч. (в том числе лекции – 14, практические занятия – 30, самостоятельная работа – 64), форма контроля: дифференцированный зачет.
              </p>
              <p><span class="author">Автор ЭУК</span>: Иванов Иван Иванович, доцент кафедры «Информационные системы», кандидат технических наук</p>
              <hr>
              <h4 class="courseParts">Занятие1</h4>
              <p class="mb-4">
              <span class="classHours">Количество часов</span>:<span class="classHours">7 ч.</span> (в том числе лекции – 2, лабораторные работы – 2)
              </p>
              <ol class="list-unstyled" id="course-themes">
                <li class="mb-3"><a class="lectureTitle" href="#" data-toggle="tooltip" title="Some tooltip text!"><i class="fa fa-book fa-lg" aria-hidden="true"></i> Лекция 1</a></li>
                <li class="mb-3"><a class="lectureTitle" href="#"><i class="fa fa-book fa-lg" aria-hidden="true"></i> Лекция 2</a></li>
                <li class="mb-3"><a class="lectureTitle" href="#"><i class="fa fa-file-pdf-o fa-lg" aria-hidden="true"></i> Презентация</a></li>
                <li class="mb-3"><a class="lectureTitle" href="#"><i class="fa fa-pencil-square-o fa-lg" aria-hidden="true"></i> Лабораторная работа 1</a></li>
                <li class="mb-3"><a class="lectureTitle" href="#"><i class="fa fa-pencil-square-o fa-lg" aria-hidden="true"></i> Лабораторная работа 2</a></li>
              </ol>
              <hr>
           </div>
  </div>

  <div id="tableStudentsSection">
  <div class="table-responsive">
        <table class="table table-striped table-sm">
          <thead>
            <tr>
              <th>ФИО</th>
              <th>Группа</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>Губаев Эдуард Александрович</td>
              <td>ГР-1</td>
            </tr>
            <tr>
              <td>Гаврилова Диана Сергеевна</td>
              <td>ГР-1</td>
            </tr>
            <tr>
              <td>Тепин Данил Владимирович</td>
              <td>ГР-1</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

  <div id="gradeStudentsSection">
  <div class="table-responsive">
        <table class="table table-striped table-sm">
          <thead>
            <tr>
              <th>ФИО</th>
              <th>Итоговая оценка</th>
              <th>Занятие 1</th>
              <th>Занятие 2</th>
              <th>Занятие 3</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>Губаев Эдуард Александрович</td>
              <td contenteditable='true'>3</td>
              <td contenteditable='true'>3</td>
              <td contenteditable='true'>3</td>
              <td contenteditable='true'>4</td>
            </tr>
            <tr>
              <td>Гаврилова Диана Сергеевна</td>
              <td>4</td>
              <td>4</td>
              <td>4</td>
              <td>4</td>
            </tr>
            <tr>
              <td>Тепин Данил Владимирович</td>
              <td>5</td>
              <td>4</td>
              <td>5</td>
              <td>5</td>
            </tr>
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
    <!-- jQuery and Bootstrap Bundle (includes Popper) -->
    <script src="js/jquery-3.5.1.js"></script>
    <script src="bootstrap/js/bootstrap.bundle.min.js"></script>
    <script src="js/course.js"></script>
</body>
</html>