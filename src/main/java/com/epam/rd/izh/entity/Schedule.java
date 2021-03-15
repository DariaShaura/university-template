package com.epam.rd.izh.entity;

import lombok.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
@Getter
@EqualsAndHashCode
public class Schedule {
    private long id;
    private long idTheme;
    private String themeTitle;
    private LocalDate startDate;
    private LocalDate endDate;
}
