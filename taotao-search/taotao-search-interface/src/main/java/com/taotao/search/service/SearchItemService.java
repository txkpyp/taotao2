package com.taotao.search.service;

import com.taotao.pojo.TaotaoResult;

/**
 * Created by Peng on 2018/11/13.
 */
public interface SearchItemService {
    //将数据导入索引库
    TaotaoResult importItemToIndex();
}
