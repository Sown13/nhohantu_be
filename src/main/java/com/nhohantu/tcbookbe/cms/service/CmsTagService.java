package com.nhohantu.tcbookbe.cms.service;

import com.nhohantu.tcbookbe.cms.dto.request.CmsTagRequest;
import com.nhohantu.tcbookbe.cms.dto.response.CmsTagResponse;
import com.nhohantu.tcbookbe.cms.repository.ICmsTagRepository;
import com.nhohantu.tcbookbe.common.model.builder.ResponseBuilder;
import com.nhohantu.tcbookbe.common.model.builder.ResponseDTO;
import com.nhohantu.tcbookbe.common.model.entity.TagModel;
import com.nhohantu.tcbookbe.common.model.enums.StatusCodeEnum;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CmsTagService {

    private final ICmsTagRepository tagRepository;
    private final ModelMapper mapper;

    public ResponseEntity<ResponseDTO<List<CmsTagResponse>>> getAllTags() {
        List<TagModel> tags = tagRepository.findAll();
        List<CmsTagResponse> response = tags.stream()
                .map(tag -> mapper.map(tag, CmsTagResponse.class))
                .toList();
        return ResponseBuilder.okResponse("Success", response, StatusCodeEnum.SUCCESS2000);
    }

    public ResponseEntity<ResponseDTO<CmsTagResponse>> getTagById(Long id) {
        Optional<TagModel> tagOpt = tagRepository.findById(id);
        if (tagOpt.isEmpty()) {
            return ResponseBuilder.badRequestResponse("Tag not found", StatusCodeEnum.EXCEPTION0404);
        }
        CmsTagResponse response = mapper.map(tagOpt.get(), CmsTagResponse.class);
        return ResponseBuilder.okResponse("Success", response, StatusCodeEnum.SUCCESS2000);
    }

    public ResponseEntity<ResponseDTO<CmsTagResponse>> createTag(CmsTagRequest request) {
        TagModel tag = TagModel.builder()
                .name(request.getName())
                .build();
        tagRepository.save(tag);
        CmsTagResponse response = mapper.map(tag, CmsTagResponse.class);
        return ResponseBuilder.okResponse("Tag created successfully", response, StatusCodeEnum.SUCCESS2000);
    }

    public ResponseEntity<ResponseDTO<CmsTagResponse>> updateTag(Long id, CmsTagRequest request) {
        Optional<TagModel> tagOpt = tagRepository.findById(id);
        if (tagOpt.isEmpty()) {
            return ResponseBuilder.badRequestResponse("Tag not found", StatusCodeEnum.EXCEPTION0404);
        }
        TagModel tag = tagOpt.get();
        tag.setName(request.getName());
        tagRepository.save(tag);
        CmsTagResponse response = mapper.map(tag, CmsTagResponse.class);
        return ResponseBuilder.okResponse("Tag updated successfully", response, StatusCodeEnum.SUCCESS2000);
    }

    public ResponseEntity<ResponseDTO<String>> deleteTag(Long id) {
        if (!tagRepository.existsById(id)) {
            return ResponseBuilder.badRequestResponse("Tag not found", StatusCodeEnum.EXCEPTION0404);
        }
        tagRepository.deleteById(id);
        return ResponseBuilder.okResponse("Tag deleted successfully", "Deleted", StatusCodeEnum.SUCCESS2000);
    }
}
