package com.epam.rd.izh.entity;

import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
@Getter(value = AccessLevel.PUBLIC)
@EqualsAndHashCode
public class Attendence {
    private long idStudent;
    private long idTheme;
    private boolean attended;
}
