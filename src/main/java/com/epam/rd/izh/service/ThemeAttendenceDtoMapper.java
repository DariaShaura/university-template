package com.epam.rd.izh.service;

import com.epam.rd.izh.dto.MarkDto;
import com.epam.rd.izh.dto.ThemeAttendenceDto;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ThemeAttendenceDtoMapper implements RowMapper<ThemeAttendenceDto> {
    @Override
    public ThemeAttendenceDto mapRow(ResultSet resultSet, int i) throws SQLException {

        ThemeAttendenceDto themeAttendenceDto = ThemeAttendenceDto.builder()
                .idTheme(resultSet.getLong("id_theme"))
                .attendence(resultSet.getBoolean("attended"))
                .build();

        return themeAttendenceDto;
    }
}
