package com.hsiao.springboot.jersey;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: ServletInitializerTest
 * @description: TODO
 * @author xiao
 * @create 2021/8/7
 * @since 1.0.0
 */
@RunWith(SpringRunner.class)
public class ServletInitializerTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private ServletInitializer servletInitializer = new ServletInitializer();

    @Test
    public void configureWithValidArgumentsShouldReturnASpringApplicationBuilder() {
        SpringApplicationBuilder result = servletInitializer.configure(new SpringApplicationBuilder(JerseyApplication.class));
        assertThat(result, notNullValue());
    }

    @Test
    public void configureWithNullArgumentsShouldReturnASpringApplicationBuilder() {
        thrown.expect(NullPointerException.class);
        servletInitializer.configure(null);
    }
}

