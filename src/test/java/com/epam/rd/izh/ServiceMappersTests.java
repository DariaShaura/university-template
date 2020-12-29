package com.epam.rd.izh;

import com.epam.rd.izh.dto.AuthorizedUserDto;
import com.epam.rd.izh.entity.AuthorizedUser;
import com.epam.rd.izh.service.UserServiceMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Test
    public void filesCopyTest()
            throws IOException{
        String realPathtoUploads = "C:\\Workspace\\final-project-template\\src\\main\\webapp\\uploads\\IIIvan";

        File directory = new File(realPathtoUploads + "\\9\\10");
        if (! directory.exists()){
            directory.mkdirs();
            // If you require it to make the entire directory path including parents,
            // use directory.mkdirs(); here instead.
        }


        Path copied = Paths.get(realPathtoUploads + "\\9\\10\\lecture04-linclass.pdf");
        Path originalPath = Paths.get(realPathtoUploads + "\\tempCourse\\lecture04-linclass.pdf");
        Files.copy(originalPath, copied, StandardCopyOption.REPLACE_EXISTING);


        assertTrue(Files.exists(copied));
        assertTrue(Files.readAllLines(originalPath)
                .equals(Files.readAllLines(copied)));
    }
}
