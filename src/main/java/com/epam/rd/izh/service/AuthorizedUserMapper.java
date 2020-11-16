package com.epam.rd.izh.service;

import com.epam.rd.izh.entity.AuthorizedUser;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class AuthorizedUserMapper implements RowMapper<AuthorizedUser> {

    @Override
    public AuthorizedUser mapRow(ResultSet resultSet, int i) throws SQLException {

        AuthorizedUser authorizedUser = AuthorizedUser.builder()
                    .id(resultSet.getLong("user.id"))
                    .login(resultSet.getString("user.login"))
                    .password(resultSet.getString("user.password"))
                    .firstName(resultSet.getString("user.firstname"))
                    .secondName(resultSet.getString("user.secondName"))
                    .lastName(resultSet.getString("user.lastName"))
                    .birthDate(new java.sql.Date(resultSet.getDate("user.birthDate").getTime()).toLocalDate())
                    .role(resultSet.getString("role.role"))
                    .build();

        return authorizedUser;
    }
}

