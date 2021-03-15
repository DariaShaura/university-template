package com.epam.rd.izh.dto;

import lombok.*;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Size;

@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
@Component
@ToString
@EqualsAndHashCode
public class AuthorizedUserDto {

    @Size(min=6)
    private String login;

    private String password;
    private String firstName;
    private String secondName;
    private String lastName;
    private String birthDate;
    private String role;
}
