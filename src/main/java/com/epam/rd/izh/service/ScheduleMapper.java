package com.epam.rd.izh.service;

import com.epam.rd.izh.entity.Material;
import com.epam.rd.izh.entity.Schedule;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ScheduleMapper implements RowMapper<Schedule> {
    @Override
    public Schedule mapRow(ResultSet resultSet, int i) throws SQLException {

        Schedule schedule = Schedule.builder()
                .id(resultSet.getLong("idShedule"))
                .idTheme(resultSet.getLong("idTheme"))
                .themeTitle(resultSet.getString("theme.title"))
                .build();

        if(resultSet.getDate("schedule.start_date") != null){
            schedule.setStartDate(resultSet.getDate("schedule.start_date").toLocalDate());
        }
        if(resultSet.getDate("schedule.end_date") != null){
            schedule.setEndDate(resultSet.getDate("schedule.end_date").toLocalDate());
        }

        return schedule;
    }
}
