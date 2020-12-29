package com.epam.rd.izh.controller;

import com.epam.rd.izh.dto.*;
import com.epam.rd.izh.entity.Course;
import com.epam.rd.izh.service.CourseService;
import com.epam.rd.izh.service.StudentCourseDtoMapper;
import com.epam.rd.izh.service.UserFolderService;
import com.epam.rd.izh.service.UserService;
import jdk.nashorn.internal.ir.debug.JSONWriter;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Controller
public class MainPageStudentController {

    @Autowired
    CourseService courseService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    UserFolderService userFolderService;

    @GetMapping("/mainStudent")
    public String mainStudent(Authentication authentication, Model model) {

        String login = authentication.getName();

        model.addAttribute("login", login);

        return "mainStudent";
    }

    @PostMapping(value = "/mainStudent", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<?> mainStudent(Authentication authentication) {

        String login = authentication.getName();

        List<StudentCourseDto> studentCourses = courseService.getStudentsCourseList(login);

        return ResponseEntity.ok(studentCourses);
    }

    @GetMapping("/mainStudent/course")
    public String mainStudentCourse(Authentication authentication, Model model) {

        String login = authentication.getName();

        model.addAttribute("login", login);

        return "studentCourse";
    }

    @PostMapping(value="/mainStudent/course",  produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ResponseEntity<?> studentCourseInfo(Authentication authentication, Model model) {

        long idCourse = Long.parseLong(request.getParameter("idCourse"));

        // создать класс CourseDTO из сущности Course
        CourseDto courseDto = courseService.getCourseDto(idCourse);

        // отправить класс CourseDTO в ответе на Post-запрос
        return ResponseEntity.ok(courseDto);
    }

    @PostMapping(value = "/mainStudent/course/marks", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<?> getStudentCourseMarks(Authentication authentication, HttpServletRequest request) {

        String login = authentication.getName();
        long idCourse = Long.parseLong(request.getParameter("idCourse"));

        List<StudentCourseLabDto> studentCourses = courseService.getStudentCourseLabList(login, idCourse);

        return ResponseEntity.ok(studentCourses);
    }

    @PostMapping(value = "/mainStudent/course/labs/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<?> updateStudentCourseLab(Authentication authentication, StudentCourseLabDto studentCourseLabDto) {

        String login = authentication.getName();

        if(courseService.updateStudentLab(login, studentCourseLabDto)){
            return new ResponseEntity<>(
                    true,
                    HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(
                    false,
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/mainStudent/deleteStudentLab", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<?> deleteStudentLab(Authentication authentication, StudentCourseLabDto studentCourseLabDto,  @RequestParam String idCourse) {

        String login = authentication.getName();

        if(courseService.deleteStudentLab(login, studentCourseLabDto)) {
            String realPathtoUploads = userFolderService.getUserDir(login + "\\" + idCourse + "\\" + studentCourseLabDto.getIdLab());

            userFolderService.deleteUserDir(new File(realPathtoUploads + "\\" + studentCourseLabDto.getPath()));

            return new ResponseEntity<>(
                    true,
                    HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(
                    false,
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/mainStudent/course/lab/upload")
    public @ResponseBody ResponseEntity<?> courseLabUploadFile(Authentication authentication,
                                                               @RequestParam("file") MultipartFile file, @RequestParam("idLab") String idLab,
                                                               @RequestParam("idCourse") String idCourse) {
        if (!file.isEmpty()) {
            try {
                String login = authentication.getName();

                userFolderService.saveMultipartFileTo(userFolderService.getUserDir(login+"\\"+ idCourse + "\\" + idLab), file);

                return new ResponseEntity<>(
                        true,
                        HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(
                        false,
                        HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(
                    false,
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/mainStudent/course/schedule", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<?> getStudentCourseSchedule(Authentication authentication, HttpServletRequest request) {

        String login = authentication.getName();
        long idCourse = Long.parseLong(request.getParameter("idCourse"));

        List<StudentThemeScheduleWithAttendenceDto> studentCourseSchedule = courseService.getStudentCourseScheduleWithAttendence(login, idCourse);

        return ResponseEntity.ok(studentCourseSchedule);
    }

    @PostMapping(value = "/mainStudent/PossibleCourses", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<?> getPossibleStudentCourses(Authentication authentication) {

        String login = authentication.getName();

        List<StudentPossibleCourseDto> studentPossibleCourses = courseService.getStudentPossibleCourseList(login);

        return ResponseEntity.ok(studentPossibleCourses);
    }

    @PostMapping(value = "/mainStudent/PossibleCourses/AddCourse", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<?> addStudentCourse(Authentication authentication, HttpServletRequest request) {

        long idCourse = Long.parseLong(request.getParameter("idCourse"));
        String login = authentication.getName();

        boolean result = courseService.addStudentAdmissionOnCourse(login, idCourse);

        return new ResponseEntity<>(
                true,
                HttpStatus.OK);
    }

    @GetMapping(value = "/mainStudent/PossibleCourses/info")
    public String possibleStudentCourseInfo(Authentication authentication, Model model) {

        String login = authentication.getName();

        model.addAttribute("login", login);

        return "studentPossibleCourseInfo";
    }

    @PostMapping(value = "/mainStudent/PossibleCourses/info", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<?> studentPossibleCourseInfo(HttpServletRequest request) {

        long idCourse = Long.parseLong(request.getParameter("idCourse"));

        CourseDto courseDto = courseService.getCourseDto(idCourse);

        return ResponseEntity.ok(courseDto);
    }

    @PostMapping(value = "/mainStudent/PossibleCourses/info/schedule", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<?> studentPossibleCourseSchedule(HttpServletRequest request) {

        long idCourse = Long.parseLong(request.getParameter("idCourse"));

        List<ScheduleDto> courseSchedule = courseService.getCourseScheduleDto(idCourse);

        return ResponseEntity.ok(courseSchedule);
    }

    @PostMapping(value = "/mainStudent/deleteCourse", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<?> deleteStudentCourse(Authentication authentication, HttpServletRequest request) {

        long idCourse = Long.parseLong(request.getParameter("idCourse"));
        String login = authentication.getName();

        if(courseService.deleteStudentAdmissionOnCourse(login, idCourse)) {

            String realPathtoUploads = userFolderService.getUserDir(login+"\\"+ idCourse);

            userFolderService.deleteUserDir(new File(realPathtoUploads));
            File courseFolder = new File(realPathtoUploads);

            return new ResponseEntity<>(
                    true,
                    HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(
                    true,
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/mainStudent/pdf", produces = "application/pdf")
    public @ResponseBody ResponseEntity<byte[]> showMaterialPdf(Authentication authentication, HttpServletRequest request) throws IOException {
        String login = authentication.getName();

        String materialPath = request.getParameter("materialPath");
        String fileType = request.getParameter("fileType");


        String dirFrom = "";
        if(fileType.equals("courseMaterial")) {
            dirFrom = "\\" + materialPath;
        }
        else {
            dirFrom = login + "\\" + materialPath;
        }

        byte[] pdfContents = null;
        try {
            pdfContents = userFolderService.readPdfFile(userFolderService.getUserDir(dirFrom));
        } catch (IOException e) {
            return new ResponseEntity<>(
                    null,
                    HttpStatus.BAD_REQUEST);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
//Here you have to set the actual filename of your pdf
        String filename = "output.pdf";
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(pdfContents, headers, HttpStatus.OK);

        return response;
    }
}

