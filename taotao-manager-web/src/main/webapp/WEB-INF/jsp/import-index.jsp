<%--
  Created by IntelliJ IDEA.
  User: Peng
  Date: 2018/11/13
  Time: 17:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div>
    <a class="easyui-linkbutton" onclick="importIndex()">导入数据到索引库</a>
</div>
<script type="text/javascript">
    function importIndex(){
        $.post("/index/import",null,function(data){
            if(data.status == 200){
                $.messager.alert('提示','导入数据到索引库成功!');
            }else{
                $.messager.alert('提示','导入数据到索引库失败!');
            }
        });
    }
</script>