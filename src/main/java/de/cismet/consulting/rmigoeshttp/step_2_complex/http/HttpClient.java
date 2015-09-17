/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.consulting.rmigoeshttp.step_2_complex.http;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.client.apache.ApacheHttpClient;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import de.cismet.consulting.rmigoeshttp.step_2_complex.data.CustomSubType;
import de.cismet.consulting.rmigoeshttp.step_2_complex.data.CustomType;
import de.cismet.consulting.rmigoeshttp.tools.Converter;
import java.io.IOException;
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

    public CustomType doStuff(CustomType ct) throws IOException, ClassNotFoundException{
        final MultivaluedMapImpl queryParams = new MultivaluedMapImpl();
        queryParams.add("complexObject",Converter.serialiseToString(ct));
        final UriBuilder uriBuilder = UriBuilder.fromPath("http://localhost:8081/complex/service/doComplexOperation");
        WebResource.Builder wrb = client.resource(uriBuilder.build()).accept(MediaType.WILDCARD);
        byte[] result = wrb.post(byte[].class, queryParams);
        return Converter.deserialiseFromBase64(result, CustomType.class);
    }

    

    public static void main(String[] args) throws Exception{
        HttpClient c = new HttpClient();
        
         CustomSubType cst=new CustomSubType();
        cst.setInput("Looonng String with trailing spaces                    ");
        
        CustomType ct=new CustomType();
        ct.setA(5);
        ct.setB(8);
        ct.setSpeech("My Text");
        ct.setT(cst);
        CustomType res=c.doStuff(ct);
        
        
        System.out.println("5+8="+ res.getC());
        System.out.println("trimmed="+res.getT().getOutput()+"!!!");
        Thread.sleep(1000);
        System.out.println("Can even talk locally");
        res.talk();
    }
}
