package com.epam.rd.izh;

import com.epam.rd.izh.dto.*;
import com.epam.rd.izh.entity.Schedule;
import com.epam.rd.izh.exception.IncorrectDataException;
import com.epam.rd.izh.service.CourseServiceMapper;
import com.epam.rd.izh.service.UserFolderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataAccessException;
import java.io.File;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ComponentScan(basePackages = "com.epam.rd.izh")
public class CourseServiceMapperTest {
    @Autowired
    CourseServiceMapper courseServiceMapper;

    @Autowired
    UserFolderService userFolderService;

    @Test
    @DisplayName("Тест метода getSchedule(ScheduleDto scheduleDto)")
    void getScheduleTestShouldThrowException(){
        ScheduleDto scheduleDto = new ScheduleDto().builder()
                .id(2)
                .idTheme(2)
                .themeTitle("test")
                .startDate("2020-12-12")
                .endDate("2020-11-12")
                .build();

        assertThrows(IncorrectDataException.class, () -> {
            courseServiceMapper.getSchedule(scheduleDto);
        });
    }

    @Test
    @DisplayName("Тест метода - getCourse")
    void getCourseTestShouldThrowException(){
        CourseDto courseDto = new CourseDto().builder()
                                            .title("")
                                            .description("descr")
                                            .hours(3)
                                            .build();

        assertThrows(IncorrectDataException.class, ()->{courseServiceMapper.getCourse(courseDto);});
    }

    @Test
    @DisplayName("Тест метода - getTheme()")
    void getThemeTestShouldThrowException(){
        ThemeDto themeDto = new ThemeDto().builder()
                                            .title("")
                                            .build();

        assertThrows(IncorrectDataException.class, ()->{courseServiceMapper.getTheme(1, themeDto);});
    }

    @Test
    @DisplayName("Тест метода - getMaterial()")
    void getMaterialTestShouldThrowException(){
        MaterialDto materialDto = new MaterialDto().builder()
                                                .type("")
                                                .build();

        assertThrows(IncorrectDataException.class, ()->{courseServiceMapper.getMaterial(1, 1, materialDto);});
    }

    @Test
    @DisplayName("Тест метода - getMark()")
    void getMarkTestShouldThrowException(){
        MarkDto markDto = new MarkDto().builder()
                                  .mark(0)
                                  .build();

        assertThrows(IncorrectDataException.class, ()->{courseServiceMapper.getMark(markDto);});
    }

    @ParameterizedTest(name = "Тест метода - updateMaterialFolder()({0})")
    @CsvSource({"DELETE","ADD","UPDATE"})
    void updateMaterialFolderTest(NeedAction action) throws IOException {

        String login = "tempLogin";

        MaterialDto materialDto = new MaterialDto().builder()
                                        .id(3)
                                        .path("sample.pdf")
                                        .build();

        materialDto.setNeedAction(action);

        switch (action){
            case ADD:
                userFolderService.deleteUserDir(userFolderService.getUserDirFile(login+"\\1\\3"));
                userFolderService.copyMaterialToUserDir(userFolderService.getUserDir(login),userFolderService.getUserDir(login+"\\tempCourse"),"sample.pdf");
                break;
            case DELETE:
                userFolderService.copyMaterialToUserDir(userFolderService.getUserDir(login),userFolderService.getUserDir(login+"\\1\\3"),"sample.pdf");
                break;
            case UPDATE:
                userFolderService.clearMaterialFolder(userFolderService.getUserDir(login+"\\1\\3"));
                userFolderService.copyMaterialToUserDir(userFolderService.getUserDir(login),userFolderService.getUserDir(login+"\\1\\3"),"sample.pdf");
                userFolderService.copyMaterialToUserDir(userFolderService.getUserDir(login),userFolderService.getUserDir(login+"\\tempCourse"),"sample1.pdf");
                materialDto.setPath("sample1.pdf");
                break;
        }

        courseServiceMapper.updateMaterialFolder(login,1, materialDto);

        File materialFolder = userFolderService.getUserDirFile(login+"\\1\\" + materialDto.getId());

        switch (action){
            case DELETE:
                assertTrue(!materialFolder.exists());
                break;
            case ADD:
            case UPDATE:
                assertTrue(materialFolder.exists() && (materialFolder.listFiles()[0].getName().equals(materialDto.getPath())));
                break;
        }
    }
}
