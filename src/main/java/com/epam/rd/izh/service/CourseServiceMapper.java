package com.epam.rd.izh.service;

import com.epam.rd.izh.dto.CourseDto;
import com.epam.rd.izh.dto.MaterialDto;
import com.epam.rd.izh.dto.ThemeDto;
import com.epam.rd.izh.entity.Course;
import com.epam.rd.izh.entity.Material;
import com.epam.rd.izh.entity.Theme;
import com.epam.rd.izh.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseServiceMapper implements CourseService{

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    UserService userService;

    @Override
    public Course getCourse(CourseDto courseDto){

        Course course = new Course().builder()
                            .title(courseDto.getTitle())
                            .description(courseDto.getDescription())
                            .hours(courseDto.getHours())
                            .build();

        long id_teacher = userService.getAuthorizedUserId(courseDto.getTeacherLogin());

        course.setId_teacher(id_teacher);

        return course;
    }

    @Override
    public Course getCourse(long id){
        return courseRepository.getCourseById(id);
    }

    @Override
    public Theme getTheme(ThemeDto themeDto){
        //TODO
        Theme theme = new Theme();

        return theme;
    }

    @Override
    public List<Theme> getCourseThemes(long idCourse){
        return courseRepository.getThemeList(idCourse);
    }

    @Override
    public boolean addCourse(Course course){
        return courseRepository.addCourse(course);
    }

    @Override
    public boolean addThemeInCourse(Theme theme){
        return courseRepository.addTheme(theme);
    }

    @Override
    public boolean addMaterialInTheme(Material material){
        return courseRepository.addMaterial(material);
    }

    @Override
    public MaterialDto getMaterialDto(Material material){
        return new MaterialDto().builder()
                .title(material.getTitle())
                .type(material.getType())
                .path(material.getPath())
                .build();
    }

    @Override
    public List<Material> getThemeMaterials(long idTheme){
        return courseRepository.getMaterialsList(idTheme);
    }

    @Override
    public List<MaterialDto> getMaterialsDto(List<Material> materialList){
        return materialList.stream().map((s) -> getMaterialDto(s)).collect(Collectors.toList());
    }

    @Override
    public ThemeDto getThemeDto(Theme theme, List<MaterialDto> materialsDto){
        return new ThemeDto().builder()
                .title(theme.getTitle())
                .materials(materialsDto)
                .build();
    }

    @Override
    public CourseDto getCourseDto(String login, Course course, List<ThemeDto> themesDto){

        return new CourseDto().builder()
                .title(course.getTitle())
                .description(course.getDescription())
                .hours(course.getHours())
                .themes(themesDto)
                .teacherLogin(login)
                .build();
    }
}
