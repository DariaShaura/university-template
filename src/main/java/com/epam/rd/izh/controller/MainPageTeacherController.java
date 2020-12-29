package com.epam.rd.izh.controller;

import com.epam.rd.izh.dto.*;
import com.epam.rd.izh.entity.Course;
import com.epam.rd.izh.entity.Material;
import com.epam.rd.izh.entity.Schedule;
import com.epam.rd.izh.entity.Theme;
import com.epam.rd.izh.exception.IncorrectDataException;
import com.epam.rd.izh.service.CourseService;
import com.epam.rd.izh.service.UserFolderService;
import com.epam.rd.izh.service.UserService;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class MainPageTeacherController {

    @Autowired
    CourseService courseService;

    @Autowired
    UserService userService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    UserFolderService userFolderService;

    @GetMapping("/mainTeacher/courseAdd")
    public String addCourse(Authentication authentication, Model model) {

        model.addAttribute("login", authentication.getName());

        return "teacherCourseAdd";
    }

    @PostMapping(value = "/mainStudent/courseAdd/upload")
    public @ResponseBody ResponseEntity<?> courseUploadFile(Authentication authentication,
                                                               @RequestParam("file") MultipartFile file, @RequestParam("idMaterial") String idMaterial,
                                                               @RequestParam("idCourse") String idCourse, @RequestParam("tempFile") boolean tempFile) {
        if (!file.isEmpty()) {
            try {
                String login = authentication.getName();

                String realPathtoUploads = "";

                if(tempFile){
                    realPathtoUploads = userFolderService.getUserDir(login+"\\tempCourse");
                }
                else {
                    realPathtoUploads = userFolderService.getUserDir(login + "\\" + idCourse + "\\" + idMaterial);

                    //delete file from the realPathtoUploads
                    for (File oldLab : new File(realPathtoUploads).listFiles())
                        if (oldLab.isFile()) oldLab.delete();
                }

                String orgName = file.getOriginalFilename();
                String filePath = realPathtoUploads + "\\" + orgName;
                File dest = new File(filePath);
                file.transferTo(dest);
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

    @PostMapping(value = "/mainTeacher/courseAdd/proceed", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ResponseEntity<?> addCourseProceed(@Valid @RequestBody CourseDto courseDto, Errors errors) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String login = auth.getName();

        courseDto.setTeacherLogin(login);

        //Course course = courseService.getCourse(courseDto);

        try {
            courseDto = courseService.addCourse(courseDto);

            String realPathtoUploads = userFolderService.getUserDir(login);

            long idCourse = courseDto.getId();

                courseDto.getThemes().stream().forEach(p -> p.getMaterials().stream().forEach(m ->
                {
                    try {
                        File directory = new File(realPathtoUploads + "\\" + idCourse + "\\" + m.getId());
                        if (! directory.exists()){
                            directory.mkdirs();
                        }
                        Files.copy(
                                Paths.get( realPathtoUploads + "\\tempCourse\\" + m.getPath()),
                                Paths.get(realPathtoUploads + "\\" + idCourse + "\\" + m.getId() + "\\" + m.getPath()),
                                StandardCopyOption.REPLACE_EXISTING
                        );
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }));

            for (File file : new File(userFolderService.getUserDir(login+"\\tempCourse")).listFiles())
                if (file.isFile()) file.delete();

            return ResponseEntity.ok(courseDto.getId());
        }
        catch (IncorrectDataException e){
            return new ResponseEntity<>(
                    false,
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/mainTeacher/course")
    public String coursePage(Authentication authentication, Model model) {

        model.addAttribute("login", authentication.getName());

        return "teacherCourse";
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
    ResponseEntity<?> deleteCourse(Authentication authentication, HttpServletRequest request) {

        long idCourse = Long.parseLong(request.getParameter("idCourse"));

        String login = authentication.getName();

        boolean delete = courseService.deleteCourse(idCourse);

        userFolderService.deleteUserDir(userFolderService.getUserDirFile(login + "\\" + idCourse));

        // отправить класс CourseDTO в ответе на Post-запрос
        return ResponseEntity.ok(delete);
    }

    @PostMapping(value = "/mainTeacher/course/edit", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ResponseEntity<?> editCourse(Authentication authentication, @Valid @RequestBody CourseDto courseDto, Errors errors) {

        String login = authentication.getName();

        try {
            CourseDto updatedCourseDto = courseService.updateCourseThemesMaterials(courseDto);

            userFolderService.deleteUserDir(userFolderService.getUserDirFile(login + "\\" + updatedCourseDto.getId()));

            for(ThemeDto themeDto: updatedCourseDto.getThemes()){
                for(MaterialDto materialDto: themeDto.getMaterials()){
                    userFolderService.copyMaterialToUserDir(userFolderService.getUserDir(login +"\\tempCourse"),
                            userFolderService.getUserDir(login +"\\" +updatedCourseDto.getId() + "\\"+ materialDto.getId()),
                            materialDto.getPath());
                }
            }

            return ResponseEntity.ok(updatedCourseDto);
        }
        catch (IncorrectDataException e) {
            return new ResponseEntity<>(
                    false,
                    HttpStatus.BAD_REQUEST);
        }
        catch (IOException e) {
            return new ResponseEntity<>(
                    false,
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/mainTeacher/course/edit/upload")
    public @ResponseBody ResponseEntity<?> editCourseUploadFile(Authentication authentication, @RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                String login = authentication.getName();

                String realPathtoUploads =  userFolderService.getUserDir(login);

                String orgName = file.getOriginalFilename();
                String filePath = realPathtoUploads + orgName;
                File dest = new File(filePath);
                file.transferTo(dest);
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

    @PostMapping(value = "/mainTeacher/course/participantsAttendence", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<?> getCourseParticipants(HttpServletRequest request){
        long idCourse = Long.parseLong(request.getParameter("idCourse"));

        List<ParticipantDto> courseParticipantList = courseService.getCourseParticipants(idCourse);

        return ResponseEntity.ok(courseParticipantList);
    }

    @PostMapping(value = "/mainTeacher/course/participantsAttendence/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<?> updateCourseParticipantsAttendence(@Valid @RequestBody List<ParticipantDto> participantDtoList){

        courseService.updateCourseAttendence(participantDtoList);

        return new ResponseEntity<>(
                true,
                HttpStatus.OK);
    }

    @PostMapping(value = "/mainTeacher/course/marks", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<?> getCourseMarks(HttpServletRequest request){
        long idCourse = Long.parseLong(request.getParameter("idCourse"));

        List<MarkDto> courseMarkList = courseService.getCourseMarks(idCourse);

        return ResponseEntity.ok(courseMarkList);
    }

    @PostMapping(value = "/mainTeacher/course/marks/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<?> updateCourseMarks(@Valid @RequestBody List<MarkDto> markDtoList){

        try{
            courseService.updateCourseMarks(markDtoList);

            return ResponseEntity.ok(markDtoList);
        }
        catch (IncorrectDataException e){
            long incorrectMarkId = ((MarkDto)(e.getIncorrectObject())).getId();

            return new ResponseEntity<>(
                    incorrectMarkId,
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/mainTeacher/pdf", produces = "application/pdf")
    public @ResponseBody ResponseEntity<byte[]> showMaterialPdf(Authentication authentication, HttpServletRequest request) throws IOException {
        String login = authentication.getName();
        String materialPath = request.getParameter("materialPath");

        byte[] pdfContents = null;
        try {
            pdfContents = userFolderService.readPdfFile(userFolderService.getUserDir(login+"\\"+materialPath));
        } catch (IOException e) {
            return new ResponseEntity<>(
                    null,
                    HttpStatus.BAD_REQUEST);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));

        String filename = "output.pdf";
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(pdfContents, headers, HttpStatus.OK);

        return response;
    }

    @PostMapping(value = "/mainTeacher/studentLab/open", produces = "application/pdf")
    public @ResponseBody ResponseEntity<byte[]> showStudentLabPdf(Authentication authentication, HttpServletRequest request) throws IOException {

        long idStudent = Long.parseLong(request.getParameter("idStudent"));
        String studentLogin = userService.getAuthorizedUserLogin(idStudent);
        String idLab = request.getParameter("idLab");
        String idCourse = request.getParameter("idCourse");
        String labPath = request.getParameter("labPath");

        byte[] pdfContents = null;
        try {
            pdfContents = userFolderService.readPdfFile(userFolderService.getUserDir(studentLogin+"\\"+idCourse+"\\"+idLab+"\\"+labPath));
        } catch (IOException e) {
            return new ResponseEntity<>(
                    null,
                    HttpStatus.BAD_REQUEST);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));

        String filename = "output.pdf";
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(pdfContents, headers, HttpStatus.OK);

        return response;
    }

}
