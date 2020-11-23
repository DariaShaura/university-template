package com.epam.rd.izh.controller;

import com.epam.rd.izh.service.UserService;
import jdk.nashorn.internal.ir.debug.JSONWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Controller
public class MainPageController {


    @Autowired
    UserService userService;

    @GetMapping("/main")
    public String mainPage(Authentication authentication) {

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        boolean authorized = authorities.contains(new SimpleGrantedAuthority("TEACHER"));

        if(authorized){
            return "redirect:/mainTeacher";
        }

         return "redirect:/login";
    }

    @GetMapping("/mainTeacher")
    public String mainTeacher(Authentication authentication, Model model) {

        String login = authentication.getName();

        model.addAttribute("login", login);

        List<Map<String, Object>> teachersCourses = userService.getTeachersCourses(login);

        model.addAttribute("coursesList", teachersCourses);

        return "mainTeacher";
    }

    @PostMapping("/mainTeacher")
    public @ResponseBody List<Map<String, Object>> mainTeacher(Authentication authentication) {

        String login = authentication.getName();

        List<Map<String, Object>> teachersCourses = userService.getTeachersCourses(login);




        return teachersCourses;
    }
}
