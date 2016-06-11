package org.artJava.protocol.http.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
public class HRoot {

    @GET
    public Response get() {
        return Response.ok("Welcome to PPC API. ").type(MediaType.TEXT_PLAIN_TYPE).build();
    }
    
}
