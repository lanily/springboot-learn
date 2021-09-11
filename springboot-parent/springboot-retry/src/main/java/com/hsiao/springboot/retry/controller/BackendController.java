package com.hsiao.springboot.retry.controller;


import com.hsiao.springboot.retry.service.BackendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * http://localhost:8080/retry?retry=true&fallback=true
 *
 * @projectName springboot-parent
 * @title: BackendController
 * @description: TODO
 * @author xiao
 * @create 2021/3/7
 * @since 1.0.0
 */
@RestController
public class BackendController {

    @Autowired
    BackendService backendService;

    @GetMapping("/retry")
    public String retry(@RequestParam(name = "retry") boolean retry,
            @RequestParam(name = "fallback") boolean fallback) {
        return backendService.service(retry, fallback);
    }
}

