package com.epam.rd.izh.entity;

import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
@Getter
public class Course {
    private long id;
    private String title;
    private String description;
    private int hours;
    private long id_teacher;
}
