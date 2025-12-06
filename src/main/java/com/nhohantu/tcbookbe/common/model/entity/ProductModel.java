package com.nhohantu.tcbookbe.common.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.nhohantu.tcbookbe.business.converter.JsonConverter;
import com.nhohantu.tcbookbe.common.model.base.entity.BaseModel;
import com.nhohantu.tcbookbe.common.utils.Constant;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    private String name;

    @Column(name = "slug", columnDefinition = "VARCHAR(500)", nullable = false, unique = true)
    private String slug;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "price", columnDefinition = "DECIMAL(10,0)")
    private BigDecimal price;

    @Column(name = "sale_price", columnDefinition = "DECIMAL(10,0)")
    private BigDecimal salePrice;

    @Column(name = "quantity", columnDefinition = "INT")
    @Min(0)
    @Builder.Default
    private Integer quantity = 0;

    @Column(name = "sold", columnDefinition = "INT")
    @Builder.Default
    private Integer sold = 0;

    @Column(name = "active", columnDefinition = "TINYINT(1)")
    private Boolean active;

    @Column(name = "main_image_url", columnDefinition = "VARCHAR(1000)")

    private String mainImageUrl = Constant.DEFAULT_IMAGE_URL;

    @Column(name = "video_url", columnDefinition = "VARCHAR(1000)")
    private String videoUrl;

    @Column(name = "unit", columnDefinition = "VARCHAR(50)")
    private String unit;

    @Column(name = "sku", columnDefinition = "VARCHAR(255)")
    private String sku;

    @Column(name = "brand", columnDefinition = "VARCHAR(255)")
    private String brand;

    @Column(name = "weight", columnDefinition = "FLOAT")
    private Float weight;

    @Column(name = "discount_percentage", columnDefinition = "FLOAT")
    private Float discountPercentage;

    @Column(name = "rating", columnDefinition = "FLOAT")
    private Float rating;

    @Column(name = "variations", columnDefinition = "JSON")
    @Convert(converter = JsonConverter.class)
    private Map<String, Object> variations;

    // --- Relationships ---
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ProductCategoryModel> productCategories = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImageModel> productImages = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "product_tag",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<TagModel> tags = new ArrayList<>();
}
