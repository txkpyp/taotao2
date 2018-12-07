package com.taotao.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * 商品列表查询返回的数据类
 * Created by Peng on 2018/11/6.
 */
public class EasyUIDataGridResult implements Serializable{
    private Integer total;
    private List rows;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }
}
