package com.taotao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peng on 2018/11/28.
 */
public class TestListRemoveMethod {
    public static void main(String[] args) {
        List list=new ArrayList<String>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        String str="4";
        for (Object o:list){
            if (str.equals(o.toString())){
                list.remove(o);
            }
        }

    }
}
