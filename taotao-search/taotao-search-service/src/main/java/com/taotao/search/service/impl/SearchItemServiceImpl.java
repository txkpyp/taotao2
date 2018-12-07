package com.taotao.search.service.impl;

import com.taotao.pojo.SearchItem;
import com.taotao.pojo.TaotaoResult;
import com.taotao.search.mapper.SearchItemMapper;
import com.taotao.search.service.SearchItemService;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * Created by Peng on 2018/11/13.
 */
@Service
public class SearchItemServiceImpl implements SearchItemService{

    @Autowired
    private SearchItemMapper searchItemMapper;

    @Autowired
    private SolrServer solrServer;

    @Override
    public TaotaoResult importItemToIndex() {
        //.注入mapper
        try {
            //查询所有的商品数据
            List<SearchItem> itemList = searchItemMapper.getSearchItemList();
            //注入SolrServer对象
            //遍历商品数据添加到索引库
            for (SearchItem searchItem:itemList){
                //创建Document对象
                SolrInputDocument document=new SolrInputDocument();
                document.addField("id",searchItem.getId());
                document.addField("item_title",searchItem.getTitle());
                document.addField("item_sell_point",searchItem.getSell_point());
                document.addField("item_price",searchItem.getPrice());
                document.addField("item_image",searchItem.getImage());
                document.addField("item_category_name",searchItem.getItem_category_name());
                document.addField("item_desc",searchItem.getItem_desc());
                //将文档添加至索引库
                solrServer.add(document);
            }
            //提交
            solrServer.commit();
            return TaotaoResult.ok();
        } catch (SolrServerException e) {
            e.printStackTrace();
            return TaotaoResult.build(500,"solr服务异常");
        } catch (IOException e) {
            e.printStackTrace();
            return TaotaoResult.build(500,"导入数据失败");
        }
    }
}
