package com.nhohantu.tcbookbe.cms.dto.request;

import lombok.Data;

@Data
public class AttachmentRequest {
    private String url;
    private String name;
    private boolean isPrimary; // FE chọn ảnh chính
}
