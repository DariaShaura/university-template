package com.epam.rd.izh;

import com.epam.rd.izh.service.UserFolderService;
import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ComponentScan(basePackages = "com.epam.rd.izh")
public class UserFolderServiceTest {
    @Autowired
    UserFolderService userFolderService;

    @Autowired
    private HttpServletRequest request;

    @Before
    void getUserDirTest() throws IOException{
        String loginDir = request.getServletContext().getRealPath("uploads\\testLogin");
        File userCourse1 = new File(loginDir+"\\1\\1");
        userCourse1.mkdirs();

        PDDocument testFile = new PDDocument();
        testFile.save(loginDir+"\\1\\1\\sample.pdf");
        testFile.close();

        File userCourse2 = new File(loginDir+"\\2\\1");
        userCourse2.mkdirs();
    }

    @Test
    @DisplayName("Тест метода - deleteUserDir()")
    void deleteUserDirTest(){
        File userDir = userFolderService.getUserDirFile("testLogin");

        userFolderService.deleteUserDir(userDir);

        assertFalse(userFolderService.getUserDirFile("testLogin").exists());
    }

    @Test
    @DisplayName("Тест метода - copyMaterialToUserDir")
    void copyMaterialToUserDirTest() throws IOException {
        userFolderService.copyMaterialToUserDir(userFolderService.getUserDir("testLogin\\1\\1"), userFolderService.getUserDir("testLogin\\2\\1"), "sample.pdf");

        assertTrue(new File(userFolderService.getUserDir("testLogin\\2\\1\\sample.pdf")).exists());

    }
}
