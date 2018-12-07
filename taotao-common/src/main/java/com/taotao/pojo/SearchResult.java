package com.taotao.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Peng on 2018/11/13.
 */
public class SearchResult implements Serializable {
    //总页数
    private long totalPage;
    //总数量
    private long totalNumber;
    //查询出的商品列表
    private List<SearchItem> itemList;

    public long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(long totalPage) {
        this.totalPage = totalPage;
    }

    public long getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(long totalNumber) {
        this.totalNumber = totalNumber;
    }

    public List<SearchItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<SearchItem> itemList) {
        this.itemList = itemList;
    }
}
