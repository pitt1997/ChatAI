package com.lijs.nex.common.web.pager;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * @author ljs
 * @date 2025-01-14
 * @description
 */
@JsonIgnoreProperties({"current", "orders", "records"}) // 忽略返回的字段
public class BasePage<T> extends Page<T> {

    private long page = 0;

    private long size = 10;

    private List<String> selectIds;

    /**
     * 模糊查询字段
     */
    private String likeQuery;

    public long getPage() {
        return page;
    }

    public void setPage(long page) {
        this.page = page;
        setCurrent(page);
    }

    public List<T> getData() {
        return getRecords();
    }

    public String getLikeQuery() {
        return likeQuery;
    }

    public void setLikeQuery(String likeQuery) {
        this.likeQuery = likeQuery;
    }

    @Override
    public long getSize() {
        return size;
    }

    @Override
    public BasePage<T> setSize(long size) {
        super.setSize(size);
        this.size = size;
        return this;
    }

    @Override
    public long offset() {
        long page = getPage();
        if (page <= 0L) {
            return 0L;
        }
        return page * getSize();
    }

    public List<String> getSelectIds() {
        return selectIds;
    }

    public void setSelectIds(List<String> selectIds) {
        this.selectIds = selectIds;
    }
}