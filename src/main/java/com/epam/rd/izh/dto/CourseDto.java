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
public class CourseDto {
    private long id;
    private String title;
    private String description;
    private int hours;
    private String teacherLogin;
    private List<ThemeDto> themes;
    @Builder.Default private NeedAction needAction = NeedAction.NONE;
    @Builder.Default private String teacherName = "";
}
