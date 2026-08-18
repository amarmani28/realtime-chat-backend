package com.chat_app.ChatApp.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/files")
@CrossOrigin("http://localhost:5173")
public class FileUploadController {

    private final String UPLOAD_DIR = "uploads/";

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }
        try {
            File directory = new File(UPLOAD_DIR);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Unique filename taaki overwrite na ho
            String originalFileName = file.getOriginalFilename();
            String uniqueFileName = UUID.randomUUID().toString() + "_" + originalFileName;
            Path path = Paths.get(UPLOAD_DIR + uniqueFileName);
            Files.write(path, file.getBytes());

            // Frontend ko file ka URL aur original name wapas bhejein
            String fileUrl = "http://localhost:8080/api/v1/files/download/" + uniqueFileName;

            return ResponseEntity.ok(Map.of(
                    "fileUrl", fileUrl,
                    "fileName", originalFileName
            ));
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error uploading file");
        }
    }

    @GetMapping("/download/{filename}")
    public ResponseEntity<byte[]> getFile(@PathVariable String filename) throws IOException {

        Path path = Paths.get(UPLOAD_DIR + filename);

        if (!Files.exists(path)) {
            return ResponseEntity.notFound().build();
        }

        byte[] fileData = Files.readAllBytes(path);

        String contentType = Files.probeContentType(path);

        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .header(
                        "Content-Disposition",
                        "attachment; filename=\"" + filename.substring(filename.indexOf("_") + 1) + "\""
                )
                .header("Content-Type", contentType)
                .body(fileData);
    }
}