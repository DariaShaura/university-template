package com.epam.rd.izh.controller;

import com.epam.rd.izh.dto.*;
import com.epam.rd.izh.entity.Course;
import com.epam.rd.izh.service.*;
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

public class MainPageAdminController {

    @Autowired
    UserService userService;

    @Autowired
    ActiveUserService activeUserService;

    @Autowired
    UserFolderService userFolderService;

    @GetMapping("/mainAdmin")
    public String mainAdmin(Authentication authentication, Model model) {

        String login = authentication.getName();

        model.addAttribute("login", login);

        return "mainAdmin";
    }

    @PostMapping(value="/mainAdmin",produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<?> getUsers(Authentication authentication) {

        String login = authentication.getName();

        List<String> activeUsers = activeUserService.getAllActiveUser();

        List<ActiveUserDto> activeUserDtoList = userService.getAllAuthorizedUsers(login);

        activeUserDtoList.stream().forEach(user -> {if(activeUsers.contains(user.getLogin())){user.setActive(true);};});


        return ResponseEntity.ok(activeUserDtoList);
    }

    @PostMapping(value="/mainAdmin/deleteUser",produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<?> deleteUser(HttpServletRequest request) {

        long idUser = Long.parseLong(request.getParameter("idUser"));
        String loginUser = request.getParameter("login");

        if(!userService.deleteUser(idUser)){
            return new ResponseEntity<>(
                    false,
                    HttpStatus.BAD_REQUEST);
        }
        else{
            // удалить все файлы на сервере
            File userDir = userFolderService.getUserDirFile(loginUser);

            userFolderService.deleteUserDir(userDir);
        }

        return new ResponseEntity<>(
                true,
                HttpStatus.OK);
    }

}

