package com.nhohantu.tcbookbe.common.model.entity;

import com.nhohantu.tcbookbe.common.model.base.entity.BaseModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/** Bảng trung gian - chi tiết 1 đơn hàng */
@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "order_detail")
@Entity
public class OrderDetailModel extends BaseModel {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private OrderModel order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductModel product;

    @Column(name = "price", columnDefinition = "DECIMAL(10,0)")
    private BigDecimal price;

    @Column(name = "quantity", columnDefinition = "INT")
    private Integer quantity;

    @Column(name = "product_name", columnDefinition = "VARCHAR(500)")
    private String productName;

    @Column(name = "product_image", columnDefinition = "VARCHAR(1000)")
    private String productImage;
}
