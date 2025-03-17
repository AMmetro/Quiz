package com.example.demo.utils.pagination;

import org.springframework.web.bind.annotation.RequestParam;

public class PaginationParameters {
    private final int pageNumber;
    private final int pageSize;
    private final String sortBy;
    private final String sortDirection;

    public PaginationParameters(int page, int size, String sortBy, String sortDirection) {
        this.pageNumber = page;
        this.pageSize = size;
        this.sortBy = sortBy;
        this.sortDirection = sortDirection;
    }

    public int getPage() {
        return pageNumber;
    }

    public int getSize() {
        return pageSize;
    }

    public String getSortDirection() {
        return sortDirection;
    }

    public String getSortBy() {
        return sortBy;
    }
}


