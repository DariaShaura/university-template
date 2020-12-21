package com.epam.rd.izh.dto;

import lombok.*;
import org.springframework.stereotype.Component;


@Component
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
@Getter
public class ParticipantDto {
    private String lastName;
    private String firstname;
    private String secondName;
    private String birthDate;
}
