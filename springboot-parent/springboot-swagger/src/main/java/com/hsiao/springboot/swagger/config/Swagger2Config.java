package com.hsiao.springboot.swagger.config;


import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: Swagger2Config
 * @description: TODO
 * @author xiao
 * @create 2021/9/12
 * @since 1.0.0
 */
//标明是配置类
//@Configuration
//开启swagger功能
//@EnableSwagger2
// 开启SwaggerBootstrapUI
//@EnableSwaggerBootstrapUI
//@Profile({"dev","test"})
public class Swagger2Config implements WebMvcConfigurer {

    @Bean
    public Docket createRestApi() {
        //DocumentationType.SWAGGER_2 固定的，代表swagger2
        return new Docket(DocumentationType.SWAGGER_2)
                //如果配置多个文档的时候，那么需要配置groupName来分组标识
                //.groupName("分布式任务系统")
                //用于生成API信息
                .apiInfo(apiInfo())
                //select()函数返回一个ApiSelectorBuilder实例,用来控制接口被swagger做成文档
                .select()
                //用于指定扫描哪个包下的接口
                .apis(RequestHandlerSelectors
                        .basePackage("com.hsiao.springboot.swagger.controller"))
                //选择所有的API,如果你想只为部分API生成文档，可以配置这里
                .paths(PathSelectors.regex(".sys/login"))
                .build();
    }

    /**
     * 用于定义API主界面的信息，比如可以声明所有的API的总标题、描述、版本
     *
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //可以用来自定义API的主标题
                .title("XX项目API")
                //可以用来描述整体的API
                .description("XX项目SwaggerAPI管理")
                //用于定义服务的域名
                .termsOfServiceUrl("")
                //可以用来定义版本
                .version("2.0")
                .build();
        /*
        // 如果有额外的全局参数，比如说请求头参数，可以这样添加
        ParameterBuilder parameterBuilder = new ParameterBuilder();
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameterBuilder.name("authorization").description("令牌")
                .modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        parameters.add(parameterBuilder.build());
        // DocumentationType.SWAGGER_2 固定的，代表swagger2
        return new Docket(DocumentationType.SWAGGER_2)
                // 用于生成API信息
                .apiInfo(apiInfo())
                // select()函数返回一个ApiSelectorBuilder实例,用来控制接口被swagger做成文档
                .select()
                // 用于指定扫描哪个包下的接口
                .apis(RequestHandlerSelectors.basePackage("com.example.controller"))
                // 选择所有的API,如果你想只为部分API生成文档，可以配置这里
                .paths(PathSelectors.any())
                .build().globalOperationParameters(parameters);
     **/
    }

    /**
     * swagger-ui.html访问不了，页面报错404,解决办法
     *
     * @param registry 注意：重写 addResourceHandlers 方法需要 implements WebMvcConfigurer 类
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry
                .addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry
                .addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
