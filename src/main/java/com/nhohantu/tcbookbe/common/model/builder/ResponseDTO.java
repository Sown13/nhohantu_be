package com.nhohantu.tcbookbe.common.model.builder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDTO<T> {
    private boolean success;
    private String message;
    private T data;
    private String statusCode;
    private MetaData metaData;
}
