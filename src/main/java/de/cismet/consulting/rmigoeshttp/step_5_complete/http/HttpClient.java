/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.consulting.rmigoeshttp.step_5_complete.http;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import de.cismet.consulting.rmigoeshttp.step_5_complete.data.CustomSubType;
import de.cismet.consulting.rmigoeshttp.step_5_complete.data.CustomType;
import de.cismet.consulting.rmigoeshttp.step_5_complete.data.TextTooLongException;
import de.cismet.consulting.rmigoeshttp.tools.Converter;
import de.cismet.consulting.rmigoeshttp.tools.HttpServiceClientHelper;
import de.cismet.consulting.rmigoeshttp.tools.SSLConfigFactoryException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.log4j.Logger;

/**
 *
 * @author thorsten
 */
public class HttpClient extends HttpServiceClientHelper {

    private static final transient Logger LOG = Logger.getLogger(HttpClient.class);

    public HttpClient() throws SSLConfigFactoryException, FileNotFoundException {
        super("http://localhost:8081/complex/service/");
    }

    public CustomType doStuff(CustomType ct) throws IOException, ClassNotFoundException, RemoteException, TextTooLongException, ClientSideCommunicationException {
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
        ct.setSpeechCommand("/usr/bin/say");
        ct.setSpeech("My Text");
        System.out.println(ct.getSpeech() + " ist " + ct.getSpeech().length() + "lang");

        ct.setT(cst);
        CustomType res = c.doStuff(ct);

        System.out.println("5+8=" + res.getC());
        System.out.println("trimmed=" + res.getT().getOutput() + "!!!");
//        Thread.sleep(1000);
//        System.out.println("Can even talk locally");
//        res.talk();
    }
}
