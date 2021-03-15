package com.epam.rd.izh.entity;

import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
@Getter
public class Mark {
    private long id;
    private  long idStudent;
    private long idLab;
    private String path;
    private int mark;
}
