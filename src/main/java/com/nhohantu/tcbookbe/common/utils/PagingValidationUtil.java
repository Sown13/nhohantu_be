package com.nhohantu.tcbookbe.common.utils;

import jakarta.annotation.Nullable;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class PagingValidationUtil {
    private final int MAX_SIZE = 100;
    private final int DEFAULT_SIZE = 10;

    /**
     * Điều chỉnh các giá trị page và size sao cho hợp lệ.
     *
     * @param page số trang, phải >= 1.
     * @param size số lượng phần tử trên mỗi trang, phải >= 1 và không vượt quá maxSize.
     * @return Map với page và size đã được điều chỉnh (lấy ra theo key "page" và "size")
     */
    public Map<String, Integer> correctPagingInput(@Nullable Integer page,@Nullable Integer size) {
        Map<String, Integer> correctedPaging = new HashMap<>();

        // Điều chỉnh page nếu nhỏ hơn 1
        if (page == null || page < 1) {
            page = 1;
        }

        // Điều chỉnh size nếu nhỏ hơn 1, và không vượt quá MAX_SIZE
        if (size == null) {
            size = DEFAULT_SIZE;
        }

        if (size < 1) {
            size = 1;
        } else if (size > MAX_SIZE) {
            size = MAX_SIZE;
        }

        correctedPaging.put("page", page);
        correctedPaging.put("size", size);

        return correctedPaging;
    }
}
