package com.epam.rd.izh.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.format.DateTimeFormatter;

@Configuration
public class WebConfig {
  /**
   * Данный класс можно использовать для создание бинов приложения, например бин ObjectMapper для десериализации.
   * Этот класс не является обязательным, но является стандартным там, где используется настройка бинов.
   */

    @Bean
  public DateTimeFormatter dateTimeFormatter() {
      return DateTimeFormatter.ofPattern("yyyy-MM-d");
  }

}
