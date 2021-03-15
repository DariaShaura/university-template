package com.epam.rd.izh.service;

import com.epam.rd.izh.entity.Course;
import com.epam.rd.izh.entity.Theme;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ThemeMapper implements RowMapper<Theme> {

        public Theme mapRow(ResultSet resultSet, int i) throws SQLException {

            Theme theme = Theme.builder()
                    .id(resultSet.getLong("theme.id"))
                    .title(resultSet.getString("theme.title"))
                    .id_Course(resultSet.getLong("theme.id_course"))
                    .build();

            return theme;
        }
}
