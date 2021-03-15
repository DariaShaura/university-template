package com.epam.rd.izh.entity;

import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
@Getter
@EqualsAndHashCode
public class Material {
    private long id;
    private String title;
    private String type;
    private String path;
    private long idTheme;
    private long idCourse;
}
