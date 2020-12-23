package com.epam.rd.izh.service;

import com.epam.rd.izh.dto.*;
import com.epam.rd.izh.entity.*;
import com.epam.rd.izh.exception.IncorrectDataException;

import java.util.List;
import java.util.Map;

public interface CourseService {
    Course getCourse(CourseDto courseDto);

    Course getCourse(long id);

    List<Map<String, Object>> getTeachersCourses(String login);

    Theme getTheme(long idCourse, ThemeDto themeDto);

    List<Theme> getCourseThemes(long idCourse);

    List<Material> getThemeMaterials(long idTheme);

    Material getMaterial(long idTheme, MaterialDto materialDto);

    List<MaterialDto> getMaterialsDto(List<Material> materialList);

    boolean addCourse(Course course);

    boolean addTheme(Theme theme);

    boolean addMaterial(Material material);

    CourseDto getCourseDto(String login, Course course);

    ThemeDto getThemeDto(Theme theme, List<MaterialDto> materialsDto);

    MaterialDto getMaterialDto(Material material);

    boolean deleteCourse(long id);

    CourseDto updateCourseThemesMaterials(CourseDto courseDto);

    List<ScheduleDto> getCourseScheduleDto(long id);

    Schedule getSchedule(ScheduleDto scheduleDto) throws IncorrectDataException;

    ScheduleDto getScheduleDto(Schedule schedule);

    boolean updateCourseSchedule(List<ScheduleDto> scheduleDtoList) throws IncorrectDataException;

    List<ParticipantDto> getCourseParticipants(long idCourse);

    List<MarkDto> getCourseMarks(long idCourse);

    boolean updateCourseMarks(List<MarkDto> markDtoList) throws IncorrectDataException;

    Mark getMark(MarkDto markDto) throws IncorrectDataException;

    boolean updateCourseAttendence(List<ParticipantDto> participantDtoList);

    List<StudentCourseDto> getStudentsCourseList(String login);

    List<StudentPossibleCourseDto> getStudentPossibleCourseList(long idStudent);

    List<CourseLabDto> getCourseLabList(long idCourse);
}
