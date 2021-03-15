package com.epam.rd.izh.service;

import com.epam.rd.izh.dto.*;
import com.epam.rd.izh.entity.*;
import com.epam.rd.izh.exception.IncorrectDataException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface CourseService {
    Course getCourse(CourseDto courseDto) throws IncorrectDataException;

    Course getCourse(long id);

    List<Map<String, Object>> getTeachersCourses(String login);

    Theme getTheme(long idCourse, ThemeDto themeDto) throws IncorrectDataException;

    List<Theme> getCourseThemes(long idCourse);

    List<Material> getThemeMaterials(long idTheme);

    Material getMaterial(long idCourse, long idTheme, MaterialDto materialDto) throws IncorrectDataException;

    List<MaterialDto> getMaterialsDto(List<Material> materialList);

    boolean addCourse(Course course);

    CourseDto addCourse(CourseDto courseDto) throws IncorrectDataException;

    boolean addTheme(Theme theme);

    boolean addMaterial(Material material);

    CourseDto getCourseDto(String login, Course course);

    CourseDto getCourseDto(long idCourse);

    ThemeDto getThemeDto(Theme theme, List<MaterialDto> materialsDto);

    MaterialDto getMaterialDto(Material material);

    boolean deleteCourse(long id);

    void updateMaterialFolder(String login, long idCourse, MaterialDto materialDto) throws IOException;

    CourseDto updateCourseThemesMaterials(CourseDto courseDto) throws IncorrectDataException, IOException;

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

    List<StudentPossibleCourseDto> getStudentPossibleCourseList(String login);

    List<StudentCourseLabDto> getStudentCourseLabList(String login, long idCourse);

    boolean addStudentAdmissionOnCourse(String login, long idCourse);

    boolean deleteStudentAdmissionOnCourse(String login, long idCourse);

    List<StudentThemeScheduleWithAttendenceDto> getStudentCourseScheduleWithAttendence(String login, long idCourse);

    Mark getLab(long idStudent, StudentCourseLabDto studentCourseLabDto);

    boolean updateStudentLab(String login, long idCourse, StudentCourseLabDto studentCourseLabDto) throws IOException;

    boolean deleteStudentLab(String login, long idCourse, StudentCourseLabDto studentCourseLabDto);
}
