package com.epam.rd.izh.dto;

import lombok.*;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
@Getter
@EqualsAndHashCode
public class ParticipantDto {
    private long idStudent;
    private String lastName;
    private String firstName;
    private String secondName;
    private String birthDate;
    private List<ThemeAttendenceDto> attendenceList;
}
