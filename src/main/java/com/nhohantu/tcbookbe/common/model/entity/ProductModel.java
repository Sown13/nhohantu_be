package com.nhohantu.tcbookbe.common.model.entity;

import com.nhohantu.tcbookbe.common.model.base.entity.BaseModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "product")
@Entity
public class ProductModel extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", columnDefinition = "VARCHAR(500)", nullable = false)
    private String name;//tên sp

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;//mô tả sp

    @Column(name = "price", columnDefinition = "DECIMAL(10,0)")
    private BigDecimal price;//giá sp

    @Column(name = "quantity", columnDefinition = "INT")
    @Min(0)
    @Builder.Default
    private Integer quantity = 0;//tồn kho (nếu active = true thì kể cả hết hàng sẽ hiển thị "còn hàng")

    @Column(name = "active", columnDefinition = "TINYINT(1)")
    private Boolean active;// đang bán hay không (tắt bật sản phẩm kể cả khi còn/hết hàng)

    @Column(name = "main_image_url", columnDefinition = "VARCHAR(1000)")
    private String mainImageUrl; //ảnh bìa sp

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductCategoryModel> productCategories = new ArrayList<>();
}
