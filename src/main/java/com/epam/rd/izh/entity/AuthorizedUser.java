package com.epam.rd.izh.entity;


import lombok.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Сущность пользователя, содержит данные(credentials), необходимые для авторизации в Spring Web приложении; Может
 * быть использована как часть бизнес логики приложеняи, например сотрудник больницы, где role определяет его
 * полномочия.
 *
 * Рекомендуется добавить поле id в сущнсть пользователя. Поле id может генерироваться базой данных, также можно
 * добавить в код приложения сервис, генерирующий UUID: 'private UUID id = randomUUID();' и проверяющий его на
 * наличие совпадений с уже существующими.
 */

 @Component
 @Builder(toBuilder = true)
 @AllArgsConstructor(access = AccessLevel.PUBLIC)
 @NoArgsConstructor(access = AccessLevel.PUBLIC)
 @Setter(value = AccessLevel.PUBLIC)
 @Getter
public class AuthorizedUser {
  private long id;
  private String login;
  private String password;

  private String firstName;
  private String secondName;
  private String lastName;
  private LocalDate birthDate;

  private String role;

}
