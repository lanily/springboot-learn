package com.hsiao.springboot.jersey.resource;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.hsiao.springboot.jersey.model.Employee;
import com.hsiao.springboot.jersey.service.EmployeeService;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.internal.inject.AbstractBinder;
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
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

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
//@RunWith(PowerMockRunner.class)
@RunWith(MockitoJUnitRunner.class)
//@PrepareForTest({CommonUtils.class})
public class EmployeeResourceMockTest extends JerseyTest {

    private static final String BASE_PATH = "/employees";

    @Mock
    EmployeeService employeeService;

//    @InjectMocks
//    private EmployeeResource employeeResource;

    Employee e;

    @Before
    public void setUp()
            throws Exception {
        super.setUp();
//        MockitoAnnotations.initMocks(this);
//        employeeService = mock(EmployeeService.class);
        assertNotNull(employeeService);
//        assertNotNull(employeeResource);
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
        ResourceConfig config = new ResourceConfig().register(EmployeeResource.class);
//        ResourceConfig config = new ResourceConfig().register(new EmployeeResource(employeeService));
        config.register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(employeeService).to(EmployeeService.class);
//              bind(EmployeeService.class).to(EmployeeService.class).in(Singleton.class);

            }
        });
        config.register(JacksonFeature.class);
        config.register(JacksonJsonProvider.class);
        return config;
    }

    @Override
    protected DeploymentContext configureDeployment() {
        return ServletDeploymentContext
                .forServlet(new ServletContainer(configure()))
                .build();
//        return ServletDeploymentContext.forPackages(getClass().getPackage().getName()).build();
    }


    @Test
    public void testGet() {
        Long id = 1L;
        Mockito.when(employeeService.find(anyLong())).thenReturn(e);
        Response response = target(BASE_PATH + "/" + id)
                .request()
                .get(Response.class);
        Mockito.verify(employeeService, Mockito.times(1))
                .find(anyLong());  // Validate if find() is called
        assertEquals(200, response.getStatus());
        Employee ret = response.readEntity(Employee.class);
        assertNotNull(ret);
    }


    @Test
    public void testGetQuery() {
        when(employeeService.find(any())).thenReturn(e);
        Response response = target("/employees")
                .queryParam("employeeId", Long.valueOf("1"))
//                .request(MediaType.APPLICATION_JSON_TYPE)
                .request()
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

