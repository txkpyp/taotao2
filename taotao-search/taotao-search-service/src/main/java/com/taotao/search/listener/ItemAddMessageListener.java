package com.taotao.search.listener;

import com.taotao.pojo.SearchItem;
import com.taotao.search.mapper.SearchItemMapper;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.IOException;

/**
 * Created by Peng on 2018/11/16.
 */
public class ItemAddMessageListener implements MessageListener {

    @Autowired
    private SearchItemMapper searchItemMapper;
    @Autowired
    private SolrServer solrServer;

    @Override
    public void onMessage(Message message) {
        TextMessage textMessage= (TextMessage) message;
        try {
            String text = textMessage.getText();
            long itemId= Long.valueOf(text);
            //根据商品id查询商品详情，这里需要注意的是消息发送方法
            //有可能还没有提交事务，因此这里是有可能取不到商品信息
            //的，为了避免这种情况出现，我们最好等待事务提交，这里
            //我采用3次尝试的方法，每尝试一次休眠一秒
            SearchItem searchItem=null;
            for (int i=0; i<3;i++){
                try {
                    Thread.sleep(1000);
                    searchItem=searchItemMapper.getItemById(itemId);
                    //如果获取到了商品信息，那就退出循环
                    if (searchItem !=null){
                        break;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //创建文档对象
            SolrInputDocument document=new SolrInputDocument();
            //向文档对象中添加域
            document.setField("id",searchItem.getId());
            document.setField("item_title",searchItem.getTitle());
            document.setField("item_sell_point",searchItem.getSell_point());
            document.setField("item_price",searchItem.getPrice());
            document.setField("item_image",searchItem.getImage());
            document.setField("item_category_name",searchItem.getItem_category_name());
            document.setField("item_desc",searchItem.getItem_desc());
            solrServer.add(document);
            solrServer.commit();
        } catch (JMSException e) {
            e.printStackTrace();
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
