package com.epam.rd.izh.service;

import com.epam.rd.izh.dto.CourseDto;
import com.epam.rd.izh.dto.ThemeDto;
import com.epam.rd.izh.entity.Course;
import com.epam.rd.izh.entity.Material;
import com.epam.rd.izh.entity.Theme;

public interface CourseService {
    Course getCourse(CourseDto courseDto);

    Theme getTheme(ThemeDto themeDto);

    boolean addCourse(Course course);

    boolean addThemeInCourse(Theme theme);

    boolean addMaterialInTheme(Material material);
}
