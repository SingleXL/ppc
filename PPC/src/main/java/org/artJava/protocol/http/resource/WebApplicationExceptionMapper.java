package org.artJava.protocol.http.resource;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.google.gson.GsonBuilder;
@Provider
public class WebApplicationExceptionMapper implements ExceptionMapper<WebApplicationException> {

    @Override
    public Response toResponse(WebApplicationException exception) {
        int statusCode = exception.getResponse().getStatus();
        Response.Status status = Response.Status.fromStatusCode(statusCode);
        return response(status);
    }

    public static Response response(Response.Status status) {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("status", String.valueOf(status.getStatusCode()));
        map.put("message", status.getReasonPhrase());
        map.put("family", status.getFamily().name());
        return Response
                .status(status)
                .entity(new GsonBuilder()
                        .setPrettyPrinting()
                        .create()
                        .toJson(map))
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build();
    }
}
