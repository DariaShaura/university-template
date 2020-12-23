package com.epam.rd.izh.dto;

import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
@Getter
public class StudentPossibleCourseDto {
    private long idCourse;
    private String courseTitle;
    private String teacherName;
    private int hours;
}
