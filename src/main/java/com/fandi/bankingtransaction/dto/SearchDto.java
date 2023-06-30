package com.fandi.bankingtransaction.dto;

public class SearchDto {
    private int page = 1;
    private int size;
    private String sort;

    public SearchDto() {
    }

    public int getPage() {
        return this.page;
    }

    public int getSize() {
        return this.size;
    }

    public String getSort() {
        return this.sort;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
