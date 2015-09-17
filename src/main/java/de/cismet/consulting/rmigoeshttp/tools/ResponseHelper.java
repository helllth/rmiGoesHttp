/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.consulting.rmigoeshttp.tools;

import java.io.IOException;
import javax.ws.rs.core.Response;

/**
 *
 * @author thorsten
 */
public class ResponseHelper {

    private ResponseHelper() {

    }

    public static Response createResponse(final Object o) throws IOException {
        return Response.ok(Converter.serialiseToBase64(o)).build();
    }

    public static Response createResponse(final Object o, final String type) throws IOException {
        return Response.ok(Converter.serialiseToBase64(o)).type(type).build();
    }
}
