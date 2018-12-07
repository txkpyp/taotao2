package com.taotao.search.dao.impl;

import com.taotao.search.dao.SearchDao;
import com.taotao.pojo.SearchItem;
import com.taotao.pojo.SearchResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Peng on 2018/11/13.
 */
@Repository
public class SearchDaoImpl implements SearchDao {

    @Autowired
    private SolrServer solrServer;

    @Override
    public SearchResult search(SolrQuery query) throws Exception {
        //根据query对象进行查询
        QueryResponse response = solrServer.query(query);
        //取查询结果
        SolrDocumentList solrDocumentList = response.getResults();
        //去查询总记录数
        long numFound = solrDocumentList.getNumFound();
        //初始化一个SolrResult对象并把总记录数进行赋值
        SearchResult searchResult = new SearchResult();
        searchResult.setTotalNumber(numFound);
        List<SearchItem> itemList = new ArrayList<>();
        //把查询结果封装SearchItem当中
        for (SolrDocument document : solrDocumentList) {
            SearchItem searchItem = new SearchItem();
            searchItem.setId((String) document.get("id"));
            String image= (String) document.get("item_image");
            if (!StringUtils.isBlank(image)){
                image=image.split(",")[0];
            }
            searchItem.setImage(image);
            searchItem.setItem_category_name((String) document.get("item_category_name"));
            searchItem.setItem_desc((String) document.get("item_desc"));
            searchItem.setPrice((Long) document.get("item_price"));
            searchItem.setSell_point((String) document.get("item_sell_point"));

            //取高亮显示
            Map<String, Map<String, List<String>>> hightlighting = response.getHighlighting();
            List<String> list = hightlighting.get(document.get("id")).get("item_title");
            String itemTitle = "";
            if (list != null && list.size() > 0) {
                itemTitle = list.get(0);
            } else {
                itemTitle = (String) document.get("item_title");
            }
            searchItem.setTitle(itemTitle);
            itemList.add(searchItem);
        }
        //把结果添加到SearchResult当中
        searchResult.setItemList(itemList);
        //返回
        return searchResult;
    }
}
