/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.consulting.rmigoeshttp.step_4_secure.http;

import com.sun.jersey.core.util.MultivaluedMapImpl;
import de.cismet.consulting.rmigoeshttp.step_2_complex.data.CustomSubType;
import de.cismet.consulting.rmigoeshttp.step_2_complex.data.CustomType;
import de.cismet.consulting.rmigoeshttp.tools.Converter;
import de.cismet.consulting.rmigoeshttp.tools.HttpServiceClientHelper;
import de.cismet.consulting.rmigoeshttp.tools.SSLConfigFactory;
import de.cismet.consulting.rmigoeshttp.tools.SSLConfigFactoryException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author thorsten
 */
public class HttpClient extends HttpServiceClientHelper {

    public HttpClient() throws SSLConfigFactoryException, FileNotFoundException {
        super("https://localhost:8081/complex/service/", 
                SSLConfigFactory.getDefault().createClientConfig(new FileInputStream("/Users/thorsten/dev/810-rmiGoesHttp/server.localhost.demo.cert")));
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
        ct.setSpeech("My Text");
        ct.setT(cst);
        CustomType res = c.doStuff(ct);

        System.out.println("5+8=" + res.getC());
        System.out.println("trimmed=" + res.getT().getOutput() + "!!!");
        Thread.sleep(1000);
        System.out.println("Can even talk locally");
        res.talk();
    }
}

