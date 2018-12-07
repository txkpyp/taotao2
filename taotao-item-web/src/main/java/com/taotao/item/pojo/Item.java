package com.taotao.item.pojo;

import com.taotao.pojo.TbItem;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Peng on 2018/11/16.
 */
public class Item extends TbItem {

    public Item(TbItem tbItem){
        this.setId(tbItem.getId());
        this.setBarcode(tbItem.getBarcode());
        this.setCid(tbItem.getCid());
        this.setCreated(tbItem.getCreated());
        this.setImage(tbItem.getImage());
        this.setNum(tbItem.getNum());
        this.setPrice(tbItem.getPrice());
        this.setSellPoint(tbItem.getSellPoint());
        this.setStatus(tbItem.getStatus());
        this.setTitle(tbItem.getTitle());
        this.setUpdated(tbItem.getUpdated());
    }

    public String[] getImages(){
        if (!StringUtils.isBlank(this.getImage())){
            String[] images=this.getImage().split(",");
            return images;
        }
        return null;
    }


}


