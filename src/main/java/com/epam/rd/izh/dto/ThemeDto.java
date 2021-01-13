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
public class ThemeDto {
    private long id;
    private String title;
    private List<MaterialDto> materials;
    @Builder.Default private NeedAction needAction = NeedAction.NONE;
}
