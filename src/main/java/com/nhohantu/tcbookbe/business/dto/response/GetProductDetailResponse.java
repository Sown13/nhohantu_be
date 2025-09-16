package com.nhohantu.tcbookbe.business.dto.response;

import com.nhohantu.tcbookbe.business.dto.CategoryDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetProductDetailResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String mainImageUrl;
    private Boolean active;
    private Integer quantity;
    private List<CategoryDTO> categories;
}
