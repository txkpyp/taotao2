package com.taotao;

import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

/**
 * Created by Peng on 2018/11/14.
 */
public class SolrCloudTest {

    @Test
    public void testAdd() throws Exception{
        //1.创建一个solrserver对象，集群的实现类
        CloudSolrServer solrServer=new CloudSolrServer("192.168.37.165:2181,192.168.37.165:2182,192.168.37.165:2183");
        //2.设置默认的collection 默认的索引库（不是core所对应的，是整个collection索引集合）
        solrServer.setDefaultCollection("collection2");
        //3.SolrInputDocument
        SolrInputDocument document=new SolrInputDocument();
        //4.添加域到文档
        document.setField("id","testCloudID");
        document.setField("item_title","哎哟，今天天气不错喔！适合敲代码！");
        //5.将文档提交到索引库
        solrServer.add(document);
        //6、提交
        solrServer.commit();
    }
}
