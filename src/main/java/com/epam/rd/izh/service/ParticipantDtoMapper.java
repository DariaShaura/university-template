package com.epam.rd.izh.service;

import com.epam.rd.izh.dto.ParticipantDto;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ParticipantDtoMapper implements RowMapper<ParticipantDto> {
@Override
public ParticipantDto mapRow(ResultSet resultSet,int i)throws SQLException{

        ParticipantDto participantDto = ParticipantDto.builder()
        .lastName(resultSet.getString("lastName"))
        .firstname(resultSet.getString("firstName"))
        .secondName(resultSet.getString("secondName"))
        .birthDate(resultSet.getString("birthDate"))
        .build();

        return participantDto;
}
}