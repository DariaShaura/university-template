package com.epam.rd.izh;

import com.epam.rd.izh.controller.MainPageStudentController;
import com.epam.rd.izh.dto.AuthorizedUserDto;
import com.epam.rd.izh.dto.CourseDto;
import com.epam.rd.izh.dto.NeedAction;
import com.epam.rd.izh.dto.StudentCourseLabDto;
import com.epam.rd.izh.service.CourseService;
import com.epam.rd.izh.service.UserDetailsServiceMapper;
import com.epam.rd.izh.service.UserFolderService;
import com.epam.rd.izh.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
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

    @MockBean
    UserFolderService userFolderService;

    private JacksonTester<CourseDto> jsonCourseDto;

    private JacksonTester<StudentCourseLabDto> jsonStudentCourseLabDto;

    @BeforeEach
    public void setUp() throws Exception {
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @WithMockUser(authorities = {"STUDENT"})
    @Test
    void mainStudentGetTest() throws Exception {
        mockMvc.perform(get("/mainStudent"))
                .andExpect(status().isOk())
                .andExpect(view().name("mainStudent"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @WithMockUser(authorities = {"STUDENT"})
    @Test
    void mainStudentCourseTest() throws Exception {
        mockMvc.perform(get("/mainStudent/course"))
                .andExpect(status().isOk())
                .andExpect(view().name("studentCourse"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @WithMockUser(authorities = {"STUDENT"})
    @Test
    void mainStudentCoursePostTest() throws Exception {
        given(courseService.getCourseDto(1))
                .willReturn(new CourseDto(1, "","",3,"",new ArrayList<>(), NeedAction.NONE, ""));

        MockHttpServletResponse response = mockMvc.perform(post("/mainStudent/course").param("idCourse","1"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse();

        String strResp = response.getContentAsString();
        assertThat(strResp).isEqualTo(jsonCourseDto.write(new CourseDto(1, "","",3,"",new ArrayList<>(), NeedAction.NONE, "")).getJson());
    }

    @WithMockUser(authorities = {"STUDENT"}, value = "Romashka")
    @Test
    void getStudentCourseMarksTest() throws Exception {
        given(courseService.getStudentCourseLabList("Romashka", 1))
                .willReturn(new ArrayList<>());

        MockHttpServletResponse response = mockMvc.perform(post("/mainStudent/course/marks").param("idCourse","1"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse();

        assertThat(response.getContentAsString()).isEqualTo("[]");
    }

    @WithMockUser(authorities = {"STUDENT"}, value = "Romashka")
    @Test
    void updateStudentCourseLabTest() throws Exception {

        StudentCourseLabDto studentCourseLabDto = new StudentCourseLabDto(1,-1,"title","path",0);

        given(courseService.updateStudentLab("Romashka", studentCourseLabDto))
                .willReturn(true);

        MockHttpServletResponse response = mockMvc.perform(post("/mainStudent/course/labs/update").contentType("application/json")
                .content(objectMapper.writeValueAsBytes(studentCourseLabDto))
                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse();
    }
}
