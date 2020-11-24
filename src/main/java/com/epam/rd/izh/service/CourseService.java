package com.epam.rd.izh.service;

import com.epam.rd.izh.dto.CourseDto;
import com.epam.rd.izh.dto.MaterialDto;
import com.epam.rd.izh.dto.ThemeDto;
import com.epam.rd.izh.entity.Course;
import com.epam.rd.izh.entity.Material;
import com.epam.rd.izh.entity.Theme;

import java.util.List;

public interface CourseService {
    Course getCourse(CourseDto courseDto);

    Course getCourse(long id);

    Theme getTheme(ThemeDto themeDto);

    List<Theme> getCourseThemes(long idCourse);

    List<Material> getThemeMaterials(long idTheme);

    List<MaterialDto> getMaterialsDto(List<Material> materialList);

    boolean addCourse(Course course);

    boolean addThemeInCourse(Theme theme);

    boolean addMaterialInTheme(Material material);

    CourseDto getCourseDto(String login, Course course, List<ThemeDto> themesDto);

    ThemeDto getThemeDto(Theme theme, List<MaterialDto> materialsDto);

    MaterialDto getMaterialDto(Material material);
}
