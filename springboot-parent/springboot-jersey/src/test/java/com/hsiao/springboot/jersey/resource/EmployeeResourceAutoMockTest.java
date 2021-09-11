package com.hsiao.springboot.jersey.resource;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.powermock.api.mockito.PowerMockito.when;

import com.hsiao.springboot.jersey.model.Employee;
import com.hsiao.springboot.jersey.service.EmployeeService;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJsonProvider;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.test.DeploymentContext;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.ServletDeploymentContext;
import org.glassfish.jersey.test.TestProperties;
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.glassfish.jersey.test.spi.TestContainerFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.modules.junit4.PowerMockRunner;

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
@RunWith(PowerMockRunner.class)
public class EmployeeResourceAutoMockTest extends JerseyTest {

    private static final String BASE_PATH = "/auto";

    @Mock
    EmployeeService employeeService;

    @InjectMocks
    private EmployeeAutoResource employeeResource;

    Employee e;

    @Before
    public void setUp()
            throws Exception {
        super.setUp();
        assertNotNull(employeeService);
        assertNotNull(employeeResource);
        e = new Employee();
        e.setId(Long.valueOf(1L));
        e.setAddress("china");
        e.setName("shawn");
        e.setCreatedAt(new Date());
        e.setUpdatedAt(new Date());
    }

    @Override
    protected TestContainerFactory getTestContainerFactory() {
        return new GrizzlyWebTestContainerFactory();
    }

    @Override
    protected ResourceConfig configure() {
        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);
        MockitoAnnotations.initMocks(this);
        ResourceConfig config = new ResourceConfig().register(employeeResource);
        config.register(JacksonFeature.class);
        config.register(JacksonJsonProvider.class);
        return config;
    }

    @Override
    protected DeploymentContext configureDeployment() {
        return ServletDeploymentContext
                .forServlet(new ServletContainer(configure()))
                .build();
    }


    @Test
    public void testGet() {
        Long id = 1L;
        when(employeeService.find(anyLong())).thenReturn(e);
        Response response = target(BASE_PATH + "/" + id)
                .request()
                .get(Response.class);
        assertEquals(200, response.getStatus());
        Employee ret = response.readEntity(Employee.class);
        assertNotNull(ret);
    }


    @Test
    public void testGetQuery() {
        when(employeeService.find(any())).thenReturn(e);
        Response response = target("/employees")
                .queryParam("employeeId", Long.valueOf("1"))
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(Response.class);
        assertEquals(200, response.getStatus());
        Employee ret = response.readEntity(Employee.class);
        assertNotNull(ret);
    }

    @Test
    public void testGetAll() {
        when(employeeService.findAll()).thenReturn(Collections.singletonList(e));
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

