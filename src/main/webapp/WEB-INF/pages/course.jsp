<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
	<meta name="author" content="Daria Shaura">
	<title>Курc:</title>
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css" integrity="sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2" crossorigin="anonymous">
	<link rel="stylesheet" href="/css/course.css">
  <link rel="icon" href="/images/favicon.ico" type="image/x-icon" />
  <link rel="shortcut icon" href="/images/favicon.ico" type="image/x-icon" />
  <link href="https://unpkg.com/bootstrap-table@1.18.1/dist/bootstrap-table.min.css" rel="stylesheet">
  <script src="https://use.fontawesome.com/450e77e423.js"></script>
</head>
<body>
  <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <i class="fa fa-graduation-cap fa-2x" id="bigCap" aria-hidden="true"></i>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
      <ul class="navbar-nav mr-auto">
        <li class="nav-item active">
          <a class="nav-link" href="/mainTeacher">Курсы <span class="sr-only">(current)</span></a>
        </li>
        <li class="nav-item">
          <%--<a class="nav-link" href="#">Link</a>--%>
        </li>
        <li class="nav-item dropdown">
          <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
            Курсы
          </a>
          <div class="dropdown-menu" aria-labelledby="navbarDropdown" id="teachersCourseList">
            <div class="dropdown-divider" id="afterTeacherCourseList"></div>
            <a class="dropdown-item" href="/mainTeacher/courseAdd">Создать курс</a>
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
            <a class="nav-link" href="/mainTeacher">Курсы > <span class="sr-only">(current)</span></a>
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
                <a class="nav-link active" href="#" id="hrefStudents"><i class="fa fa-users fa-lg" aria-hidden="true"></i> Участники</a>
              </li>
              <li class="nav-item">
                <a class="nav-link" href="#" id="hrefGrade"><i class="fa fa-bars" aria-hidden="true"></i> Оценки</a>
              </li>
              <li class="nav-item">
                <a class="nav-link" href="#" id="hrefSchedule"><i class="fa fa-calendar fa-lg" aria-hidden="true"></i> Расписание</a>
              </li>
              <li class="nav-item">
                <hr>
              </li>
              <li class="nav-item">
                <a class="nav-link" href="#" id="hrefEditCourse"><i class="fa fa-pencil-square-o fa-lg" aria-hidden="true"></i> Редактировать курс</a>
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
                            <th scope="col" class="invisible .col- scheduleIndex">ID_schedule</th>
                            <th scope="col" class="invisible .col- themeIndex">ID_course</th>
                            <th class="text-center">Название темы</th>
                            <th class="text-center">Начало</th>
                            <th class="text-center">Конец</th>
                        </tr>
                    </thead>
                    <tbody class="scheduleTbody">
                    </tbody>
    </table>
  </div>
  <button type="button" class="btn btn-primary scheduleUpdate">Внести изменения в расписание</button>
  </div>

  <div id="courseStudentsTemp">
    <div class="table-responsive mb-3">
      <table  class="table studentsTable table-hover">
                      <thead>
                          <tr>
                              <th scope="col" class="invisible .col- studentIndex">ID_student</th>
                              <th class="text-center">Фамилия</th>
                              <th class="text-center">Имя</th>
                              <th class="text-center">Отчество</th>
                              <th class="text-center">Дата рождения</th>
                          </tr>
                      </thead>
                      <tbody class="studentsTbody">
                      </tbody>
      </table>
    </div>
    <button type="button" class="btn btn-primary studentsAttendenceUpdate">Внести изменения в посещаемость</button>
    </div>

<div id="courseMarksTemp">
  <div class="table-responsive mb-3">
    <table class="table marksTable table-hover">
                    <thead>
                        <tr>
                            <th scope="col" class="invisible .col- markId">ID_mark</th>
                            <th scope="col" class="invisible .col- labId">ID_lab</th>
                            <th scope="col" class="invisible .col- studentId">ID_course</th>
                            <th class="text-center">Фамилия</th>
                            <th class="text-center">Имя</th>
                            <th class="text-center">Лабораторная</th>
                            <th class="text-center">Файл</th>
                            <th class="text-center">Оценка</th>
                        </tr>
                    </thead>
                    <tbody class="marksTbody">
                    </tbody>
    </table>
  </div>
  <button type="button" class="btn btn-primary marksUpdate">Внести изменения в оценки</button>
  </div>

    <div id="editCourseForm">
    <h3 class="pb-1 text-center">Редактирование курса</h3>
            <form id="courseEditForm">
            <div class="form-group">
                <label for="courseTitle">Название курса</label>
                <input type="text" class="form-control" id="editCourseTitle" required="">
            </div>
            <div class="form-group">
                <label for="courseDescription">Описание курса</label>
                 <textarea class="form-control" id="editCourseDescription" rows="5" required=""></textarea>
            </div>
            <div class="form-group">
                <label for="hours">Количество часов</label>
                <input type="number" class="form-control" id="editCoursehours" required="" max="500">
            </div>
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
            <button class="btn btn-primary" id="updateCourse">Обновить курс</button>
            </form>
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
          <%--<div class="form-row">
              <div class="form-group col-md-8 ">
                    <label for="inputTitle">Описание</label>
                    <input type="text" class="form-control editMaterialTitle">
              </div>
              <div class="form-group col-md-4">
                <label for="editMaterialType">Вид материала</label>
                <select class="form-control editMaterialType">
                  <option value="Лекция">Лекция</option>
                  <option value="Лабораторная">Лабораторная</option>
                </select>
              </div>
          </div>
          <div class="form-row">
                <div class="input-group mb-3">
                  <div class="custom-file">
                    <input type="file" class="custom-file-input editMaterialPath" aria-describedby="inputGroupFileAddon01">
                    <label class="custom-file-label">Загрузить другой файл</label>
                  </div>
                </div>
          </div>
          <div class="form-group row">
            <button class="btn btn-primary">Удалить материал</button>
          </div>--%>

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
    <script src="/js/course.js"></script>
    <script src="https://cdn.datatables.net/1.10.22/js/jquery.dataTables.min.js"></script>
    <script src="https://cdn.datatables.net/1.10.22/js/dataTables.bootstrap4.min.js"></script>
    <script src="https://unpkg.com/bootstrap-table@1.18.1/dist/bootstrap-table.min.js"></script>
</body>
</html>