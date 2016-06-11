package org.artJava.protocol.http.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.artJava.protocol.http.service.NodeMasterHttpService;

import com.google.gson.GsonBuilder;

@Path("executors")
public class HExecutors {

    @GET
    public Response get() {
        return Response
                .ok(new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .create()
                .toJson(NodeMasterHttpService
                        .getInstance()
                        .getMaster()
                        .getExecutors()))
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build();
    }
}
