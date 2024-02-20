package com.skytravel.client.lfels.controller;

import com.skytravel.client.lfels.model.OpenPathResponse;
import com.skytravel.client.lfels.service.FileAndFolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class FileAndFolderController {
    private final FileAndFolderService fileAndFolderService;

    @GetMapping("/open-path")
    public ResponseEntity<String> openFile(@RequestParam("path") String path){
        OpenPathResponse response = fileAndFolderService.openFile(path);
        return ResponseEntity.status(response.code()).body(response.message());
    }

    @PostMapping("/file")
    public void uploadFile(@RequestPart("file") MultipartFile file) {
        this.fileAndFolderService.saveFile(file);
    }

    @GetMapping("/file")
    public ResponseEntity<Resource> readFile(@RequestParam("filepath") String path) {
        try {
            Path file = Paths.get(path);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, "image/png")
                        .body(new InputStreamResource(Files.newInputStream(file)));
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found or not readable.");
            }
        } catch (MalformedURLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while processing file.", e);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while reading file.", e);
        }
    }

    @DeleteMapping("/file")
    public void deleteFile(@RequestParam("filepath") String path) {
        this.fileAndFolderService.deleteFile(path);
    }


}
