package com.epam.rd.izh.dto;

import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
@Getter
public class MaterialDto {
    private long id;
    private String title;
    private String type;
    private String path;
    @Builder.Default private NeedAction needAction = NeedAction.NONE;
}
