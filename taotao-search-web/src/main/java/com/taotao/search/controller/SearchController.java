package com.taotao.search.controller;

import com.taotao.pojo.SearchResult;
import com.taotao.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Peng on 2018/11/14.
 */
@Controller
public class SearchController {

    @Autowired
    private SearchService searchService;

    @Value("${SEARCH_RESULT_ROWS}")
    private Integer SEARCH_RESULT_ROWS;

    @RequestMapping("/search")
    public String search(@RequestParam("q") String queryString,
                         @RequestParam(defaultValue = "1") Integer page, Model model) {
        try {
            //解决乱码问题
            queryString=new String(queryString.getBytes("ISO8859-1"),"UTF-8");
            //引入服务
            //注入服务
            //调用服务
            SearchResult searchResult = searchService.search(queryString, page, SEARCH_RESULT_ROWS);
            //把结果传递给页面
            model.addAttribute("query", queryString);
            model.addAttribute("totalPages", searchResult.getTotalPage());
            model.addAttribute("itemList", searchResult.getItemList());
            model.addAttribute("page", page);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回逻辑页面
        return "search";
    }
}
