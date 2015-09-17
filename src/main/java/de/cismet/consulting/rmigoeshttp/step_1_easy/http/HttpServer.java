/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.consulting.rmigoeshttp.step_1_easy.http;

import com.sun.jersey.spi.container.servlet.ServletContainer;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.bio.SocketConnector;
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
        initParams.put("com.sun.jersey.config.property.packages", "de.cismet.consulting.rmigoeshttp.step_1_easy.http"); // NOI18N

        final ServletHolder servlet = new ServletHolder(ServletContainer.class);
        servlet.setInitParameters(initParams);

        this.port = 8081;
        server = new org.mortbay.jetty.Server();
        Connector con=new SocketConnector(); //unverschluesselte Verbindung!!!
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
