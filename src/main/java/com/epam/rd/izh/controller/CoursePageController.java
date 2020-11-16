package com.epam.rd.izh.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CoursePageController {

    @GetMapping("/courseAdd")
    public String addCourse(Authentication authentication, Model model) {

        model.addAttribute("login", authentication.getName());

        return "courseAdd";
    }

    @GetMapping("/course")
    public String coursePage(Authentication authentication, Model model) {

        model.addAttribute("login", authentication.getName());

        return "course";
    }
}
