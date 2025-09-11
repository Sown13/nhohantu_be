package com.nhohantu.tcbookbe.model.entity;

import com.nhohantu.tcbookbe.model.base.entity.BaseModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "product")
@Entity
public class ProductModel extends BaseModel {
    @Column(name = "name", columnDefinition = "VARCHAR(500)", nullable = false)
    private String name;//tên sp

    @Column(name = "desciption", columnDefinition = "VARCHAR(255)")
    private String desciption;//mô tả sp

    @Column(name = "cover_url", columnDefinition = "VARCHAR(1000)")
    private String coverUrl;//ảnh bìa sp

    @Column(name = "price", columnDefinition = "DECIMAL(10,0)")
    private BigDecimal price;//giá sp

    @Column(name = "quantity", columnDefinition = "INT")
    @Min(0)
    @Builder.Default
    private Integer quantity = 0;//tồn kho (nếu active = true thì kể cả hết hàng sẽ hiển thị "còn hàng")

    @Column(name = "active", columnDefinition = "TINYINT(1)")
    private Boolean active;// đang bán hay không (tắt bật sản phẩm kể cả khi còn/hết hàng)
}
