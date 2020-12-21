package com.epam.rd.izh;

import com.epam.rd.izh.controller.MainPageController;
import com.epam.rd.izh.service.CourseService;
import com.epam.rd.izh.service.UserDetailsServiceMapper;
import com.epam.rd.izh.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = MainPageController.class)
@ActiveProfiles("test")
public class MainPageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    CourseService courseService;

    @MockBean
    UserService userService;

    @MockBean
    private UserDetailsServiceMapper userDetailsService;

    @WithMockUser(value = "IIIvan")
    @Test
    void whenValidInput_thenReturns302() throws Exception {
        mockMvc.perform(get("/main"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/login"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
}
