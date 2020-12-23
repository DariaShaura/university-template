package com.epam.rd.izh.service;

import com.epam.rd.izh.dto.CourseLabDto;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class CourseLabDtoMapper implements RowMapper<CourseLabDto> {
    @Override
    public CourseLabDto mapRow(ResultSet resultSet, int i) throws SQLException {

        CourseLabDto courseLabsDto = CourseLabDto.builder()
                .idCourse(resultSet.getLong("idCourse"))
                .idLab(resultSet.getLong("idLab"))
                .labTitle(resultSet.getString("labTitle"))
                .build();

        return courseLabsDto;
    }
}