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
public class HttpClientBetter extends HttpServiceClientHelper {

    private static final transient Logger LOG = Logger.getLogger(HttpClientBetter.class);

    public HttpClientBetter() throws SSLConfigFactoryException, FileNotFoundException {
        super("http://localhost:8081/complex/service/");
    }

    public CustomType doStuff(CustomType ct) throws RemoteException, TextTooLongException, ClientSideCommunicationException {
        try {
            final MultivaluedMapImpl queryParams = new MultivaluedMapImpl();
            queryParams.add("complexObject", Converter.serialiseToString(ct));
            try {
                return super.getResponsePOST("doComplexOperation", queryParams, CustomType.class);
            } catch (final UniformInterfaceException ex) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("exception during request, remapping", ex);
                }
                
                final ClientResponse response = ex.getResponse();
                if (HttpStatus.SC_PRECONDITION_FAILED == response.getStatus()) {
                    final TextTooLongException ttlEx = ServerExceptionMapper.fromResponse(response, TextTooLongException.class);
                    if (ttlEx == null) {
                        throw ex;
                    } else {
                        throw ttlEx;
                    }
                } else {
                    final RemoteException remEx = ServerExceptionMapper.fromResponse(response, RemoteException.class);
                    if (remEx == null) {
                        throw ex;
                    } else {
                        throw remEx;
                    }
                }
            }
        } catch (IOException iOException) {
            throw new ClientSideCommunicationException("ioError",iOException);
        } catch (ClassNotFoundException classNotFoundException) {
            throw new ClientSideCommunicationException("classNotFound",classNotFoundException);
        } 

    }

    public static void main(String[] args) throws Exception {
        HttpClientBetter c = new HttpClientBetter();

        CustomSubType cst = new CustomSubType();
        cst.setInput("Looonng String with trailing spaces                    ");
        CustomType ct = new CustomType();
        ct.setA(5);
        ct.setB(8);
        ct.setSpeechCommand("/usr/bin/saay");
        ct.setSpeech("My Text");
        System.out.println(ct.getSpeech() + " ist " + ct.getSpeech().length() + "lang");

        ct.setT(cst);
        CustomType res = c.doStuff(ct);

        System.out.println("5+8=" + res.getC());
        System.out.println("trimmed=" + res.getT().getOutput() + "!!!");
//        Thread.sleep(1000);
//        System.out.println("Can even talk locally");
//        res.setSpeechCommand("/usr/bin/say");
//
//        res.talk();
    }
}
