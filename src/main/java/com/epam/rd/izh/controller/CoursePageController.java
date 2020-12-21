package com.epam.rd.izh.controller;

import com.epam.rd.izh.dto.*;
import com.epam.rd.izh.entity.Course;
import com.epam.rd.izh.entity.Material;
import com.epam.rd.izh.entity.Schedule;
import com.epam.rd.izh.entity.Theme;
import com.epam.rd.izh.exception.IncorrectDataException;
import com.epam.rd.izh.service.CourseService;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @Autowired
    CourseService courseService;

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

        // создать класс CourseDTO из сущности Course
        CourseDto courseDto = courseService.getCourseDto(login, course);

        // отправить класс CourseDTO в ответе на Post-запрос
        return ResponseEntity.ok(courseDto);
    }

    @PostMapping(value = "/mainTeacher/course/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ResponseEntity<?> deleteCourse(HttpServletRequest request) {

        long idCourse = Long.parseLong(request.getParameter("idCourse"));

        boolean delete = courseService.deleteCourse(idCourse);

        // отправить класс CourseDTO в ответе на Post-запрос
        return ResponseEntity.ok(delete);
    }

    @PostMapping(value = "/mainTeacher/course/edit", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ResponseEntity<?> editCourse(@Valid @RequestBody CourseDto courseDto, Errors errors) {

        CourseDto updatedCourseDto = courseService.updateCourseThemesMaterials(courseDto);

        return ResponseEntity.ok(updatedCourseDto);
    }

    @PostMapping(value = "/mainTeacher/course/schedule/load", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<?> getCourseSchedule(HttpServletRequest request){
        long idCourse = Long.parseLong(request.getParameter("idCourse"));

        List<ScheduleDto> scheduleDtoList = courseService.getCourseScheduleDto(idCourse);

        return ResponseEntity.ok(scheduleDtoList);
    }

    @PostMapping(value = "/mainTeacher/course/schedule/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<?> updateCourseSchedule(@Valid @RequestBody List<ScheduleDto> scheduleDtoList){

        try {
            courseService.updateCourseSchedule(scheduleDtoList);

            return ResponseEntity.ok(scheduleDtoList);
        }
        catch (IncorrectDataException e){
            long incorrectScheduleId = ((ScheduleDto)(e.getIncorrectObject())).getId();

            return new ResponseEntity<>(
                    incorrectScheduleId,
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/mainTeacher/course/participants", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<?> getCourseParticipants(HttpServletRequest request){
        long idCourse = Long.parseLong(request.getParameter("idCourse"));

        List<ParticipantDto> courseParticipantList = courseService.getCourseParticipants(idCourse);

        return ResponseEntity.ok(courseParticipantList);
    }

    @PostMapping(value = "/mainTeacher/course/marks", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<?> getCourseMarks(HttpServletRequest request){
        long idCourse = Long.parseLong(request.getParameter("idCourse"));

        List<MarkDto> courseMarkList = courseService.getCourseMarks(idCourse);

        return ResponseEntity.ok(courseMarkList);
    }

}
