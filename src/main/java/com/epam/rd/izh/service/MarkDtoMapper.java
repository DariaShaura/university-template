package com.epam.rd.izh.service;

import com.epam.rd.izh.dto.MarkDto;
import com.epam.rd.izh.entity.Course;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MarkDtoMapper implements RowMapper<MarkDto> {
    @Override
    public MarkDto mapRow(ResultSet resultSet, int i) throws SQLException {

        MarkDto markDto = MarkDto.builder()
                .id(resultSet.getLong("mark.id"))
                .idLab(resultSet.getLong("mark.id_lab"))
                .idStudent(resultSet.getLong("mark.id_student"))
                .lastName(resultSet.getString("user.lastName"))
                .firstName(resultSet.getString("user.firstName"))
                .labDescription(resultSet.getString("material.title"))
                .pathToLab(resultSet.getString("mark.path"))
                .mark(resultSet.getInt("mark.mark"))
                .build();

        return markDto;
    }
}

