package com.nhohantu.tcbookbe.common.model.builder;

import com.nhohantu.tcbookbe.common.model.enums.StatusCodeEnum;
import jakarta.annotation.Nonnull;
import lombok.experimental.UtilityClass;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM;

@UtilityClass
public class ResponseBuilder {

    @Nonnull
    public static <T> ResponseEntity<ResponseDTO<T>> okResponse(String message, StatusCodeEnum statusCode) {
        final ResponseDTO<T> dto = ResponseDTO.<T>
                        builder()
                .success(true)
                .message(message)
                .statusCode(statusCode.toString())
                .build();
        return ResponseEntity.ok(dto);
    }

    public static <T> ResponseEntity<ResponseDTO<T>> okResponse(String message, @Nonnull T body, StatusCodeEnum statusCode) {
        final ResponseDTO<T> dto = ResponseDTO.<T>
                        builder()
                .success(true)
                .message(message)
                .data(body)
                .statusCode(statusCode.toString())
                .build();
        return ResponseEntity.ok(dto);
    }

    public static <T> ResponseEntity<ResponseDTO<T>> internalErrorResponse(@Nonnull T body, StatusCodeEnum statusCode) {
        final ResponseDTO<T> dto = ResponseDTO.<T>
                        builder()
                .success(false)
                .data(body)
                .statusCode(statusCode.toString())
                .build();
        return ResponseEntity.ok(dto);
    }

    public static <T> ResponseEntity<ResponseDTO<T>> badRequestResponse(String message, StatusCodeEnum statusCode) {
        final ResponseDTO<T> dto = ResponseDTO.<T>
                        builder()
                .success(false)
                .message(message)
                .statusCode(statusCode.toString())
                .build();
        return ResponseEntity.ok(dto);
    }

    public static <T> ResponseEntity<ResponseDTO<T>> badRequestResponse(String message, @Nonnull T body, StatusCodeEnum statusCode) {
        final ResponseDTO<T> dto = ResponseDTO.<T>
                        builder()
                .success(false)
                .message(message)
                .data(body)
                .statusCode(statusCode.toString())
                .build();
        return ResponseEntity.ok(dto);
    }

    @Nonnull
    public static <T> ResponseEntity<ResponseDTO<T>> createdResponse(@Nonnull T body, StatusCodeEnum statusCode) {
        final ResponseDTO<T> dto = ResponseDTO.<T>
                        builder()
                .success(true)
                .data(body)
                .statusCode(statusCode.toString())
                .build();
        return ResponseEntity.ok(dto);
    }

    @Nonnull
    public static ResponseEntity<InputStreamResource> writeInputStreamResource(
            @Nonnull InputStreamResource resource,
            @Nonnull String csvFileName) {
        return ResponseEntity.ok()
                .contentType(APPLICATION_OCTET_STREAM)
                .header(CONTENT_DISPOSITION, String.format("attachment; filename=invoices-%s.csv", csvFileName))
                .body(resource);
    }

    @Nonnull
    public static <T> ResponseEntity<ResponseDTO<T>> okResponse(String message, @Nonnull T body, StatusCodeEnum statusCode, MetaData metaData) {
        final ResponseDTO<T> dto = ResponseDTO.<T>
                        builder()
                .success(true)
                .message(message)
                .data(body)
                .statusCode(statusCode.toString())
                .metaData(metaData)
                .build();
        return ResponseEntity.ok(dto);
    }

    @Nonnull
    public static <T> ResponseEntity<ResponseDTO<T>> createdResponse(@Nonnull T body, StatusCodeEnum statusCode, MetaData metaData) {
        final ResponseDTO<T> dto = ResponseDTO.<T>
                        builder()
                .success(true)
                .data(body)
                .statusCode(statusCode.toString())
                .metaData(metaData)
                .build();
        return ResponseEntity.ok(dto);
    }

    @Nonnull
    public static <T> ResponseEntity<ResponseDTO<T>> BadRequestResponse(@Nonnull T body, StatusCodeEnum statusCode) {
        final ResponseDTO<T> dto = ResponseDTO.<T>
                        builder()
                .success(false)
                .data(body)
                .statusCode(statusCode.toString())
                .build();
        return ResponseEntity.ok(dto);
    }
}