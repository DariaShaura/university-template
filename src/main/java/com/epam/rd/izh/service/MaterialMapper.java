package com.epam.rd.izh.service;

import com.epam.rd.izh.entity.Course;
import com.epam.rd.izh.entity.Material;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MaterialMapper implements RowMapper<Material> {
        @Override
        public Material mapRow(ResultSet resultSet, int i) throws SQLException {

            Material material = Material.builder()
                    .id(resultSet.getLong("material.id"))
                    .title(resultSet.getString("material.title"))
                    .type(resultSet.getString("material.type"))
                    .path(resultSet.getString("material.path"))
                    .idTheme(resultSet.getLong("material.id_theme"))
                    .idCourse(resultSet.getLong("material.id_course"))
                    .build();

            return material;
        }
    }
