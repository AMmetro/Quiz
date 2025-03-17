package com.example.demo.utils.pagination;

import org.springframework.web.bind.annotation.RequestParam;

public class SortQueryUtils {
    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_SIZE = 10;
    private static final String DEFAULT_SORT_BY = "createdAt";
    private static final String DEFAULT_SORT_DIRECTION = "desc";

    public static PaginationParameters getPaginationParameters(Integer pageNumber, Integer pageSize, String sortBy, String sortDirection) {
        int validatedPage = (pageNumber == null || pageNumber < 1) ? DEFAULT_PAGE : pageNumber;
        int validatedSize = (pageSize == null || pageSize <= 0) ? DEFAULT_SIZE : pageSize;
        String validatedSortBy = (sortBy == null ) ? DEFAULT_SORT_BY : sortBy;
        String validatedSortDirection = (sortDirection == null ) ? DEFAULT_SORT_DIRECTION : sortDirection;

        return new PaginationParameters(validatedPage, validatedSize, validatedSortBy, validatedSortDirection );
    }

}

