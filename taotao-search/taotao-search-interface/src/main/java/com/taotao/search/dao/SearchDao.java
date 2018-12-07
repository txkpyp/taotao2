package com.taotao.search.dao;

import com.taotao.pojo.SearchResult;
import org.apache.solr.client.solrj.SolrQuery;

/**
 * Created by Peng on 2018/11/13.
 */
public interface SearchDao {
    SearchResult search(SolrQuery query) throws Exception;
}
