package com.epam.rd.izh.dto;

import lombok.*;
import org.springframework.stereotype.Component;

@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
@Component
public class ActiveUserDto {

    private long idUser;
    private String login;
    private String role;
    private String firstName;
    private String secondName;
    private String lastName;
    private String birthDate;
    @Builder.Default private boolean active = false;
}
