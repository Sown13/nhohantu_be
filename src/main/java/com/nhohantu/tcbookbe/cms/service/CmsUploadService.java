package com.nhohantu.tcbookbe.cms.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
public class CmsUploadService {

    @Value("${app.upload.dir}")
    private String uploadDir;

    public Map<String, Object> upload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File is empty");
        }

        String contentType = file.getContentType();
        Set<String> allowed = Set.of("image/jpeg", "image/png", "image/webp");
        if (contentType == null || !allowed.contains(contentType)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only JPG/PNG/WebP allowed");
        }

        long maxBytes = 5L * 1024 * 1024;
        if (file.getSize() > maxBytes) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File too large (max 5MB)");
        }

        String original = StringUtils.cleanPath(
                Objects.requireNonNullElse(file.getOriginalFilename(), "file")
        );
        String ext = getExt(original).orElseGet(() -> mimeToExt(contentType));

        String filename = UUID.randomUUID() + ext;

        Path dir = Paths.get(uploadDir).toAbsolutePath().normalize();
        Path target = dir.resolve(filename).normalize();

        try {
            Files.createDirectories(dir);

            if (!target.startsWith(dir)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid path");
            }

            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Upload failed");
        }

        String url = "/static/" + filename;

        return Map.of(
                "fileName", filename,
                "contentType", contentType,
                "size", file.getSize(),
                "url", url
        );
    }

    private Optional<String> getExt(String filename) {
        int dot = filename.lastIndexOf('.');
        if (dot < 0) return Optional.empty();
        String ext = filename.substring(dot).toLowerCase(Locale.ROOT);
        if (ext.length() > 5) return Optional.empty();
        return Optional.of(ext);
    }

    private String mimeToExt(String mime) {
        return switch (mime) {
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            case "image/webp" -> ".webp";
            default -> "";
        };
    }
}