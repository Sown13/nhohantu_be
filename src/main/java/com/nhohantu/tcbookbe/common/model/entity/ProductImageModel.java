package com.nhohantu.tcbookbe.common.model.entity;

import com.nhohantu.tcbookbe.common.model.base.entity.BaseModel;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "product_image")
@Entity
public class ProductImageModel extends BaseModel {

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "is_primary")
    private boolean isPrimary;//ảnh chính dùng làm ảnh bìa, có trigger khi update 1 ảnh là ảnh chính thì sẽ tự động update trường main_image_url trong bảng product

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductModel product;
}
