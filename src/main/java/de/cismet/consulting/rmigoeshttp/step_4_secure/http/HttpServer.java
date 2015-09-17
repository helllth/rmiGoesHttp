/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.consulting.rmigoeshttp.step_4_secure.http;

import com.sun.jersey.spi.container.servlet.ServletContainer;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.jetty.security.SslSocketConnector;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;

/**
 *
 * @author thorsten
 */
public class HttpServer {
    private static final int HEADER_BUFFER_SIZE = 512 * 1024; // = 512kb
    private static final transient Logger LOG = Logger.getLogger(HttpServer.class);

    //~ Instance fields --------------------------------------------------------

    private final transient int port;
    private final transient org.mortbay.jetty.Server server;
    private HttpServer() {
        final Map<String, String> initParams = new HashMap<String, String>();
        initParams.put("com.sun.jersey.config.property.packages", "de.cismet.consulting.rmigoeshttp.step_2_complex.http"); // NOI18N

        final ServletHolder servlet = new ServletHolder(ServletContainer.class);
        servlet.setInitParameters(initParams);
        Connector con=new SocketConnector(); //unverschluesselte Verbindung!!!

        this.port = 8081;
        server = new org.mortbay.jetty.Server();
         try {
                final SslSocketConnector ssl = new SslSocketConnector();
                ssl.setMaxIdleTime(30000);
                ssl.setKeystore("/Users/thorsten/dev/810-rmiGoesHttp/server.localhost.demo.keystore");
                ssl.setPassword("123demostorepass");
                ssl.setKeyPassword("123demokeypass");
                ssl.setWantClientAuth(false);
                ssl.setNeedClientAuth(false);

                con = ssl;
            } catch (final Exception e) {
                final String message = "cannot initialise SSL connector"; // NOI18N
                LOG.error(message, e);
                throw new RuntimeException(message, e);
            }
        con.setPort(port);
        server.addConnector(con);

        final Context context = new Context(server, "/", Context.SESSIONS); // NOI18N
        context.addServlet(servlet, "/*");                                  // NOI18N

        try {
            server.start();
            System.in.read();
            server.setStopAtShutdown(true);
            System.exit(0);
        } catch (final Exception ex) {
            final String message = "could not create jetty web container on port: " + port; // NOI18N
            LOG.error(message, ex);
            throw new RuntimeException(message, ex);
        }
    }
    public static void main(String[] args) {
        HttpServer s=new HttpServer();
    }
    
   
}
