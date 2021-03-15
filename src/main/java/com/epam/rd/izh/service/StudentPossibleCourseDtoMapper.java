package com.epam.rd.izh.service;

import com.epam.rd.izh.dto.StudentPossibleCourseDto;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class StudentPossibleCourseDtoMapper implements RowMapper<StudentPossibleCourseDto> {
    @Override
    public StudentPossibleCourseDto mapRow(ResultSet resultSet, int i) throws SQLException {

        StudentPossibleCourseDto studentPossibleCoursesDto = StudentPossibleCourseDto.builder()
                .idCourse(resultSet.getLong("course.id"))
                .courseTitle(resultSet.getString("course.title"))
                .teacherName(resultSet.getString("teacher_name"))
                .hours(resultSet.getInt("course.hours"))
                .build();

        return studentPossibleCoursesDto;
    }
}