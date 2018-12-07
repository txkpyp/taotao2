package com.taotao.listener;

import com.taotao.item.pojo.Item;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.service.ItemService;
import freemarker.core.ParseException;
import freemarker.template.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Peng on 2018/11/16.
 */
public class ItemAddMessageListener implements MessageListener {

    @Autowired
    private ItemService itemService;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Value("${HTML_OUT_PATH}")
    private String HTML_OUT_PATH;

    @Override
    public void onMessage(Message message) {
        try {
            //从消息中获取商品ID
            TextMessage textMessage = (TextMessage) message;
            String itemIdStr = textMessage.getText();
            long itemId = Long.valueOf(itemIdStr);
            //等待事务提交，采用三次尝试的机会
            TbItem tbitem = null;
            for (int i = 0; i < 3; i++) {
                //根据商品ID查询商品的基本信息和描述
                Thread.sleep(1000);
                tbitem = itemService.getItemById(itemId);
                if (tbitem != null) {
                    break;
                }
            }
            Item item = new Item(tbitem);
            TbItemDesc itemDesc = itemService.getItemDescById(itemId);
            //使用freemarker生成静态页面
            Configuration configuration = freeMarkerConfigurer.getConfiguration();
            //创建模板并加载模板对象
            Template template = configuration.getTemplate("item.ftl");
            //准备模板需要的数据
            Map data = new HashMap();
            data.put("item", item);
            data.put("itemDesc", itemDesc);
            //指定输出目录和文件名
            OutputStreamWriter out = new OutputStreamWriter(
                    new FileOutputStream(new File(HTML_OUT_PATH + itemIdStr + ".html")), "UTF-8");
            //生成静态页面
            template.process(data, out);
            //关闭流
            out.close();
        } catch (JMSException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (MalformedTemplateNameException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (TemplateNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }
}
