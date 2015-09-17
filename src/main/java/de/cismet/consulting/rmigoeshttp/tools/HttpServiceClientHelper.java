/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.consulting.rmigoeshttp.tools;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.client.apache.ApacheHttpClient;
import com.sun.jersey.client.apache.config.ApacheHttpClientConfig;
import com.sun.jersey.client.apache.config.DefaultApacheHttpClientConfig;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import org.apache.log4j.Logger;

/**
 *
 * @author thorsten
 */
public class HttpServiceClientHelper {
    private static final transient Logger LOG = Logger.getLogger(HttpServiceClientHelper.class);

    private static final String MULTITHREADEDHTTPCONNECTION_IGNORE_EXCEPTION
            = "Interrupted while waiting in MultiThreadedHttpConnectionManager";
    private static final int TIMEOUT = 10000;

    //~ Instance fields --------------------------------------------------------
    private final transient String rootResource;
    private final transient Map<String, Client> clientCache;

    private final transient Proxy proxy;
/**
     * Creates a new RESTfulSerialInterfaceConnector object.
     *
     * @param  rootResource  DOCUMENT ME!
     */
    public HttpServiceClientHelper(final String rootResource) {
        this(rootResource, null, null);
    }

    /**
     * Creates a new RESTfulSerialInterfaceConnector object.
     *
     * @param  rootResource  DOCUMENT ME!
     * @param  proxy         config proxyURL DOCUMENT ME!
     */
    public HttpServiceClientHelper(final String rootResource, final Proxy proxy) {
        this(rootResource, proxy, null);
    }

    /**
     * Creates a new RESTfulSerialInterfaceConnector object.
     *
     * @param  rootResource  DOCUMENT ME!
     * @param  sslConfig     DOCUMENT ME!
     */
    public HttpServiceClientHelper(final String rootResource, final SSLConfig sslConfig) {
        this(rootResource, null, sslConfig);
    }

