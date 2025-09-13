package com.nhohantu.tcbookbe.common.model.entity;

import com.nhohantu.tcbookbe.common.model.base.entity.BaseModel;
import com.nhohantu.tcbookbe.common.model.system.UserBasicInfoModel;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "orders")
@Entity
public class OrderModel extends BaseModel {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserBasicInfoModel user;

    private BigDecimal totalPrice;

    private String status;//todo chắc đổi qua enum
}
