package com.hsiao.springboot.page.controller;


import com.hsiao.springboot.page.entity.Book;
import com.hsiao.springboot.page.entity.BookQuery;
import com.hsiao.springboot.page.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: BookController
 * @description: TODO
 * @author xiao
 * @create 2021/3/28
 * @since 1.0.0
 */

@Controller
@RequestMapping(value = "/queryBook")
public class BookController {

    @Autowired
    BookService bookService;

    @RequestMapping("/findBookNoQuery")
    public String findBookNoQuery(ModelMap modelMap,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "5") Integer size) {
        Page<Book> datas = bookService.findBookNoCriteria(page, size);
        modelMap.addAttribute("datas", datas);
        return "index1";
    }

    @RequestMapping(value = "/findBookQuery", method = {RequestMethod.GET, RequestMethod.POST})
    public String findBookQuery(ModelMap modelMap,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "5") Integer size, BookQuery bookQuery) {
        Page<Book> datas = bookService.findBookCriteria(page, size, bookQuery);
        modelMap.addAttribute("datas", datas);
        return "index2";
    }
}

