package com.nhohantu.tcbookbe.common.model.entity;

import com.nhohantu.tcbookbe.common.model.base.entity.BaseModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewModel extends BaseModel {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductModel product;

    @Column(name = "author_name", columnDefinition = "VARCHAR(100)", nullable = false)
    @NotBlank(message = "Tên tác giả không được để trống")
    @Size(max = 100, message = "Tên tác giả không được vượt quá 100 ký tự")
    private String authorName;

    @Column(name = "author_email", columnDefinition = "VARCHAR(255)", nullable = false)
    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    @Size(max = 255, message = "Email không được vượt quá 255 ký tự")
    private String authorEmail;

    @Column(name = "rating", columnDefinition = "INT", nullable = false)
    @NotNull(message = "Rating không được để trống")
    @Min(value = 1, message = "Rating tối thiểu là 1")
    @Max(value = 5, message = "Rating tối đa là 5")
    private Integer rating;

    @Column(name = "title", columnDefinition = "VARCHAR(255)")
    @Size(max = 255, message = "Tiêu đề không được vượt quá 255 ký tự")
    private String title;

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    @NotBlank(message = "Nội dung đánh giá không được để trống")
    private String description;
}
