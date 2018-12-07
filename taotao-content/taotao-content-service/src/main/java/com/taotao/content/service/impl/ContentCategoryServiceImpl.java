package com.taotao.content.service.impl;

import com.taotao.content.service.ContentCategoryService;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.EasyUITreeCode;
import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import com.taotao.pojo.TbContentCategoryExample.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 内容分类
 * Created by Peng on 2018/11/8.
 */
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

    @Autowired
    private TbContentCategoryMapper contentCategoryMapper;

    @Override
    public List<EasyUITreeCode> getContentCategoryList(long parentId) {
        //1.注入mapper
        //2.创建example
        TbContentCategoryExample example=new TbContentCategoryExample();
        //3.设置条件
        Criteria criteria=example.createCriteria();
        criteria.andParentIdEqualTo(parentId);//select * from tbcontentcategory where parent_id=1
        //4.执行查询
        List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
        //5.转成EasyUITreeCode 列表
        List<EasyUITreeCode> nodes=new ArrayList<>();
        for (TbContentCategory contentCategory:list){
            EasyUITreeCode node=new EasyUITreeCode();
            node.setId(contentCategory.getId());
            node.setState(contentCategory.getIsParent()?"closed":"open");
            node.setText(contentCategory.getName());//分类名称
            nodes.add(node);
        }
        //6。返回
        return nodes;
    }

    @Override
    public TaotaoResult addContentCategroy(long parentId, String name) {
        //实例化一个对象
        TbContentCategory contentCategory = new TbContentCategory();
        //填充属性值
        contentCategory.setParentId(parentId);
        contentCategory.setName(name);
        //状态。可选值:1(正常),2(删除)，刚添加的节点肯定是正常的
        contentCategory.setStatus(1);
        //刚添加的节点肯定不是父节点
        contentCategory.setIsParent(false);
        //数据库中现在默认的都是1，所以这里我们也写成1
        contentCategory.setSortOrder(1);
        //保存当前操作时间
        contentCategory.setCreated(new Date());
        contentCategory.setUpdated(new Date());
        //插入节点到数据库
        contentCategoryMapper.saveAndGetId(contentCategory);
        //添加一个节点需要判断父节点是不是叶子节点，如果父节点是叶子节点的话，
        //需要改成父节点状态
        TbContentCategory parent = contentCategoryMapper.selectByPrimaryKey(parentId);
        if(!parent.getIsParent()){
            parent.setIsParent(true);
            contentCategoryMapper.updateByPrimaryKey(parent);
        }
        return TaotaoResult.ok(contentCategory);
    }

    @Override
    public TaotaoResult updateContentCategory(long id, String name) {
        //通过id查询节点对象
        TbContentCategory contentCategory=contentCategoryMapper.selectByPrimaryKey(id);
        //判断性的name值是否与原来的值相同，如果相同这不更新
        if(null!=name && name.equals(contentCategory.getName())){
            return TaotaoResult.ok();
        }
        contentCategory.setName(name);
        //设置更新时间
        contentCategory.setCreated(new Date());
        contentCategory.setUpdated(new Date());
        //更新节点数据库
        contentCategoryMapper.updateByPrimaryKey(contentCategory);
        return TaotaoResult.ok();
    }


    //通过父节点id来查询所有子节点，这是抽离出来的公共方法
    private List<TbContentCategory> getContentCategoryListByParentId(long parentId){
        TbContentCategoryExample example = new TbContentCategoryExample();
        Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
        return list;
    }

    //递归删除节点
    private void deleteNode(long parentId){
        List<TbContentCategory> list = getContentCategoryListByParentId(parentId);
        for(TbContentCategory contentCategory : list){
            contentCategory.setStatus(2);
            contentCategoryMapper.updateByPrimaryKey(contentCategory);
            if(contentCategory.getIsParent()){
                deleteNode(contentCategory.getId());
            }
        }
    }

    @Override
    public TaotaoResult deleteContentCategory(long id) {
        //删除分类，就是改节点的状态为2
        TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(id);
        //状态。可选值:1(正常),2(删除)
        contentCategory.setStatus(2);
        contentCategoryMapper.updateByPrimaryKey(contentCategory);
        //我们还需要判断一下要删除的这个节点是否是父节点，如果是父节点，那么就级联
        //删除这个父节点下的所有子节点（采用递归的方式删除）
        if(contentCategory.getIsParent()){
            deleteNode(contentCategory.getId());
        }
        //需要判断父节点当前还有没有子节点，如果有子节点就不用做修改
        //如果父节点没有子节点了，那么要修改父节点的isParent属性为false即变为叶子节点
        TbContentCategory parent = contentCategoryMapper.selectByPrimaryKey(contentCategory.getParentId());
        List<TbContentCategory> list = getContentCategoryListByParentId(parent.getId());
        //判断父节点是否有子节点是判断这个父节点下的所有子节点的状态，如果状态都是2就说明
        //没有子节点了，否则就是有子节点。
        boolean flag = false;
        for(TbContentCategory tbContentCategory : list){
            if(tbContentCategory.getStatus() == 0){
                flag = true;
                break;
            }
        }
        //如果没有子节点了
        if(!flag){
            parent.setIsParent(false);
            contentCategoryMapper.updateByPrimaryKey(parent);
        }
        //返回结果
        return TaotaoResult.ok();
    }
}
