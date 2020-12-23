package com.epam.rd.izh.service;

import com.epam.rd.izh.dto.StudentCourseDto;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class StudentCourseDtoMapper implements RowMapper<StudentCourseDto> {
    @Override
    public StudentCourseDto mapRow(ResultSet resultSet, int i) throws SQLException {

        StudentCourseDto studentCoursesDto = StudentCourseDto.builder()
                .idCourse(resultSet.getLong("id_course"))
                .courseTitle(resultSet.getString("course.title"))
                .build();

        return studentCoursesDto;
    }
}
