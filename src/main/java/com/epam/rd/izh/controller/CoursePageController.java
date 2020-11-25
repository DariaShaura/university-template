package com.epam.rd.izh.controller;

import com.epam.rd.izh.dto.AuthorizedUserDto;
import com.epam.rd.izh.dto.CourseDto;
import com.epam.rd.izh.dto.MaterialDto;
import com.epam.rd.izh.dto.ThemeDto;
import com.epam.rd.izh.entity.Course;
import com.epam.rd.izh.entity.Material;
import com.epam.rd.izh.entity.Theme;
import com.epam.rd.izh.service.CourseService;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
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

    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
    private class CourseTemp{
        private String title;
        private String description;
        private int hours;
        private String teacherLogin;
    }

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

        return "courseAdd";
    }

    @PostMapping(value = "/mainTeacher/courseAdd/proceed", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ResponseEntity<?> addCourseProceed(@Valid @RequestBody CourseDto courseDto, Errors errors) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String login = auth.getName();

        courseDto.setTeacherLogin(login);

        Course course = courseService.getCourse(courseDto);

        courseService.addCourse(course);

        courseDto.getThemes().stream()
                .forEach(themeDto -> {Theme theme = courseService.getTheme(course.getId(), themeDto);
                                        courseService.addTheme(theme);
                                        themeDto.getMaterials().stream().forEach(materialDto ->
                                                { Material material = courseService.getMaterial(theme.getId(), materialDto);
                                                  courseService.addMaterial(material); });});

        return ResponseEntity.ok(course.getId());
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
