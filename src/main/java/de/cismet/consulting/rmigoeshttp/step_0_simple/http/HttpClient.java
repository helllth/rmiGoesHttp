/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.consulting.rmigoeshttp.step_0_simple.http;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.client.apache.ApacheHttpClient;
import com.sun.jersey.client.apache.config.DefaultApacheHttpClientConfig;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

/**
 *
 * @author thorsten
 */
public class HttpClient {

    Client client;

    public HttpClient() {
        final DefaultApacheHttpClientConfig config = new DefaultApacheHttpClientConfig();
        String proxyHost = "myproxy.intra.org";
        String proxyPort = "3128";

        System.out.println(" using proxy: " + proxyHost + ":" + proxyPort);
        config.getProperties().put(DefaultApacheHttpClientConfig.PROPERTY_PROXY_URI, "http://" + proxyHost + ":" + proxyPort);

        client = ApacheHttpClient.create(config);

    }

    public String helloPlainWorld() {
        final UriBuilder uriBuilder = UriBuilder.fromPath("http://localhost:8081/simple/service/hello");
        WebResource.Builder wrb = client.resource(uriBuilder.build()).accept(MediaType.WILDCARD);

        String s = wrb.get(String.class);
        return s;
    }

    public String ping() {
        final UriBuilder uriBuilder = UriBuilder.fromPath("http://localhost:8081/simple/service/ping");
        WebResource.Builder wrb = client.resource(uriBuilder.build()).accept(MediaType.WILDCARD);

        String s = wrb.get(String.class);
        return s;
    }

    public static void main(String[] args) {
        HttpClient c = new HttpClient();
        System.out.println("Clientausgabe hello: " + c.helloPlainWorld());
        System.out.println("Clientausgabe ping: " + c.ping());
    }
}
