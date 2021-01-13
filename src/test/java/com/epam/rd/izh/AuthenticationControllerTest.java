package com.epam.rd.izh;

import com.epam.rd.izh.config.WebConfig;
import com.epam.rd.izh.controller.AuthenticationController;
import com.epam.rd.izh.dto.AuthorizedUserDto;
import com.epam.rd.izh.entity.AuthorizedUser;
import com.epam.rd.izh.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AuthenticationController.class)
@ActiveProfiles("test")
@Import({UserValidator.class, WebConfig.class})
public class AuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private RoleService roleService;

    @MockBean
    private UserDetailsServiceMapper userDetailsService;

    @MockBean
    private UserFolderService userFolderService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    private JacksonTester<AuthorizedUserDto> jsonAuthorizedUserDto;

    @BeforeEach
    public void setUp() throws Exception {
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    void loginTest() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    void registrationTest() throws Exception {
        mockMvc.perform(get("/registration"))
                .andExpect(status().isOk())
                .andExpect(view().name("registration"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    void loginAvailabilityTest() throws Exception {

        given(userService.isLoginAvailable("Daria"))
                .willReturn(true);
        given(userService.isBirthDateCorrect("1987-09-26"))
                .willReturn(true);

        MockHttpServletResponse response = mockMvc.perform(post("/registration/availability").param("login", "Daria")
                .param("birthDate", "1987-09-26"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    void processRegistrationTest() throws Exception {

        AuthorizedUserDto authorizedUserDto = new AuthorizedUserDto("Daria",
                "19871987","Daria","Stanislavovna","Shaura","1987-09-26","STUDENT");

        AuthorizedUser authorizedUser = new AuthorizedUser(1,"Daria","19871987","Daria","Stanislavovna","Shaura",
                LocalDate.of(1987,9,26), "STUDENT");

        given(userService.getAuthorizedUser(authorizedUserDto)).willReturn(authorizedUser);

        given(userService.addAuthorizedUser(authorizedUser)).willReturn(true);

        mockMvc.perform(post("/registration/proceed").
                flashAttr("registrationForm", authorizedUserDto))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(view().name("redirect:/login"))
                .andReturn();

    }

}
