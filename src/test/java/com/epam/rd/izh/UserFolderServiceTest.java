package com.epam.rd.izh;

import com.epam.rd.izh.service.UserFolderService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ComponentScan(basePackages = "com.epam.rd.izh")
public class UserFolderServiceTest {
    @Autowired
    UserFolderService userFolderService;

    @Test
    void getUserDirTest(){
        String loginDir = userFolderService.getUserDir("tempLogin");
        File userCourse1 = new File(loginDir+"\\1\\1");
        userCourse1.mkdirs();
        File userCourse2 = new File(loginDir+"\\2\\1");
        userCourse2.mkdirs();
    }

    @Test
    @DisplayName("Тест метода - deleteUserDir()")
    void deleteUserDirTest(){
        File userDir = userFolderService.getUserDirFile("tempLogin");

        userFolderService.deleteUserDir(userDir);

        assertFalse(userFolderService.getUserDirFile("tempLogin").exists());
    }

    @Test
    @DisplayName("Тест метода - copyMaterialToUserDir")
    void copyMaterialToUserDirTest() throws IOException {
        userFolderService.copyMaterialToUserDir(userFolderService.getUserDir("tempLogin\\1\\1"), userFolderService.getUserDir("tempLogin\\2\\1"), "sample.pdf");

        assertTrue(new File(userFolderService.getUserDir("tempLogin\\2\\1\\sample.pdf")).exists());

    }
}
