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
@ToString
public class StudentCourseLabDto {
    private long idLab;
    private long idMark;
    private String labTitle;
    private String path;
    private int mark;
}
