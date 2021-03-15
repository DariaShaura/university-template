package com.epam.rd.izh.dto;

import lombok.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;


@Component
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
@Getter
@ToString
@EqualsAndHashCode
public class ScheduleDto {
    private long id;
    private long idTheme;
    private String themeTitle;
    private String startDate;
    private String endDate;
    @Builder.Default private NeedAction needAction = NeedAction.NONE;
}
