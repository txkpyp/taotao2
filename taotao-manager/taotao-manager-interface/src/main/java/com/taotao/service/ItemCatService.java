package com.taotao.service;

import com.taotao.pojo.EasyUITreeCode;

import java.util.List;

/**
 * Created by Peng on 2018/11/7.
 */
public interface ItemCatService {
    /**
     * 根据父节点ID来查询树形结构，因为是懒加载，既最开始只显示第一级目录
     * 只有当点击下一级目录的时候才会去查询第二级的目录的内容
     * @param parentId
     * @return
     */

    List<EasyUITreeCode> getItemCatList(long parentId);

}
