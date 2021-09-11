package com.hsiao.springboot.jersey.config;

import com.hsiao.springboot.jersey.resource.EmployeeResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;
import org.springframework.context.annotation.Configuration;


@Configuration
//@Named
public class JerseyAppConfig extends ResourceConfig {

    public JerseyAppConfig() {
        // 注册包的方式
//        packages(JerseyApplication.class.getPackage().toString());
//        packages("com.hsiao.springboot.jersey.resource");
        // 注册类的方式
        /*
* Servlet Filter that exposes the request to
the current thread, through both org.springframework.context.i18n.LocaleContextHolder
and RequestContextHolder. To be registered as
filter in web.xml.<br/>
Alternatively, Spring's org.springframework.web.context
.request.RequestContextListener and Spring's org.springframework.web.servlet.DispatcherServlet
also expose the same request context to the
current thread.<br/>
This filter is mainly for use with third-party
servlets, e.g. the JSF FacesServlet. Within
Spring's own web support, DispatcherServlet's
processing is perfectly sufficient.<br/>
*/
        register(RequestContextFilter.class);
        register(EmployeeResource.class);
//        register(MultiPartFeature.class);
//        register(RequestContextFilter.class);
//        register(LoggingFilter.class);
    }

/*    @Bean
    public ServletRegistrationBean jerseyServlet() {
        // 手动注册servlet
        ServletRegistrationBean registration = new ServletRegistrationBean(new ServletContainer(), "/api/*");
        registration.addInitParameter(ServletProperties.JAXRS_APPLICATION_CLASS, JerseyAppConfig.class.getName());
        return registration;
    }*/
}