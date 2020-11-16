package com.epam.rd.izh.entity;

//import sratic javax.persistance.GenerationType.IDENTITY;

/**
 * Сущность пользователя, содержит данные(credentials), необходимые для авторизации в Spring Web приложении; Может
 * быть использована как часть бизнес логики приложеняи, например сотрудник больницы, где role определяет его
 * полномочия.
 *
 * Рекомендуется добавить поле id в сущнсть пользователя. Поле id может генерироваться базой данных, также можно
 * добавить в код приложения сервис, генерирующий UUID: 'private UUID id = randomUUID();' и проверяющий его на
 * наличие совпадений с уже существующими.
 */
//@Entity
//@Table(name="users")
public class AuthorizedUser {

  private String login;
  private String password;

  /**
   * Определяет GrantedAuthority пользователя. Может быть колелкцией, например Set<Strings> если логика приложения
   * подразумевает множество ролей и их комбинацию в пределах одного пользователя.
   * Хорошим решением будет хранить роль в таблице БД, также допускается использовать ENUM класс в пакете utils.
   * Если роль для бизнес-логики не важна, можно задать для всех объектов 'private String role = "User"'.
   */

  private String role;

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }



  /**
   * Ниже представлена простая реализация паттерна builder;
   * Паттерн builder рекомендуется использовать в DTO или часто создаваемых Entity объектах для лаконичности кода;
   * Реализация паттерна builder может быть легко добавлена при помощи библиотеки Lombok.
   */

  public AuthorizedUser login(String login) {
    this.setLogin(login);
    return this;
  }

  public AuthorizedUser password(String password) {
    this.setPassword(password);
    return this;
  }

  public AuthorizedUser role(String role) {
    this.setRole(role);
    return this;
  }

}
