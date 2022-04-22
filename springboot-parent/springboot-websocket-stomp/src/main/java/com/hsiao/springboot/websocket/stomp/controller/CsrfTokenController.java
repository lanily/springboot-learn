package com.hsiao.springboot.websocket.stomp.controller;


import javax.servlet.http.HttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * 〈一句话功能简述〉<br>
 * https://github.com/eugenp/tutorials/tree/master/spring-security-modules/spring-security-web-sockets
 * @projectName springboot-parent
 * @title: CsrfTokenController
 * @description: TODO
 * @author xiao
 * @create 2022/4/21
 * @since 1.0.0
 */
@Controller
public class CsrfTokenController {
    @GetMapping("/csrf")
    public @ResponseBody
    String getCsrfToken(HttpServletRequest request) {
        CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        return csrf.getToken();
    }
}
