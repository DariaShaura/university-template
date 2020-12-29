package com.epam.rd.izh.service;

import com.epam.rd.izh.dto.StudentCourseLabDto;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class StudentCourseLabDtoMapper implements RowMapper<StudentCourseLabDto> {
    @Override
    public StudentCourseLabDto mapRow(ResultSet resultSet, int i) throws SQLException {

        StudentCourseLabDto courseLabsDto = StudentCourseLabDto.builder()
                .idLab(resultSet.getLong("idLab"))
                .idMark(resultSet.getLong("idMark"))
                .labTitle(resultSet.getString("labTitle"))
                .path(resultSet.getString("mark.path"))
                .mark(resultSet.getInt("mark.mark"))
                .build();

        return courseLabsDto;
    }
}