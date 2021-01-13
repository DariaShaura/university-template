package com.epam.rd.izh;

import com.epam.rd.izh.controller.MainPageTeacherController;
import com.epam.rd.izh.dto.CourseDto;
import com.epam.rd.izh.dto.NeedAction;
import com.epam.rd.izh.dto.ScheduleDto;
import com.epam.rd.izh.dto.ThemeDto;
import com.epam.rd.izh.service.CourseService;
import com.epam.rd.izh.service.UserDetailsServiceMapper;
import com.epam.rd.izh.service.UserFolderService;
import com.epam.rd.izh.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = MainPageTeacherController.class)
@ActiveProfiles("test")
public class MainPageTeacherControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    CourseService courseService;

    @MockBean
    private UserDetailsServiceMapper userDetailsService;

    @MockBean
    UserFolderService userFolderService;

    @MockBean
    UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @WithMockUser(authorities = {"TEACHER"})
    @Test
    void addCourseTest() throws Exception {
        mockMvc.perform(get("/mainTeacher/courseAdd"))
                .andExpect(status().isOk())
                .andExpect(view().name("teacherCourseAdd"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @WithMockUser(authorities = {"TEACHER"}, value = "IIIvan")
    @Test
    void addCourseProceedTest() throws Exception {

        CourseDto courseDto = new CourseDto(1, "","",3,"IIIvan",new ArrayList<>(), NeedAction.NONE, "");

        given(courseService.addCourse(new CourseDto(0, "","",3,"IIIvan",new ArrayList<>(), NeedAction.NONE, "")))
                .willReturn(courseDto);

        MockHttpServletResponse response = mockMvc.perform(post("/mainTeacher/courseAdd/proceed").contentType("application/json")
                .content(objectMapper.writeValueAsString(new CourseDto(0, "","",3,"IIIvan",new ArrayList<>(), NeedAction.NONE, "")))
                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse();

        assertThat(response.getContentAsString()).isEqualTo("1");
    }

    @WithMockUser(authorities = {"TEACHER"}, value = "IIIvan")
    @Test
    void getCourseInfoTest() throws Exception {

        CourseDto courseDto = new CourseDto(1, "","",3,"IIIvan",new ArrayList<>(), NeedAction.NONE, "");

        given(courseService.getCourseDto(1))
                .willReturn(courseDto);

        MockHttpServletResponse response = mockMvc.perform(post("/mainTeacher/course").param("idCourse", "1"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse();

        assertThat(response.getContentAsString()).isEqualTo(objectMapper.writeValueAsString(courseDto));
    }

    @WithMockUser(authorities = {"TEACHER"}, value = "IIIvan")
    @Test
    void deleteCourseTest() throws Exception {

        CourseDto courseDto = new CourseDto(1, "","",3,"IIIvan",new ArrayList<>(), NeedAction.NONE, "");

        given(courseService.deleteCourse(1))
                .willReturn(true);

        mockMvc.perform(post("/mainTeacher/course/delete").param("idCourse", "1"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

    }

    @WithMockUser(authorities = {"TEACHER"}, value = "IIIvan")
    @Test
    void editCourseTest() throws Exception {

        CourseDto courseDto = new CourseDto(1, "","",3,"IIIvan", new ArrayList<>(), NeedAction.NONE, "");

        given(courseService.updateCourseThemesMaterials(courseDto))
                .willReturn(courseDto);

        MockHttpServletResponse response = mockMvc.perform(post("/mainTeacher/course/edit").contentType("application/json")
                .content(objectMapper.writeValueAsString(courseDto))
                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse();

        assertThat(response.getContentAsString()).isEqualTo(objectMapper.writeValueAsString(courseDto));
    }

    @WithMockUser(authorities = {"TEACHER"}, value = "IIIvan")
    @Test
    void getCourseScheduleTest() throws Exception {

        given(courseService.getCourseScheduleDto(1))
                .willReturn(new ArrayList<>());

        MockHttpServletResponse response = mockMvc.perform(post("/mainTeacher/course/schedule/load").param("idCourse", "1"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse();

        assertThat(response.getContentAsString()).isEqualTo("[]");
    }

    @WithMockUser(authorities = {"TEACHER"}, value = "IIIvan")
    @Test
    void updateCourseScheduleTest() throws Exception {

        given(courseService.updateCourseSchedule(new ArrayList<ScheduleDto>()))
                .willReturn(true);

        MockHttpServletResponse response = mockMvc.perform(post("/mainTeacher/course/schedule/update").contentType("application/json")
                .content(objectMapper.writeValueAsString(new ArrayList<ScheduleDto>()))
                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse();

        assertThat(response.getContentAsString()).isEqualTo("[]");
    }
}
