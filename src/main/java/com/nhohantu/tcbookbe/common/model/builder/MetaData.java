package com.nhohantu.tcbookbe.common.model.builder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**Start page = 0,
 * Default size = 10*/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MetaData {
    private int totalPage;
    private int currentPage;
    private int pageSize;
    private int totalItems;
}
