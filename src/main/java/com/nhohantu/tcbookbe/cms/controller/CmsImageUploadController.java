package com.nhohantu.tcbookbe.cms.controller;

import com.nhohantu.tcbookbe.cms.service.CmsUploadService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cms/images")
public class CmsImageUploadController {

    private final CmsUploadService uploadImageService;

    public CmsImageUploadController(CmsUploadService imageService) {
        this.uploadImageService = imageService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String, Object> upload(@RequestPart("file") MultipartFile file) {
        return uploadImageService.upload(file);
    }

    @PostMapping(value = "/multi-upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<Map<String, Object>> uploadMany(@RequestPart("files") MultipartFile[] files) {
        return Arrays.stream(files)
                .map(uploadImageService::upload)
                .toList();
    }
}
