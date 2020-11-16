package com.epam.rd.izh;

import com.epam.rd.izh.entity.AuthorizedUser;
import com.epam.rd.izh.repository.RoleRepository;
import com.epam.rd.izh.repository.UserRepository;
import com.epam.rd.izh.service.AuthorizedUserMapper;
import com.epam.rd.izh.service.RoleService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ComponentScan(basePackages = "com.epam.rd.izh")
public class WorkWithDBTests {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserRepository userRepository;

    private static AuthorizedUser authorizedUser;

    @BeforeAll
    public static void init()
    {
        authorizedUser = new AuthorizedUser().builder()
                .id(1)
                .firstName("test")
                .secondName("test")
                .lastName("test")
                .birthDate(LocalDate.of(1980, 6, 5))
                .role("TEACHER")
                .login("testAdd")
                .password("19801980")
                .build();
    }

    @Test
    @DisplayName("Бин DataSource успешно создан")
    public void dataSourceTest()
            throws SQLException {
        assertDoesNotThrow(() -> dataSource.getConnection().createStatement().execute("SELECT * FROM role"), "При доступе к БД произошла ошибка");
    }

    @Test
    @DisplayName("Тест метода - получение Map<String, String> ролей")
    void getRolesTest() {
        Map<String, String> rolesMap = roleRepository.getRoles();

        assertTrue(rolesMap.containsKey("СТУДЕНТ"));
    }

    @Test
    @DisplayName("Тест метода - getRolesTitles() - получение названия ролей на русском")
    void getRolesTitlesTest() {
        assertTrue(roleService.getRolesTitles().size() > 0);
    }

    @ParameterizedTest
    @ValueSource(strings = {"IIIvan"})
    @DisplayName("Тест метода - getAuthorizedUserByLogin()")
    void getAuthorizedUserByLoginTest(String login) {
        assertNotNull( userRepository.getUserByLogin(login));
    }

    @Test
    @DisplayName("Тест метода - addUser")
    void addUserTest()
    {
        assertTrue(userRepository.addUser(authorizedUser));
    }
}
