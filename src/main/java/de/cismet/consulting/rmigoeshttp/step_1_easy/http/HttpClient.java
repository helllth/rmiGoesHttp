/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.consulting.rmigoeshttp.step_1_easy.http;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.client.apache.ApacheHttpClient;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import java.rmi.RemoteException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

/**
 *
 * @author thorsten
 */
public class HttpClient {

    Client client;

    public HttpClient() {
        client = new ApacheHttpClient();
    }

    public String trim(String s) {
        final MultivaluedMapImpl queryParams = new MultivaluedMapImpl();
        queryParams.add("stringToTrim", s);
        final UriBuilder uriBuilder = UriBuilder.fromPath("http://localhost:8081/easy/service/trim");
        WebResource.Builder wrb = client.resource(uriBuilder.build()).accept(MediaType.WILDCARD);
        String result = wrb.post(String.class, queryParams);
        return result;
    }

    public void say(String s) {
        final MultivaluedMapImpl queryParams = new MultivaluedMapImpl();
        queryParams.add("speechText", s);
        final UriBuilder uriBuilder = UriBuilder.fromPath("http://localhost:8081/easy/service/say");
        WebResource.Builder wrb = client.resource(uriBuilder.build()).accept(MediaType.WILDCARD);
        wrb.post(queryParams);
    }

    public int add(int a, int b) {
        final MultivaluedMapImpl queryParams = new MultivaluedMapImpl();
        queryParams.add("a", a);
        queryParams.add("b", b);
        final UriBuilder uriBuilder = UriBuilder.fromPath("http://localhost:8081/easy/service/add");
        WebResource.Builder wrb = client.resource(uriBuilder.build()).accept(MediaType.WILDCARD);
        String result = wrb.post(String.class, queryParams);
        return new Integer(result);
    }

    public static void main(String[] args) {
        HttpClient c = new HttpClient();
        System.out.println("5+7=" + c.add(5, 7));
        System.out.println("trimmed=" + c.trim("long text with spaces at the end                  ") + "!!!");
        c.say("Http Client Demo done");
    }
}
