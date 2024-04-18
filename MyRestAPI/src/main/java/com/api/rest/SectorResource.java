package com.api.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/sectors")
public class SectorResource {

    private static List<Sector> sectors = new ArrayList<>();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Sector> getSectors() {
        return sectors;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addSector(Sector sector) {
        sectors.add(sector);
        return Response.status(Response.Status.CREATED).entity(sector).build();
    }

}
