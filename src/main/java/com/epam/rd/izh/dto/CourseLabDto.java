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
public class CourseLabDto {
    private long idCourse;
    private long idLab;
    private String labTitle;
}
