package com.epam.rd.izh.repository;

import com.epam.rd.izh.entity.AuthorizedUser;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.epam.rd.izh.service.AuthorizedUserMapper;
import com.epam.rd.izh.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Данный репозиторий хранит список зарегистрированных пользователей;
 * На данный момент он представляет из себя коллекцию List<AuthorizedUser> и методы доступа к ней;
 *
 * Необходимо превратить данный репозиторий в DAO класс:
 * Создать базу данных, подключить ее к приложению, сделать CRUD операции (или их часть) для доступа
 * к хранящимся сущностям.
 * Создать другие DAO классы для хранения бизнес-сущностей выбранной темы финального проекта в этом же пакете.
 */

@Repository
public class UserRepository {

  private final List<AuthorizedUser> users = new ArrayList<>();

  @Autowired
  JdbcTemplate jdbcTemplate;

  @Autowired
  AuthorizedUserMapper authorizedUserMapper;

  @Autowired
  RoleService roleService;

  /**
   * В данном методе использована библиотека Stream API:
   * .filter проверяет каждый элемент коллекции на удовлетворение условия .equals(login), в случае, если совпадающий
   * элемент будет найдет, он будет возвращен методом .findFirst(). Если в коллекции не будет найдет удовлетворяющий
   * условию элемент, методом .orElse(null) будет возвращен null.
   * Допускается использовать вместо  Stream API стандартные циклы For и While.
   *
   * аннотации @Nullable и @Nonnull расставляются над возвращающими не примитивные значения методами и передаваемыми
   * в метод аргументами.
   */

  @Nullable
  public AuthorizedUser getUserByLogin(@Nonnull String login) {
    String query_getAuthorizedUserByLogin = "SELECT user.id, user.login, user.password, user.firstName, user.secondName, user.lastName, " +
            "user.birthDate, role.role FROM user " +
            "left join role " +
            "on user.id_role = role.id " +
            "where user.login = ?";

    AuthorizedUser authorizedUser = jdbcTemplate.queryForObject(query_getAuthorizedUserByLogin, new Object[]{ login }, authorizedUserMapper);

    return authorizedUser;
  }

  public boolean addUser(@Nullable AuthorizedUser user) {

    if (user != null) {
      int roleId = roleService.getRoleId(user.getRole());

      String query_insertUser = "insert into user (firstName, secondName, lastName, birthDate, id_role, login, password) "+"" +
              "                 VALUES (?, ?, ?, ?, ?, ?, ?)";
      return jdbcTemplate.update(
              query_insertUser,
              user.getFirstName(), user.getSecondName(), user.getLastName(),
                    user.getBirthDate(), roleId, user.getLogin(), user.getPassword()
      ) > 0;
    }
    return false;
  }

}
