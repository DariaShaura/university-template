package com.epam.rd.izh;

import com.epam.rd.izh.controller.MainPageStudentController;
import com.epam.rd.izh.service.CourseService;
import com.epam.rd.izh.service.UserDetailsServiceMapper;
import com.epam.rd.izh.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = MainPageStudentController.class)
@ActiveProfiles("test")
public class MainPageStudentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    CourseService courseService;

    @MockBean
    private UserDetailsServiceMapper userDetailsService;

    @WithMockUser(value = "Romashka")
    @Test
    void mainStudentGetTest() throws Exception {
        mockMvc.perform(get("/mainStudent"))
                .andExpect(status().isOk())
                .andExpect(view().name("mainStudent"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @WithMockUser(value = "Romashka")
    @Test
    void mainStudentPostTest() throws Exception {
        List<Map<String, Object>> courses = new ArrayList<>();
        Map<String, Object> course1 = new HashMap<>();
        course1.put("id", 1); course1.put("title", "Методы оптимизации");
        courses.add(course1);
        Map<String, Object> course2 = new HashMap<>();
        course2.put("id", 2); course2.put("title", "test Course1");
        courses.add(course2);

        Mockito.when(courseService.getStudentCourses("Romashka")).thenReturn(courses);

        MvcResult mvcResult = mockMvc.perform(post("/mainStudent"))
                .andExpect(status().isOk())
                .andReturn();

        String actualResponseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);

        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(
                objectMapper.writeValueAsString(courses));
    }
}
