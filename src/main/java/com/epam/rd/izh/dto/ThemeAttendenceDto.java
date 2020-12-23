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
@EqualsAndHashCode
public class ThemeAttendenceDto {
    private long idTheme;
    private Boolean attendence;
}
