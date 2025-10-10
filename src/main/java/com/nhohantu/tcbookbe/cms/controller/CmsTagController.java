package com.nhohantu.tcbookbe.cms.controller;

import com.nhohantu.tcbookbe.cms.dto.request.CmsTagRequest;
import com.nhohantu.tcbookbe.cms.dto.response.CmsTagResponse;
import com.nhohantu.tcbookbe.cms.service.CmsTagService;
import com.nhohantu.tcbookbe.common.model.builder.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cms/tags")
@RequiredArgsConstructor
public class CmsTagController {

    private final CmsTagService cmsTagService;

    @GetMapping
    public ResponseEntity<ResponseDTO<List<CmsTagResponse>>> getAllTags() {
        return cmsTagService.getAllTags();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<CmsTagResponse>> getTagById(@PathVariable Long id) {
        return cmsTagService.getTagById(id);
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<CmsTagResponse>> createTag(@RequestBody CmsTagRequest request) {
        return cmsTagService.createTag(request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<CmsTagResponse>> updateTag(@PathVariable Long id, @RequestBody CmsTagRequest request) {
        return cmsTagService.updateTag(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<String>> deleteTag(@PathVariable Long id) {
        return cmsTagService.deleteTag(id);
    }
}
