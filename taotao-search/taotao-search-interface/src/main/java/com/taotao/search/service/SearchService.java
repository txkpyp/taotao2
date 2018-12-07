package com.taotao.search.service;

import com.taotao.pojo.SearchResult;

/**
 * Created by Peng on 2018/11/14.
 */
public interface SearchService {
    //搜索
    SearchResult search(String queryString,int page,int rows) throws Exception;
}
