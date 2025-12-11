package com.nhohantu.tcbookbe.business.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewRequest {
    @NotNull(message = "Product ID không được để trống")
    private Long productId;

    @NotBlank(message = "Tên không được để trống")
    @Size(min = 2, max = 100, message = "Tên phải dài từ 2 đến 100 ký tự")
    private String name;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Định dạng email không hợp lệ")
    private String email;

    @NotBlank(message = "Nội dung đánh giá không được để trống")
    @Size(min = 10, message = "Nội dung đánh giá phải dài ít nhất 10 ký tự")
    private String message;

    @NotNull(message = "Rating không được để trống")
    @Min(value = 1, message = "Rating phải từ 1 sao trở lên")
    @Max(value = 5, message = "Rating tối đa là 5 sao")
    private Integer rating;
}
