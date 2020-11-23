package com.epam.rd.izh.controller;

import com.epam.rd.izh.dto.AuthorizedUserDto;
import com.epam.rd.izh.dto.CourseDto;
import com.epam.rd.izh.dto.ThemeDto;
import com.epam.rd.izh.entity.Course;
import com.epam.rd.izh.entity.Material;
import com.epam.rd.izh.entity.Theme;
import com.epam.rd.izh.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class CoursePageController {

    @Autowired
    CourseService courseService;

    @GetMapping("/mainTeacher/courseAdd")
    public String addCourse(Authentication authentication, Model model) {

        model.addAttribute("login", authentication.getName());

        if(!model.containsAttribute("courseAddForm")){
            model.addAttribute("courseAddForm", new CourseDto());
        }

        return "courseAdd";
    }

    @PostMapping("/mainTeacher/courseAdd/proceed")
    public String addCourseProceed(@Valid @ModelAttribute("courseAddForm") CourseDto courseDto,
                                   BindingResult bindingResult, HttpServletRequest request, RedirectAttributes redirectAttributes) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String login = auth.getName();

        courseDto.setTeacherLogin(login);

        Course course = courseService.getCourse(courseDto);

        courseService.addCourse(course);

        String[] themeTitles = request.getParameterValues("themeTitle");

        int i =0;
        for(String themeTitle: themeTitles){
            i++;
            Theme theme = new Theme().builder()
                                .title(themeTitle)
                                .id_Course(course.getId())
                                .build();
            // add theme in DB
            courseService.addThemeInCourse(theme);

            String[] descriptions = request.getParameterValues("description" + i);
            String[] materialTypes = request.getParameterValues("materialType" + i);
            String[] filePath = request.getParameterValues("filePath" + i);
            for(int j=0; j< descriptions.length; j++){
                Material material = new Material().builder()
                                                .title(descriptions[j])
                                                .type(materialTypes[j])
                                                .path(filePath[j])
                                                .id_Theme(theme.getId())
                                                .build();
                // add material in DB
                courseService.addMaterialInTheme(material);
            }
        }

        //TODO
        //redirect to mainTeacher/course, course = page with new course
        redirectAttributes.addFlashAttribute("idCourse", course.getId());

        return "redirect:/mainTeacher/course";
    }

    @GetMapping("/mainTeacher/course")
    public String coursePage(Authentication authentication, Model model) {

        model.addAttribute("login", authentication.getName());

        return "course";
    }
}
