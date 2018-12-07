package com.taotao.freemarker;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.*;

/**
 * Created by Peng on 2018/11/16.
 */
public class TestFreeMarker {

    @Test
    public void testFreemarker() throws Exception{
        //1.创建一个模板文件（就是我们刚刚创建的hello.ftl）
        //2.创建一个configuration对象
        Configuration configuration=new Configuration(Configuration.getVersion());
        //3.设置模板所在的路径
        configuration.setDirectoryForTemplateLoading(new File("D:/IdeaProjects/taotao/taotao-item-web/src/main/webapp/WEB-INF/ftl/"));
        //4.设置模板的字符集，一般为utf-8
        configuration.setDefaultEncoding("utf-8");
        //5.使用Configuration对象加载一个模板文件，需要指定模板文件的文件名
//        Template template = configuration.getTemplate("hello.ftl");
        Template template = configuration.getTemplate("student.ftl");
        //6.创建一个数据集，可以是pojo，也可以是map.推荐使用map
        Map data=new HashMap();
        data.put("hello","hello freemarker!");
        Student student=new Student(1,"张三",20,"广东宝安西乡街道永丰社区四区");
        data.put("student",student);

//        循环列表
        List<Student> stuList=new ArrayList<>();
        stuList.add(new Student(1,"李四",20,"广东宝安西乡街道永丰社区四区"));
        stuList.add(new Student(2,"李四2",22,"广东宝安西乡街道永丰社区四区"));
        stuList.add(new Student(3,"李四3",23,"广东宝安西乡街道永丰社区四区"));
        stuList.add(new Student(4,"李四4",24,"广东宝安西乡街道永丰社区四区"));
        stuList.add(new Student(5,"李四5",25,"广东宝安西乡街道永丰社区四区"));
        data.put("stuList",stuList);

//        日期处理
        data.put("date",new Date());

        data.put("var","123456");

        //7.创建一个writer对象，指定输出文件的路径和文件名
        Writer out=new FileWriter(new File("K:/freemarker/out/student.html"));
        //8.使用模板对象的process方法输出文件
        template.process(data,out);
        //9.关闭流
        out.close();
    }
}
