package com.epam.rd.izh;

import com.epam.rd.izh.dto.AuthorizedUserDto;
import com.epam.rd.izh.entity.AuthorizedUser;
import com.epam.rd.izh.service.UserServiceMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ComponentScan(basePackages = "com.epam.rd.izh")
public class ServiceMappersTests {

    @Autowired
    UserServiceMapper userServiceMapper;

    private static AuthorizedUser authorizedUser;

    @BeforeAll
    public static void init() {
        authorizedUser = new AuthorizedUser().builder()
                                        .id(1)
                                        .firstName("Иван")
                                        .secondName("Иванович")
                                        .lastName("Иванов")
                                        .birthDate(LocalDate.of(1980, 6, 5))
                                        .role("TEACHER")
                                        .login("IIIvan")
                                        .password("19801980")
                                        .build();
    }

    @Test
    public void getAuthorizedUserDtoTest() {
        AuthorizedUserDto authorizedUserDto = userServiceMapper.getAuthorizedUserDto(authorizedUser);

        AuthorizedUserDto authorizedUserDtoCheck = new AuthorizedUserDto().builder()
                                                        .firstName("Иван")
                                                        .secondName("Иванович")
                                                        .lastName("Иванов")
                                                        .birthDate("1980-06-5")
                                                        .role("TEACHER")
                                                        .login("IIIvan")
                                                        .password("19801980")
                                                        .build();

        assertEquals(authorizedUserDtoCheck, authorizedUserDto);
    }
}
