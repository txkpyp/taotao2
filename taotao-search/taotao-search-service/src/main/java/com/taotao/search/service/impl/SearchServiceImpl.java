package com.taotao.search.service.impl;

import com.taotao.search.dao.SearchDao;
import com.taotao.pojo.SearchResult;
import com.taotao.search.service.SearchService;
import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Peng on 2018/11/14.
 */
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private SearchDao searchDao;

    @Override
    public SearchResult search(String queryString, int page, int rows) throws Exception {
        //根据查询条件拼接查询对象
        //创建一个SorlQuery对象
        SolrQuery query = new SolrQuery();
        //设置查询条件
        query.setQuery(queryString);
        //设置分页条件
        if (page < 1) page = 1;
        query.setStart((page - 1) * rows);
        if (rows < 1) rows = 10;
        query.setRows(rows);
        //设置默认搜索域，由于复制域查询搜索不太准确，因此建议直接使用item_title域
        query.set("df", "item_title");
        //设置高亮显示
        query.setHighlight(true);
        query.addHighlightField("item_title");
        query.setHighlightSimplePre("<em>");
        query.setHighlightSimplePost("</em>");
        //调用dao执行查询
        SearchResult searchResult = searchDao.search(query);
        //计算查询结果总页数
        long totalNumber = searchResult.getTotalNumber();
        long pages = totalNumber / rows;
        if (totalNumber % rows > 0) {
            pages++;
        }
        searchResult.setTotalPage(pages);
        return searchResult;
    }
}
