package com.epam.rd.izh.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

@Service
public class UserFolderService {

    @Autowired
    private HttpServletRequest request;

    public File getUserDirFile(String login){
        String uploadsDir = "\\uploads";
        String realPathtoUploads =  request.getServletContext().getRealPath(uploadsDir);

        return new File(realPathtoUploads+"\\"+login);
    }

    public String getUserDir(String login){

        File userDirPath = getUserDirFile(login);

        if(! userDirPath.exists())
        {
            userDirPath.mkdirs();
        }

        return userDirPath.getAbsolutePath();
    }

    public void deleteUserDir(File userDir) {

        if(!userDir.exists()){
            return;
        }

        if(userDir.isDirectory()) {
            for(File file: userDir.listFiles()){
                deleteUserDir(file);
            }
        }

        userDir.delete();
    }

    public void copyMaterialToUserDir(String dirFrom, String dirTo, String fileName)
                throws IOException{
        try {
            Files.copy(
                    Paths.get( dirFrom + "\\" + fileName),
                    Paths.get(dirTo + "\\" + fileName),
                    StandardCopyOption.REPLACE_EXISTING
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clearMaterialFolder(String pathToFolder){
        File userMaterialFolder = new File(pathToFolder);

        if(userMaterialFolder.exists()) {
            for (File file : userMaterialFolder.listFiles()) {
                file.delete();
            }
        }
    }

    public void saveMultipartFileTo(String path, MultipartFile file) throws IOException {

        String orgName = file.getOriginalFilename();
        String filePath = path + "\\" + orgName;
        File dest = new File(filePath);
        file.transferTo(dest);
    }

    public byte[] readPdfFile(String pathToFile) throws IOException {
        Path path = Paths.get(pathToFile);

        return Files.readAllBytes(path);
    }
}
