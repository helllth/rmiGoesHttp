/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.consulting.rmigoeshttp.step_2_complex.http;

import de.cismet.consulting.rmigoeshttp.step_2_complex.data.CustomType;
import de.cismet.consulting.rmigoeshttp.tools.Converter;
import java.io.IOException;
import java.rmi.RemoteException;
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
@Path("/complex/service") // NOI18N
public class HttpService {

    @POST
    @Path("/doComplexOperation")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response op(@FormParam("complexObject") String complexObjectBytesAsString) {
        try {
            CustomType ct = Converter.deserialiseFromString(complexObjectBytesAsString, CustomType.class);
            ct.setC(ct.getA() + ct.getB());
            ct.getT().setOutput(ct.getT().getInput().trim());

            ct.talk();
            return Response.ok(Converter.serialiseToBase64(ct)).build();

        } catch (Exception ex) {
            return Response.serverError().build();
        }

    }

}
