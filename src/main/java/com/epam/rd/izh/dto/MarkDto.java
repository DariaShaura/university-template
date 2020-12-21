package com.epam.rd.izh.dto;

import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
@Getter
@EqualsAndHashCode
public class MarkDto {
    private long id;
    private long idLab;
    private long idStudent;
    private String lastName;
    private String firstName;
    private String labDescription;
    private String pathToLab;
    private int mark;

}
