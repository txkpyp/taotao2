package com.taotao.service.impl;

import com.taotao.pojo.EasyUITreeCode;
import com.taotao.mapper.TbItemCatMapper;
import com.taotao.pojo.TbItemCat;
import com.taotao.pojo.TbItemCatExample;
import com.taotao.pojo.TbItemCatExample.*;
import com.taotao.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peng on 2018/11/7.
 */
@Service
public class ItemCatServiceImpl implements ItemCatService {
    @Autowired
    private TbItemCatMapper mapper;
    @Override
    public List<EasyUITreeCode> getItemCatList(long parentId) {
        //1.注入mapper
        //根据父节点的ID查询直接点的列表
        TbItemCatExample example=new TbItemCatExample();
        //设置查询条件
        Criteria criteria = example.createCriteria();
        //设置parentid
        criteria.andParentIdEqualTo(parentId);
        //执行查询
        List<TbItemCat> list = mapper.selectByExample(example);
        List<EasyUITreeCode> easyUITreeCodeslist=new ArrayList<>();
        for (TbItemCat itemcat:list) {
            EasyUITreeCode easyUITreeCode=new EasyUITreeCode();
            easyUITreeCode.setId(itemcat.getId());
            easyUITreeCode.setText(itemcat.getName());
            easyUITreeCode.setState(itemcat.getIsParent()?"closed":"open");
            easyUITreeCodeslist.add(easyUITreeCode);
        }
        return easyUITreeCodeslist;
    }
}
