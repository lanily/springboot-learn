package com.hsiao.springboot.websocket.dist.handler;

import javax.servlet.ServletException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.mock.web.MockServletContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.DispatcherServlet;

@Component
public class DispatcherLoader implements ApplicationListener<ContextRefreshedEvent> {


    @Autowired
    DispatcherServlet dispatcherServlet;



    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        try {
            this.dispatcherServlet.init(new MockServletConfig(new MockServletContext()));
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }
}
