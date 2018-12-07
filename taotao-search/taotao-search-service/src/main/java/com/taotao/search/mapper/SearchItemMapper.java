package com.taotao.search.mapper;

import com.taotao.pojo.SearchItem;

import java.util.List;

/**
 * Created by Peng on 2018/11/13.
 */
public interface SearchItemMapper {
    //获取到要导入到索引库中的数据
    List<SearchItem> getSearchItemList();

    //根据商品ID获取商品详情
    SearchItem getItemById(long itemId);
}
