package com.epam.rd.izh.controller;

import com.epam.rd.izh.entity.AuthorizedUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.epam.rd.izh.exception.UserAlreadyRegisteredException;
import com.epam.rd.izh.exception.UsersAgeCorrectnessException;
import com.epam.rd.izh.repository.RoleRepository;
import com.epam.rd.izh.service.RoleService;
import com.epam.rd.izh.service.UserFolderService;
import com.epam.rd.izh.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * В аргументы контроллеров, которые обрабатывают запросы, можно указать дополнительные входные параметры: Например:
 * HttpServletRequest и HttpServletResponse. Данные объекты автоматически заполняться данными о реквесте и респонсе. Эти
 * данные можно использовать, например достать куки, сессию, хедеры итд.
 */

@Controller
public class AuthenticationController {

  //@Autowired
  //@Qualifier("userValidator") // spring validator
  //private Validator userValidator;


  @Autowired
  private UserService userService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private RoleService roleService;

  @Autowired
  UserFolderService userFolderService;

  @Autowired
  UserValidator userValidator;


  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.setValidator(userValidator);
  }

  /**
   * Метод, отвечающий за логику авторизации пользователя.
   * /login - определяет URL, по которому пользователь должен перейти, чтобы запустить данный метод-обработчик.
   */
  @GetMapping("/login")
  public String login(Model model, @RequestParam(required = false) String error) {
    model.addAttribute("error_login_placeholder", "");

    if (error != null) {
      /**
       * Model представляет из себя Map коллекцию ключ-значения, распознаваемую View элементами MVC.
       * Добавляется String "invalid login or password!", с ключем "error_login_placeholder".
       * При создании View шаблона плейсхолдер ${error_login_placeholder} будет заменен на переданное значение.
       *
       * В класс Model можно передавать любые объекты, необходимые для генерации View.
       */
      model.addAttribute("error_login_placeholder", "invalid login or password!");
    }
    /**
     * Контроллер возвращает String название JSP страницы.
     * В application.properties есть следующие строки:
     * spring.mvc.view.prefix=/WEB-INF/pages/
     * spring.mvc.view.suffix=.jsp
     * Spring MVC, используя суффикс и префикс, создаст итоговый путь к JSP: /WEB-INF/pages/login.jsp
     */
    return "login";
  }

  /**
   * Метод, отвечающий за логику регистрации пользователя.
   */
  @GetMapping("/registration")
  public String viewRegistration(Model model) {
    if(!model.containsAttribute("registrationForm")){
      model.addAttribute("registrationForm", new AuthorizedUser());
    }
    return "registration";
  }

  @PostMapping(value ="/registration/availability", produces = MediaType.APPLICATION_JSON_VALUE)
  public @ResponseBody ResponseEntity<?> loginAvailability(HttpServletRequest request) {

    boolean isAvailable = true;

    if(request.getParameter("login") != null) {
      try {
        isAvailable = userService.isLoginAvailable(request.getParameter("login"));
      }
      catch (UserAlreadyRegisteredException ex){
        isAvailable = false;
      }
    }
    else if(request.getParameter("birthDate") != null){
      try{
        isAvailable = userService.isBirthDateCorrect(request.getParameter("birthDate"));
      }
      catch (UsersAgeCorrectnessException ex){
        isAvailable = false;
      }
    }

    if(isAvailable){
      return new ResponseEntity<>(
              true,
              HttpStatus.OK);
    }
    else{
      return new ResponseEntity<>(
              false,
              HttpStatus.BAD_REQUEST);
    }
  }


  /**
   * Метод, отвечающий за подтверждение регистрации пользователя и сохранение данных в репозиторий или DAO.
   */
  @PostMapping("/registration/proceed")
  public String processRegistration(@Valid @ModelAttribute("registrationForm") AuthorizedUser registeredUser,
      BindingResult bindingResult, RedirectAttributes redirectAttributes) {

    /**
     * Здесь по желанию можно добавить валидацию введенных данных на back-end слое.
     * Для этого необходимо написать реализацию Validator.
     */
    //registeredUser.validate(registeredUserDto, bindingResult);

    if (bindingResult.hasErrors()) {
      //логика отображения ошибки, не является обязательной
      //...
      //...
      return "redirect:/registration";
    }
    /**
     * Здесь происходит присвоение роли пользователю и шифрование пароля.
     * Роль может быть так же определена пользователем на этапе регистрации, либо иным способов, зависящим
     * от темы финального проекта.
     * registeredUser может быть DTO объектом, преобразуемым в AuthorizedUser сущность в сервисе-маппере
     * (эот сервис нужно написать самим), вместе с присвоением роли и шифрованием пароля.
     */
    registeredUser.setRole("User");
    registeredUser.setPassword(passwordEncoder.encode(registeredUser.getPassword()));

    /**
     * Добавление пользователя в репозиторий или в базу данных через CRUD операции DAO.
     * Рекомендуется вынести эту логику на сервисный слой.
     */
    userRepository.addAuthorizedUser(registeredUser);
    /**
     * В случае успешной регистрации редирект можно настроить на другой энд пойнт.
     */
    return "redirect:/login";
  }

}