package com.nhohantu.tcbookbe.common.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.nhohantu.tcbookbe.common.model.base.entity.BaseModel;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "category")
@Entity
public class CategoryModel extends BaseModel {

    @Column(name = "name", nullable = false, length = 255)
    private String name; // Tên danh mục

    @Column(name = "slug", nullable = false, unique = true, length = 255)
    private String slug; // slug thân thiện cho URL

    @Column(name = "image_url")
    private String imageUrl; // Link ảnh hoặc ID ảnh

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @JsonBackReference
    private CategoryModel parentCategory; // Danh mục cha

    @OneToMany(mappedBy = "parentCategory", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<CategoryModel> childCategories; // Danh mục con

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ProductCategoryModel> productCategories; // Liên kết sản phẩm qua bảng trung gian

    @Column(name = "category_level")
    private Integer categoryLevel; // 1, 2, 3 (sản phẩm chỉ gán level = 3)

    @Transient
    private Long productCount; // Không lưu DB — dùng khi cần đếm sản phẩm
}
