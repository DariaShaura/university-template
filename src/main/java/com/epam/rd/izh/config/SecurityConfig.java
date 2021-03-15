package com.epam.rd.izh.config;

import com.epam.rd.izh.service.UserDetailsServiceMapper;
import com.epam.rd.izh.service.UserFolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private UserDetailsServiceMapper userDetailsService;

  @Autowired
  UserFolderService userFolderService;


  @Bean(name = "sessionRegistry")
  public SessionRegistry sessionRegistry() {
    return new SessionRegistryImpl();
  }

  /**
   * configure методы определяют настройку Spring Security.
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        /**
         * Список всех энд-пойнтов, требующих особой политики авторизации.
         * /login доступен только неавторизованным пользователям.
         * /registration и входящие в него энд-пойнты доступны всем пользователям.
         * Сюда можно добавить свои энд-пойнты, спецефические для выбранного проекта.
         */
        .authorizeRequests()
        .antMatchers("/login").anonymous()
        .antMatchers("/registration").permitAll()
        .antMatchers("/registration/**").permitAll()
        .antMatchers("/mainTeacher").hasAuthority("TEACHER")
        .antMatchers("/mainStudent").hasAuthority("STUDENT")
        .antMatchers("/mainAdmin").hasAuthority("ADMIN")
        /**
         * Открытие доступа к ресурсным пакетам:
         * /webapp/css
         * /webapp/js
         * /webapp/images
         * /webapp/fonts
         */
        .antMatchers("/css/**").permitAll()
        .antMatchers("/js/**").permitAll()
        .antMatchers("/images/**").permitAll()
        .antMatchers("/fonts/**").permitAll()

        /**
         * Любой реквест, кроме перечисленных выше, доступен лишь авторизованному пользователю.
         * Неавторизованный пользователь будет переброшен на "/login".
         */
        .anyRequest().authenticated()

        /**
         * отключение настройки csrf.
         */
        .and()
        .csrf().disable()

        /**
         * Настройка логики страницы логина.
         * Обратить внимание на переход страницы в случае успешной авторизации.
         */
        .formLogin()
        .loginPage("/login")
            .defaultSuccessUrl("/main", true)
            .permitAll()
        .failureUrl("/login?error=error")
        .usernameParameter("login")
        .passwordParameter("password")

        /**
         * Включение функции выхода из текущей сессии.
         */
        .and()
        .logout()
            .logoutSuccessHandler(new SimpleUrlLogoutSuccessHandler() {

              @Override
              public void onLogoutSuccess(HttpServletRequest request,
                                          HttpServletResponse response, Authentication authentication)
                      throws IOException, ServletException {

                String login = authentication.getName();

                userFolderService.deleteUserDir(userFolderService.getUserDirFile(login+"\\tempCourse"));

                super.onLogoutSuccess(request, response, authentication);
              }
            })
        //.logoutSuccessUrl("/login")
        .and()
        .sessionManagement()
        .maximumSessions(1)
        .sessionRegistry(sessionRegistry());
  }

  @Override
  protected void configure(AuthenticationManagerBuilder authentication) {
    authentication.authenticationProvider(authProvider());
  }

  /**
   * Класс, обеспечивающий механизм авторизации. Принимает в себя реализацию сервиса авторизации UserDetailsService
   * и механизм шифрования пароля (реализацию PasswordEncoder).
   * Итоговый бин DaoAuthenticationProvider добавляется в контекст приложения и обеспечивает основную
   * логику Spring Security.
   */
  @Bean
  public DaoAuthenticationProvider authProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }

  /**
   * Механизм шифрования пароля, реализующий интерфейс PasswordEncoder. В данном примере использован
   * BCryptPasswordEncoder, можно написать свою реализацию, создав собственный класс шифрования.
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}
