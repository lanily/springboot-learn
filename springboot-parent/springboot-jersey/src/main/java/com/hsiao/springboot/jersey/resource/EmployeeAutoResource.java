package com.hsiao.springboot.jersey.resource;

import com.hsiao.springboot.jersey.model.Employee;
import com.hsiao.springboot.jersey.service.EmployeeService;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * https://github.com/runepeter/jersey-guice-test-framework/blob/master/src/test/java/no/jforce/jersey/guice/example/resource/BallsResource.java
 */

@Component
@Path("/auto")
public class EmployeeAutoResource {


    @Autowired
    EmployeeService employeeService;

    public EmployeeAutoResource() {}


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createEmployee(final Employee employee) throws URISyntaxException {
        if (Objects.isNull(employee.getName()) || Objects.isNull(employee.getAddress())) {
            return Response.status(400).entity("Please provide all mandatory inputs").build();
        }
        employeeService.save(employee);
        return Response.status(201).entity(employee)
                .contentLocation(new URI("/employees/" + employee.getId())).build();
    }

    @GET
    @Path("/{employeeId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getEmployee(@PathParam("employeeId") Long employeeId)
            throws URISyntaxException {
        Employee employee = employeeService.find(employeeId);
        if (Objects.isNull(employee)) {
            return Response.status(404).build();
        }
        return Response
                .status(200)
                .entity(employee)
                .contentLocation(new URI("/employees/" + employeeId)).build();
    }

    @GET
    public Employee getEmployeeQuery(@QueryParam("employeeId") Long employeeId) {
        return employeeService.find(employeeId);
    }

    @PUT
    @Path("/{employeeId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editEmployee(final Employee employee,
            @PathParam("employeeId") Long employeeId) {
        Employee e = employeeService.find(employeeId);
        if (Objects.isNull(e)) {
            return Response.status(404).build();
        }
        Employee updated = employeeService.update(employeeId, employee);
        return Response.status(200).entity(updated).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Employee> ListEmployees() {
        return employeeService.findAll();
    }


    @DELETE
    @Path("/{employeeId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteEmployees(@PathParam("employeeId") Long employeeId) {
        Employee e = employeeService.find(employeeId);
        if (Objects.nonNull(e)) {
            employeeService.delete(employeeId);
            return Response.status(200).build();
        }
        return Response.status(404).build();
    }
}
