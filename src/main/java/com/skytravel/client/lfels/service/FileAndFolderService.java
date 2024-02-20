package com.skytravel.client.lfels.service;

import com.skytravel.client.lfels.model.OpenPathResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;

@Service
public class FileAndFolderService {
    public OpenPathResponse openFile(String path) {
        try {
            File resource = new File(path);
            if (!resource.exists()) {
                return new OpenPathResponse(404, "Path not found:" + path);
            }
            Desktop desktop = Desktop.getDesktop();
            desktop.open(resource);
            return new OpenPathResponse(200, "Command run success:" + path);
        } catch (IOException e) {
            return new OpenPathResponse(500, "Command run failed:" + path + "\nMessage:" + Arrays.toString(e.getStackTrace()));
        }
    }


    public void saveFile(MultipartFile file) {
        try {
            Path filePath = Paths.get(Objects.requireNonNull(file.getOriginalFilename()));
            file.transferTo(filePath);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while saving file.", e);
        }
    }


    public void deleteFile(String filename) {
        try {
            Path file = Paths.get(filename);
            Files.deleteIfExists(file);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while deleting file.", e);
        }
    }
}