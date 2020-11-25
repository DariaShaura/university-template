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

    Theme getTheme(long idCourse, ThemeDto themeDto);

    List<Theme> getCourseThemes(long idCourse);

    List<Material> getThemeMaterials(long idTheme);

    Material getMaterial(long idTheme, MaterialDto materialDto);

    List<MaterialDto> getMaterialsDto(List<Material> materialList);

    boolean addCourse(Course course);

    boolean addTheme(Theme theme);

    boolean addMaterial(Material material);

    CourseDto getCourseDto(String login, Course course, List<ThemeDto> themesDto);

    ThemeDto getThemeDto(Theme theme, List<MaterialDto> materialsDto);

    MaterialDto getMaterialDto(Material material);
}
