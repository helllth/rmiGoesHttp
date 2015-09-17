/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.consulting.rmigoeshttp.step_1_easy.http;

import java.io.IOException;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author thorsten
 */
@Path("/easy/service") // NOI18N
public class HttpService {
    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
   public Response add(@FormParam("a") int a, @FormParam("b") int b) {
       return Response.ok(new Integer(a+b).toString()).build();
    }
    
    @POST
    @Path("/trim")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response trim(@FormParam("stringToTrim") String s) {
               return Response.ok(s.trim()).build();
    }
    
    @POST
    @Path("/say")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response say(@FormParam("speechText") String s) {
        System.out.println("should say: "+s);  
        try {
            ProcessBuilder b=new ProcessBuilder("/usr/bin/say",s);
            b.start();
            return Response.ok().build();
        } catch (IOException ex) {
            return Response.serverError().build();
        }
    }
    
    
}
