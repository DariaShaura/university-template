package com.epam.rd.izh.service;

import com.epam.rd.izh.dto.CourseDto;
import com.epam.rd.izh.dto.ThemeDto;
import com.epam.rd.izh.entity.Course;
import com.epam.rd.izh.entity.Material;
import com.epam.rd.izh.entity.Theme;
import com.epam.rd.izh.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseServiceMapper implements CourseService{

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    UserService userService;

    @Override
    public Course getCourse(CourseDto courseDto){
        //TODO!!!
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
    public Theme getTheme(ThemeDto themeDto){
        //TODO
        Theme theme = new Theme();

        return theme;
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
}
