package com.nhohantu.tcbookbe.model.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse {
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
