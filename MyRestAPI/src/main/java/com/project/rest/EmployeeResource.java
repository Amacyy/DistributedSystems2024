package com.project.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.project.db.Employee;
import com.project.db.EmployeeDAO;

@Path("/employees")
public class EmployeeResource {
	@POST
	@Path("/createTable")
	public Response createTable() {
		try {
			EmployeeDAO.instance.createTable();
			return Response.status(Response.Status.OK).build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@POST
	@Path("/fillTable")
	public Response fillTable() {
		try {
			EmployeeDAO.instance.fillTable();
			return Response.status(Response.Status.OK).build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GET
	@Path("/id/{id}")
	@Produces(MediaType.APPLICATION_XML)
	public List<Employee> getValues(@PathParam("id") String temp) {
		int id = Integer.parseInt(temp);
		List<Employee> arr = EmployeeDAO.instance.getEmployee(id);
		return arr;
	}

	@GET
	@Path("/name/{name}")
	@Produces(MediaType.APPLICATION_XML)
	public List<Employee> getValuesByName(@PathParam("name") String name) {
		List<Employee> arr = EmployeeDAO.instance.getEmployeeByName(name);
		return arr;
	}

	@GET
	@Path("/age/{age}")
	@Produces(MediaType.APPLICATION_XML)
	public List<Employee> getValuesByAge(@PathParam("age") String temp) {
		int age = Integer.parseInt(temp);
		List<Employee> arr = EmployeeDAO.instance.getEmployeeByAge(age);
		return arr;
	}

	@GET
	@Path("/searchsector/{sector}")
	@Produces(MediaType.APPLICATION_XML)
	public List<Employee> getValuesBySector(@PathParam("sector") String temp) {
		int sector = Integer.parseInt(temp);
		List<Employee> arr = EmployeeDAO.instance.getEmployeeBySector(sector);
		return arr;
	}

	@GET
	@Path("/building/{building}")
	@Produces(MediaType.APPLICATION_XML)
	public List<Employee> getValuesByBuilding(@PathParam("building") String building) {
		List<Employee> arr = EmployeeDAO.instance.getEmployeeByBuilding(building);
		return arr;
	}

	@GET
	@Produces(MediaType.APPLICATION_XML)
	public List<Employee> getValues() {
		List<Employee> arr = EmployeeDAO.instance.getAllEmployees();
		return arr;
	}

	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response editPerson(@PathParam("id") String tempId, @FormParam("name") String name,
			@FormParam("age") String tempAge, @FormParam("sector") String tempSector) {
		try {
			int id = Integer.parseInt(tempId);
			int age = Integer.parseInt(tempAge);
			int sector = Integer.parseInt(tempSector);
			EmployeeDAO.instance.editEmployee(id, name, age, sector);
			return Response.status(Response.Status.OK).build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DELETE
	@Path("/{id}")
	public Response deleteOne(@PathParam("id") String tempId) {
		try {
			int id = Integer.parseInt(tempId);
			EmployeeDAO.instance.deleteEmployee(id);
			return Response.status(Response.Status.OK).build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DELETE
	public Response deleteAll() {
		try {
			EmployeeDAO.instance.deleteAllEmployees();
			return Response.status(Response.Status.OK).build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response insertPerson(@FormParam("name") String name, @FormParam("age") String tempAge,
			@FormParam("sector") String tempSector) {
		try {
			int age = Integer.parseInt(tempAge);
			int sector = Integer.parseInt(tempSector);
			EmployeeDAO.instance.createEmployee(name, age, sector);
			return Response.status(Response.Status.CREATED).build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}
}
