/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.consulting.rmigoeshttp.step_0_simple.http;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author thorsten
 */
@Path("/simple/service") // NOI18N
public class HttpService {

    @GET
    @Path("/helloWorld")
    @Produces(MediaType.TEXT_HTML)
    public Response helloWorld() {
        System.out.println("helloWorld called (HTML)");
        return Response.ok("Hello HTTP-World").build();
    }

    @GET
    @Path("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    public Response helloPlainWorld() {
        System.out.println("hello called");
        return Response.ok("Hello World").build();
    }

    @GET
    @Path("/ping")
    @Produces(MediaType.TEXT_PLAIN)
    public Response ping() {
        System.out.println("ping called");
        return Response.ok("pong").build();
    }

}
