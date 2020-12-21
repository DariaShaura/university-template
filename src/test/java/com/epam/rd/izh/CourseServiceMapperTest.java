package com.epam.rd.izh;

import com.epam.rd.izh.dto.ScheduleDto;
import com.epam.rd.izh.exception.IncorrectDataException;
import com.epam.rd.izh.service.CourseServiceMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataAccessException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ComponentScan(basePackages = "com.epam.rd.izh")
public class CourseServiceMapperTest {
    @Autowired
    CourseServiceMapper courseServiceMapper;

    @Test
    @DisplayName("Тест метода getSchedule(ScheduleDto scheduleDto)")
    void getScheduleTestSchouldThrowException(){
        ScheduleDto scheduleDto = new ScheduleDto().builder()
                .id(2)
                .idTheme(2)
                .themeTitle("test")
                .startDate("2020-12-12")
                .endDate("2020-11-12")
                .build();

        assertThrows(IncorrectDataException.class, () -> {
            courseServiceMapper.getSchedule(scheduleDto);
        });
    }
}
