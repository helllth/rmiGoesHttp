/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.consulting.rmigoeshttp.step_6_clientsidecerts.http;

import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import de.cismet.consulting.rmigoeshttp.step_2_complex.data.CustomSubType;
import de.cismet.consulting.rmigoeshttp.step_2_complex.data.CustomType;
import de.cismet.consulting.rmigoeshttp.tools.Converter;
import de.cismet.consulting.rmigoeshttp.tools.HttpServiceClientHelper;
import de.cismet.consulting.rmigoeshttp.tools.SSLConfigFactory;
import de.cismet.consulting.rmigoeshttp.tools.SSLConfigFactoryException;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.log4j.Logger;

/**
 *
 * @author thorsten
 */
public class HttpClient extends HttpServiceClientHelper {

    private static final transient Logger LOG = Logger.getLogger(HttpClient.class);

    public HttpClient() throws SSLConfigFactoryException, FileNotFoundException {
        super(
                "https://localhost:8081/complex/service/",
                SSLConfigFactory.getDefault().createClientConfig(
                        "/Users/thorsten/dev/810-rmiGoesHttp/server.cert.der", 
                        "/Users/thorsten/dev/810-rmiGoesHttp/client.keystore", 
                        "123456".toCharArray(), 
                        "123456".toCharArray())
        );
    }

    public String ping(){
        WebResource.Builder wrb=super.createWebResourceBuilder("ping");
        String s=wrb.get(String.class);
        return s;
    }
    
    
    public CustomType doStuff(CustomType ct) throws IOException, ClassNotFoundException {
        final MultivaluedMapImpl queryParams = new MultivaluedMapImpl();
        queryParams.add("complexObject", Converter.serialiseToString(ct));
        return super.getResponsePOST("doComplexOperation", queryParams, CustomType.class);
    }

    public static void main(String[] args) throws Exception {
        HttpClient c = new HttpClient();

        CustomSubType cst = new CustomSubType();
        cst.setInput("Looonng String with trailing spaces                    ");

        CustomType ct = new CustomType();
        ct.setA(5);
        ct.setB(8);
        ct.setSpeech("Yes");
        ct.setT(cst);
        CustomType res = c.doStuff(ct);

        System.out.println("5+8=" + res.getC());
        System.out.println("trimmed=" + res.getT().getOutput() + "!!!");
        
    }
    
}
