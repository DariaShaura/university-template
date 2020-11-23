package com.epam.rd.izh.repository;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Component
public class RoleRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public Map<String, String> getRoles() {
        String queryGetRoles = "SELECT roleTitle, role FROM role";

        return jdbcTemplate.query(queryGetRoles, new ResultSetExtractor<Map<String, String>>() {
            @Override
            public Map<String, String> extractData(ResultSet rs)
                    throws SQLException, DataAccessException {
                Map<String, String> map = new HashMap<>();

                while (rs.next()) {
                    map.put(rs.getString("roleTitle"), rs.getString("role"));
                }
                return map;
            }
        });
    }

    public int getRoleId(String role) {
        String queryGetRoleId = "SELECT id FROM role WHERE role.role = ?";

        return jdbcTemplate.queryForObject(queryGetRoleId, new Object[]{role}, Integer.class);
    }
}
