package com.taotao;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * Created by Peng on 2018/11/13.
 */
public class TestSolrJ {


    @Test
    public void testAddDomcument() throws Exception{
        //创建一个solrServer对象，创建一个HttpSolrServer对象，需要制定solr服务的url
        //如果有多个collection则需要制定要操作哪个collection，如果只有一个，可以不指定
        SolrServer solrServer=new HttpSolrServer("http://192.168.37.165:8080/solr/collection1");
        //创建一个文档对象SolrInputDocumnet
        SolrInputDocument document=new SolrInputDocument();
        //向文档中添加域，必须有ID域，域的名称必须在shcama.xml中定义
        document.addField("id","222");
        document.addField("item_title","联想电脑");
        document.addField("item_sell_point","送耳机一个");
        document.addField("item_price","10000");
        document.addField("item_image","http://www.3445.jpg");
        document.addField("item_category_name","电器");
        document.addField("item_desc","这是一款最新的电脑，值得信赖！！");
        //将document添加至索引库
        solrServer.add(document);
        //提交
        solrServer.commit();
    }
    @Test
    public void testDelDomcument() throws Exception{
        //创建一个solrServer对象，创建一个HttpSolrServer对象，需要制定solr服务的url
        SolrServer solrServer=new HttpSolrServer("http://192.168.37.165:8080/solr/collection1");
        //通过ID来删除对象
        solrServer.deleteById("change.me");
        //提交
        solrServer.commit();
    }
    @Test
    public void testDelDomcumentByQuery() throws Exception{
        //创建一个solrServer对象，创建一个HttpSolrServer对象，需要制定solr服务的url
        SolrServer solrServer=new HttpSolrServer("http://192.168.37.165:8080/solr/collection1");
        //通过ID来删除对象
        solrServer.deleteByQuery("item_price:15000");
        //提交
        solrServer.commit();
    }

    @Test
    public void testQueryDomcument() throws Exception{
        //创建一个solrServer对象，创建一个HttpSolrServer对象，需要制定solr服务的url
        SolrServer solrServer=new HttpSolrServer("http://192.168.37.165:8080/solr/collection1");
        //通过ID来删除文档
        SolrQuery query=new SolrQuery();
        query.setQuery("id:222");
        QueryResponse response=solrServer.query(query);
        SolrDocumentList list=response.getResults();
        for (SolrDocument document:list){
            String id =document.getFieldValue("id").toString();
            String title=document.getFieldValue("item_title").toString();
            System.out.println(id);
            System.out.println(title);
        }
        //提交
        solrServer.commit();
    }

    @Test
    public void queryDocument() throws Exception{
        //创建一个solrServer对象，创建一个HTTPSolrServer对象，需要指定solr服务的url
        SolrServer solrServer=new HttpSolrServer("http://192.168.37.165:8080/solr/collection1");
        //创建一个SolrQuery对象
        SolrQuery query=new SolrQuery();
        //设置查询条件，过滤、分页条件，排序条件，高亮
        //query.set("q","*:*")
        query.setQuery("手机");
        //分页条件
        query.setStart(0);
        query.setRows(3);
        //设置默认搜索域
        query.set("df","item_keywords");
        //设置高亮
        query.setHighlight(true);
        //高亮显示区域
        query.addHighlightField("item_title");
        query.setHighlightSimplePre("<em>");
        query.setHighlightSimplePost("</em>");
        //执行查询，得到一个response对象
        QueryResponse response=solrServer.query(query);
        //取查询结果
        SolrDocumentList solrDocumentList=response.getResults();
        //取查询结果总记录数
        System.out.println("查询结果总记录数："+solrDocumentList.getNumFound());
        for (SolrDocument document:solrDocumentList){
            System.out.println(document.getFieldValue("id"));
            //取高亮显示
            Map<String,Map<String,List<String>>> highlighting=response.getHighlighting();
            List<String> list=highlighting.get(document.getFieldValue("id")).get("item_title");
            String itemTitle="";
            if (list != null && list.size()>0){
                itemTitle=list.get(0);
            }else {
                itemTitle= (String) document.get("item_title");
            }
            System.out.println(itemTitle);
            System.out.println(document.get("item_sell_point"));
            System.out.println(document.get("item_price"));
            System.out.println(document.get("item_image"));
            System.out.println(document.get("item_category_name"));
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        }
    }
}
