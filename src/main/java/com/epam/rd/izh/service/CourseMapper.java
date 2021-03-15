package com.epam.rd.izh.service;

import com.epam.rd.izh.entity.AuthorizedUser;
import com.epam.rd.izh.entity.Course;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class CourseMapper implements RowMapper<Course> {
    @Override
    public Course mapRow(ResultSet resultSet, int i) throws SQLException {

        Course course = Course.builder()
                .id(resultSet.getLong("course.id"))
                .title(resultSet.getString("course.title"))
                .description(resultSet.getString("course.description"))
                .hours(resultSet.getInt("course.hours"))
                .id_teacher(resultSet.getLong("course.id_teacher"))
                .build();

        return course;
    }
}
