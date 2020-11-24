package com.epam.rd.izh.controller;

import com.epam.rd.izh.dto.AuthorizedUserDto;
import com.epam.rd.izh.dto.CourseDto;
import com.epam.rd.izh.dto.MaterialDto;
import com.epam.rd.izh.dto.ThemeDto;
import com.epam.rd.izh.entity.Course;
import com.epam.rd.izh.entity.Material;
import com.epam.rd.izh.entity.Theme;
import com.epam.rd.izh.service.CourseService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class CoursePageController {

    @Autowired
    CourseService courseService;

    @Getter
    private class GettingIdCourse {

        String idCourse;

        public void setIdCourse(String idCourse) {
            this.idCourse = idCourse;
        }
    }

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

    @PostMapping(value = "/mainTeacher/course", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ResponseEntity<?> getCourseInfo(Authentication authentication, HttpServletRequest request) {

        String login = authentication.getName();

        long idCourse = Long.parseLong(request.getParameter("idCourse"));

        // получить сущность Курс, Темы, Материалы
        Course course = courseService.getCourse(idCourse);
        List<Theme> themes = courseService.getCourseThemes(idCourse);
        List<ThemeDto> themeDtoList = new ArrayList<>();
        for(Theme theme: themes){
            long idTheme = theme.getId();
            // получить список материалов
            List<Material> materials = courseService.getThemeMaterials(idTheme);
            List<MaterialDto> materialDtoList = courseService.getMaterialsDto(materials);

            themeDtoList.add(courseService.getThemeDto(theme, materialDtoList));
        }
        // создать класс CourseDTO из сущностей Курс, Темы, Материалы
        CourseDto courseDto = courseService.getCourseDto(login, course, themeDtoList);

        // отправить класс CourseDTO в ответе на Post-запрос
        return ResponseEntity.ok(courseDto);
    }
}
