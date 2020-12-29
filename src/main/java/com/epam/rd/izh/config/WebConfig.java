package com.epam.rd.izh.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
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

    @Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.ofMegabytes(2));
        factory.setMaxRequestSize(DataSize.ofMegabytes(2));
        return factory.createMultipartConfig();
    }

}