    /**
     * Creates a new RESTfulSerialInterfaceConnector object.
     *
     * @param  rootResource  DOCUMENT ME!
     * @param  proxy         proxyConfig proxyURL DOCUMENT ME!
     * @param  sslConfig     DOCUMENT ME!
     */
    public HttpServiceClientHelper(final String rootResource,
            final Proxy proxy,
            final SSLConfig sslConfig) {
        if (sslConfig == null) {
            LOG.warn("cannot initialise ssl because sslConfig is null"); // NOI18N
        } else {
            initSSL(sslConfig);
        }

        // add training '/' to the root resource if not present
        if ('/' == rootResource.charAt(rootResource.length() - 1)) {
            this.rootResource = rootResource;
        } else {
            this.rootResource = rootResource + "/"; // NOI18N
        }

        if (proxy == null) {
            this.proxy = new Proxy();
        } else {
            this.proxy = proxy;
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("using proxy: " + proxy); // NOI18N
        }

        clientCache = new HashMap<String, Client>();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   sslConfig  DOCUMENT ME!
     *
     * @throws  IllegalStateException  DOCUMENT ME!
     */
    private void initSSL(final SSLConfig sslConfig) {
        if (LOG.isInfoEnabled()) {
            LOG.info("initialising ssl connection: " + sslConfig); // NOI18N
        }

        try {
            // server certificate for trustmanager
            // should never be null because otherwise the client cannot be sure to be communicating with the correct
            // server
            final TrustManagerFactory tmf;
            if (sslConfig.getServerKeystore() == null) {
                tmf = null;
                LOG.info("no server certificates provided by SSLConfig"); // NOI18N
            } else {
                tmf = TrustManagerFactory.getInstance(SSLConfig.TMF_SUNX509);
                tmf.init(sslConfig.getServerKeystore());
            }

            // client certificate and key for key manager
            // if server does not require client authentication there is no need to provide a client keystore
            final KeyManagerFactory kmf;
            if (sslConfig.getClientKeystore() == null) {
                kmf = null;
            } else {
                kmf = KeyManagerFactory.getInstance(SSLConfig.TMF_SUNX509);
                kmf.init(sslConfig.getClientKeystore(), sslConfig.getClientKeyPW());
            }

            // init context
            final SSLContext context = SSLContext.getInstance(SSLConfig.CONTEXT_TYPE_TLS);

            // Use the CidsTrustManager to validate the default certificates and the cismet certificate
            final CidsTrustManager trustManager;
            X509TrustManager cidsManager = null;
            TrustManager[] trustManagerArray = null;

            if ((tmf != null) && (tmf.getTrustManagers() != null) && (tmf.getTrustManagers().length == 1)) {
                if (tmf.getTrustManagers()[0] instanceof X509TrustManager) {
                    cidsManager = (X509TrustManager)tmf.getTrustManagers()[0];
                }
            }

            try {
                trustManager = new CidsTrustManager(cidsManager);
                trustManagerArray = new TrustManager[] { trustManager };
            } catch (Exception e) {
                LOG.error("Cannot create CidsTrustManager.", e);
                trustManagerArray = (tmf == null) ? null : tmf.getTrustManagers();
            }

            context.init(
                (kmf == null) ? null : kmf.getKeyManagers(),
                trustManagerArray,
                null);

            SSLContext.setDefault(context);
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new SSLHostnameVerifier());
        } catch (final NoSuchAlgorithmException e) {
            throw new IllegalStateException("system does not support SSL", e);            // NOI18N
        } catch (final KeyStoreException e) {
            throw new IllegalStateException("system does not support java keystores", e); // NOI18N
        } catch (final KeyManagementException e) {
            throw new IllegalStateException("ssl context init properly initialised", e);  // NOI18N
        } catch (final UnrecoverableKeyException e) {
            throw new IllegalStateException("cannot get key from keystore", e);           // NOI18N
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getRootResource() {
        return rootResource;
    }

    /**
     * Creates a {@link WebResource.Builder} from the given path. Equal to <code>createWebResourceBuilder(path,
     * null)</code>.
     *
     * @param   path  the path relative to the root resource
     *
     * @return  a <code>WebResource.Builder</code> ready to perform an operation (GET, POST, PUT...)
     *
     * @see     #createWebResourceBuilder(java.lang.String, java.util.Map)
     */
    public WebResource.Builder createWebResourceBuilder(final String path) {
        return createWebResourceBuilder(path, null);
    }

    /**
     * Creates a {@link WebResource.Builder} from the given path and the given params. The given path will be appended
     * to the root path of this connector, thus shall denote a path relative to the root resource. The given {@link Map}
     * of queryParams will be appended to the query.
     *
     * @param   path         the path relative to the root resource
     * @param   queryParams  parameters of the query, may be null or empty.
     *
     * @return  a <code>WebResource.Builder</code> ready to perform an operation (GET, POST, PUT...)
     */
    public WebResource.Builder createWebResourceBuilder(final String path, final Map<String, String> queryParams) {
        // remove leading '/' if present
        final String resource;
        if (path == null) {
            resource = rootResource;
        } else if ('/' == path.charAt(0)) {
            resource = rootResource + path.substring(1, path.length() - 1);
        } else {
            resource = rootResource + path;
        }

        // create new client and webresource from the given resource
        if (!clientCache.containsKey(path)) {
            final DefaultApacheHttpClientConfig clientConfig = new DefaultApacheHttpClientConfig();
            if (proxy.isEnabled()) {
                if ((proxy.getHost() != null) && (proxy.getPort() > 0)) {
                    clientConfig.getProperties()
                            .put(
                                ApacheHttpClientConfig.PROPERTY_PROXY_URI,
                                "http://"
                                + proxy.getHost()
                                + ":"
                                + proxy.getPort());
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("proxy set: " + proxy);
                    }

                    if ((proxy.getUsername() != null) && (proxy.getPassword() != null)) {
                        clientConfig.getState()
                                .setProxyCredentials(
                                    null,
                                    proxy.getHost(),
                                    proxy.getPort(),
                                    proxy.getUsername(),
                                    proxy.getPassword(),
                                    proxy.getDomain(),
                                    "");
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("proxy credentials set: " + proxy);
                        }
                    }
                }
            }

            clientConfig.getProperties().put(ClientConfig.PROPERTY_CONNECT_TIMEOUT, TIMEOUT);
            clientCache.put(path, ApacheHttpClient.create(clientConfig));
        }

        final Client c = clientCache.get(path);
        final UriBuilder uriBuilder = UriBuilder.fromPath(resource);

        // add all query params that are present
        if (queryParams != null) {
            for (final Map.Entry<String, String> entry : queryParams.entrySet()) {
                uriBuilder.queryParam(entry.getKey(), entry.getValue());
            }
        }

        final WebResource wr = c.resource(uriBuilder.build());

        // this is the binary interface so we accept the octet stream type only
        return wr.type(MediaType.APPLICATION_FORM_URLENCODED_TYPE).accept(MediaType.APPLICATION_OCTET_STREAM_TYPE);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   <T>   DOCUMENT ME!
     * @param   path  DOCUMENT ME!
     * @param   type  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  IOException             DOCUMENT ME!
     * @throws  ClassNotFoundException  DOCUMENT ME!
     */
    protected <T> T getResponsePOST(final String path, final Class<T> type) throws IOException, ClassNotFoundException {
        final WebResource.Builder builder = createWebResourceBuilder(path);

        return getResponsePOST(builder, type, null);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   <T>        DOCUMENT ME!
     * @param   path       DOCUMENT ME!
     * @param   queryData  DOCUMENT ME!
     * @param   type       DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  IOException             DOCUMENT ME!
     * @throws  ClassNotFoundException  DOCUMENT ME!
     */
    protected <T> T getResponsePOST(final String path, final Map queryData, final Class<T> type) throws IOException,
        ClassNotFoundException {
        final WebResource.Builder builder = createWebResourceBuilder(path);

        return getResponsePOST(builder, type, queryData);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   <T>        DOCUMENT ME!
     * @param   builder    DOCUMENT ME!
     * @param   type       DOCUMENT ME!
     * @param   queryData  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  IOException             DOCUMENT ME!
     * @throws  ClassNotFoundException  DOCUMENT ME!
     * @throws  IllegalStateException   DOCUMENT ME!
     */
    protected <T> T getResponsePOST(final WebResource.Builder builder,
            final Class<T> type,
            final Map queryData) throws IOException, ClassNotFoundException {
        if ((builder == null) || (type == null)) {
            throw new IllegalStateException("neither builder nor type may be null"); // NOI18N
        }

        try {
            final byte[] bytes = builder.post(byte[].class, queryData);
            return Converter.deserialiseFromBase64(bytes, type);
        } catch (final RuntimeException ex) {
            if ((ex.getCause() != null) && (ex.getCause() instanceof IllegalThreadStateException)) {
                if (ex.getCause().getMessage().equals(MULTITHREADEDHTTPCONNECTION_IGNORE_EXCEPTION)) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug(
                            "ignoring \""
                                    + MULTITHREADEDHTTPCONNECTION_IGNORE_EXCEPTION
                                    + "\" IllegalThreadStateException",
                            ex);
                    }
                } else {
                    LOG.warn("Error while querying request", ex);
                }
                return null;
            } else {
                throw ex;
            }
        }
    }
}
