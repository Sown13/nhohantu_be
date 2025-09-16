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
public class GetProductListResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private String mainImageUrl;
    private Boolean active;
    private List<CategoryDTO> categories;
}