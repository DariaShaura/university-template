package com.epam.rd.izh.service;

import com.epam.rd.izh.dto.StudentThemeScheduleWithAttendenceDto;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class StudentThemeScheduleWithAttendenceMapper implements RowMapper<StudentThemeScheduleWithAttendenceDto> {
    @Override
    public StudentThemeScheduleWithAttendenceDto mapRow(ResultSet resultSet, int i) throws SQLException {

        StudentThemeScheduleWithAttendenceDto studentThemeScheduleWithAttendenceDto = StudentThemeScheduleWithAttendenceDto.builder()
                .themeTitle(resultSet.getString("theme.title"))
                .startDate(resultSet.getString("schedule.start_date"))
                .endDate(resultSet.getString("schedule.end_date"))
                .attended(resultSet.getBoolean("attendence.attended"))
                .build();

        return studentThemeScheduleWithAttendenceDto;
    }
}
