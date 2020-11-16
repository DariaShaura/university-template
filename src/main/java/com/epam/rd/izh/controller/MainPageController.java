package com.epam.rd.izh.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

@Controller
public class MainPageController {


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

        model.addAttribute("login", authentication.getName());

        return "mainTeacher";
    }
}
