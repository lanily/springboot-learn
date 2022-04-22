package com.hsiao.springboot.swagger.controller;


import com.hsiao.springboot.swagger.model.Ad;
import com.hsiao.springboot.swagger.model.Goods;
import com.hsiao.springboot.swagger.model.PageResult;
import com.hsiao.springboot.swagger.model.PaginationResult;
import com.hsiao.springboot.swagger.model.RestResult;
import com.hsiao.springboot.swagger.model.RowResult;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 *
 * 首页信息管理
 *
 * @projectName springboot-parent
 * @title: HomeController
 * @description: 首页信息管理
 * @author xiao
 * @create 2021/9/12
 * @since 1.0.0
 */
@Api(tags = "首页信息管理")
@RestController
@RequestMapping("/home")
public class HomeController {

    /*@Operation(summary = "首页详情")
    @GetMapping("/home")
    public String home() {
        return "success(this.userRecordService.getById(id))";
    }*/

    @Operation(summary = "session详情")
    @GetMapping("/session")
    @ResponseBody
    public String session() {
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest().getSession();
        Enumeration e = session.getAttributeNames();
        String s = "";
        while (e.hasMoreElements()) {
            String sessionName = (String) e.nextElement();
            s += "name=" + sessionName + ";<br/>";
            s += "value=" + session.getAttribute(sessionName) + ";";
        }
        return s;
    }

    @Operation(summary = "首页详情")
    @GetMapping("/home")
    @ResponseBody
    public RestResult<PageResult<RowResult<Goods, Ad>>> home() {
        //整体返回数据
        RestResult res = new RestResult();
        //第一行的列表
        List<Goods> rowList = new ArrayList();
        Goods goodsone = new Goods();
        goodsone.setGoodsId(3L);
        goodsone.setGoodsName("电子书");
        goodsone.setSubject("学python,学ai");
        goodsone.setPrice(new BigDecimal(60));
        goodsone.setStock(10);
        rowList.add(goodsone);

        Goods goodstwo = new Goods();
        goodstwo.setGoodsId(4L);
        goodstwo.setGoodsName("蓝牙音箱");
        goodstwo.setSubject("便携式音质优异");
        goodstwo.setPrice(new BigDecimal(1200.00));
        goodstwo.setStock(30);
        rowList.add(goodstwo);
        //第一行的result
        RowResult oneRow = new RowResult();
        oneRow.setListGoods(rowList);
        oneRow.setRowType("goods");

        //第二行的列表
        List<Ad> rowList2 = new ArrayList();
        Ad adone = new Ad();
        adone.setTitle("保温杯");
        adone.setImgurl("http://a.com/1.jpg");
        rowList2.add(adone);

        Ad adtwo = new Ad();
        adtwo.setTitle("茶具");
        adtwo.setImgurl("http://a.com/2.jpg");
        rowList2.add(adtwo);

        //第二行的result
        RowResult twoRow = new RowResult();
        twoRow.setListAd(rowList2);
        twoRow.setRowType("ad");

        //各行都添加到pagelist
        List<RowResult> pageList = new ArrayList();
        pageList.add(oneRow);
        pageList.add(twoRow);

        //pagelist添加到pageresult
        PageResult<List<RowResult>> pres = new PageResult();
        pres.setList(pageList);

        //分页数字
        PaginationResult pares = new PaginationResult();
        pares.setTotal(10);
        pares.setCurrentPage(3);
        pares.setPerPageCount(5);

        //设置到分页result
        pres.setPagination(pares);

        return res.success(pres);
    }
}
