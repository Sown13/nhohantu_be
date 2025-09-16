package com.nhohantu.tcbookbe.common.utils;

import jakarta.annotation.Nullable;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.HashMap;
import java.util.Map;

/**Quy ước: đầu vào page là 1-based, convert thành 0-based (mặc định Pageable của Spring)*/
@UtilityClass
public class PagingValidationUtil {
    private final int MAX_SIZE = 100;
    private final int DEFAULT_SIZE = 10;

    /**
     * Điều chỉnh các giá trị page và size sao cho hợp lệ.
     *
     * @param page số trang client gửi (1-based).
     * @param size số lượng phần tử trên mỗi trang, phải >= 1 và không vượt quá maxSize.
     * @return Map với page (0-based, dùng cho Spring Data) và size đã được điều chỉnh
     */
    public Map<String, Integer> correctPagingInput(@Nullable Integer page, @Nullable Integer size) {
        Map<String, Integer> correctedPaging = new HashMap<>();

        // Nếu null hoặc < 1 thì set về 1
        if (page == null || page < 1) {
            page = 1;
        }

        // Convert về 0-based cho PageRequest
        int springPage = page - 1;

        // Validate size
        if (size == null) {
            size = DEFAULT_SIZE;
        }
        if (size < 1) {
            size = 1;
        } else if (size > MAX_SIZE) {
            size = MAX_SIZE;
        }

        correctedPaging.put("page", springPage);
        correctedPaging.put("size", size);

        return correctedPaging;
    }

    /**
     * Điều chỉnh page/size và tạo Pageable.
     *
     * @param page          số trang client gửi (1-based).
     * @param size          số lượng phần tử trên mỗi trang.
     * @param sortBy        cột sort.
     * @param sortDirection hướng sort ("asc" hoặc "desc").
     * @return Pageable hợp lệ cho Spring Data (0-based).
     */
    public Pageable createPageable(@Nullable Integer page,
                                   @Nullable Integer size,
                                   @NonNull String sortBy,
                                   @NonNull String sortDirection) {
        // normalize page
        if (page == null || page < 1) {
            page = 1;
        }
        int springPage = page - 1; // convert về 0-based

        // normalize size
        if (size == null) {
            size = DEFAULT_SIZE;
        }
        if (size < 1) {
            size = 1;
        } else if (size > MAX_SIZE) {
            size = MAX_SIZE;
        }

        // sort
        Sort sort = sortDirection.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        return PageRequest.of(springPage, size, sort);
    }
}

