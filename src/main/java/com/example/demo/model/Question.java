package com.example.demo.model;

import com.example.demo.entity.QuestionEntity;

import java.util.List;

public class Question {


    private int pagesCount;
    private int page;
    private int pageSize;
    private Long totalCount;
    private List<QuestionEntity> items;

    public Question(int pagesCount, int page, int pageSize, Long totalCount, List<QuestionEntity> items) {
        this.pagesCount = pagesCount;
        this.page = page;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
        this.items = items;
    }

    public int getPagesCount() {
        return pagesCount;
    }

    public void setPagesCount(int pagesCount) {
        this.pagesCount = pagesCount;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public List<QuestionEntity> getItems() {
        return items;
    }

    public void setItems(List<QuestionEntity> items) {
        this.items = items;
    }

}




