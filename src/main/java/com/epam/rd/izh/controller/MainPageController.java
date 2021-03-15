package com.epam.rd.izh.controller;

import com.epam.rd.izh.service.CourseService;
import com.epam.rd.izh.service.UserService;
import jdk.nashorn.internal.ir.debug.JSONWriter;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Controller
public class MainPageController {

    @Autowired
    UserService userService;

    @Autowired
    CourseService courseService;

    @GetMapping("/main")
    public String mainPage(Authentication authentication) {

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        boolean authorized = authorities.contains(new SimpleGrantedAuthority("TEACHER"));

        if(authorities.contains(new SimpleGrantedAuthority("TEACHER"))){
            return "redirect:/mainTeacher";
        }
        else if (authorities.contains(new SimpleGrantedAuthority("STUDENT"))){
            return "redirect:/mainStudent";
        }
        else if (authorities.contains(new SimpleGrantedAuthority("ADMIN"))){
            return "redirect:/mainAdmin";
        }

         return "redirect:/login";
    }
}
