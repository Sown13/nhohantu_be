package com.nhohantu.tcbookbe.business.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderMailItem {
    private String productName;
    private Integer quantity;
    private BigDecimal price;
}
