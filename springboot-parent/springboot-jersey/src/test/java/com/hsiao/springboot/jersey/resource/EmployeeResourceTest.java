package com.hsiao.springboot.jersey.resource;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.hsiao.springboot.jersey.SpringContextJerseyTest;
import com.hsiao.springboot.jersey.JerseyApplication;
import com.hsiao.springboot.jersey.model.Employee;
import com.hsiao.springboot.jersey.service.EmployeeService;
import java.util.Date;
import java.util.List;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJsonProvider;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: EmployeeResourceTest
 * @description: TODO
 * @author xiao
 * @create 2021/8/7
 * @since 1.0.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JerseyApplication.class)
public class EmployeeResourceTest extends SpringContextJerseyTest {


    @Autowired
    private EmployeeService employeeService;

    Employee e;

    @Before
    public void setUp() {
        super.setUp();
        assertNotNull(employeeService);
        e = new Employee();
        e.setId(Long.valueOf(1L));
        e.setAddress("china");
        e.setName("shawn");
        e.setCreatedAt(new Date());
        e.setUpdatedAt(new Date());
    }

    @Override
    protected Application configure() {
        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);
        ApplicationContext context = new AnnotationConfigApplicationContext(JerseyApplication.class);
//        ResourceConfig config = new ResourceConfig().register(new EmployeeResource(employeeService));
        ResourceConfig config = new ResourceConfig().register(EmployeeResource.class);
        config.property("contextConfig", context);
        config.register(JacksonFeature.class);
        config.register(JacksonJsonProvider.class);
        return config;
    }

    /*    @Override
    protected Application configure() {
        ResourceConfig resourceConfig = new JerseyApplication();
        x
//        resourceConfig.property("contextConfigLocation", MOCK_SPRING_APPLICATIONCONTEXT); // Set which application context to use
        return resourceConfig;
    }*/

    @Test
    public void testGet() {
        Long id = 1L;
        Response response = target(BASE_PATH  + "/" + id)
                .request()
                .get(Response.class);
//        Mockito.verify(employeeService, Mockito.times(1))
//                .find(anyInt());  // Validate if find() is called
        assertEquals(200, response.getStatus());
        Employee ret = response.readEntity(Employee.class);
        assertNotNull(ret);
    }


/*    @Test
    public void testGetQuery() {
        when(employeeService.find(any())).thenReturn(mock(Employee.class));
        Response response = target("/employees")
                .queryParam("employeeId", "1")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(Response.class);
        assertEquals(200, response.getStatus());
        Employee ret = response.readEntity(Employee.class);
        assertNotNull(ret);
    }*/

    @Test
    public void testGetAll() {
        Response response = target(BASE_PATH)
                .request()
                .get(Response.class);
        assertEquals("Should return status 200", 200, response.getStatus());
        List<Employee> ret = response.readEntity(new GenericType<List<Employee>>() {
        });
        assertNotNull(ret);
    }

    @Test
    public void testPost() {
        Response response = target(BASE_PATH)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity
                        .entity(e, MediaType.APPLICATION_JSON_TYPE)
                );
/*        Employee r = target("/employees")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity
                        .entity(e, MediaType.APPLICATION_JSON_TYPE)
                , Employee.class);
                */
        Employee ret = response.readEntity(Employee.class);
        assertNotNull(ret);
    }

/*
    @Test
    public void testDoSomethingWithException() {
        Mockito.doThrow(new RuntimeException()).when(mockSomeService).doSomething();
        Response response = target("someaction").request().get(Response.class);
        Mockito.verify(mockSomeService, Mockito.times(1)).doSomething();  // Validate if doSomething() is called
        Assert.assertEquals(500, response.getStatus()); // Expect 500 when exception is thrown
    }
*/

}

