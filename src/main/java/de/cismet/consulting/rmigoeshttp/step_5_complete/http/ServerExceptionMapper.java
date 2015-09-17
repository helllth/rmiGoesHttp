/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.consulting.rmigoeshttp.step_5_complete.http;

import com.sun.jersey.api.client.ClientResponse;
import de.cismet.consulting.rmigoeshttp.step_5_complete.data.TextTooLongException;
import de.cismet.consulting.rmigoeshttp.tools.Converter;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.log4j.Logger;

import java.io.IOException;

import java.rmi.RemoteException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


public final class ServerExceptionMapper {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient Logger LOG = Logger.getLogger(ServerExceptionMapper.class);

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   t        DOCUMENT ME!
     * @param   builder  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static Response toResponse(final Throwable t, final Response.ResponseBuilder builder) {
        final Response.ResponseBuilder response;
        if (builder == null) {
            response = Response.serverError();
        } else {
            response = builder;
        }

        if (t != null) {
            try {
                response.entity(Converter.serialiseToString(t));
            } catch (final IOException ex) {
                LOG.error("could not serialise throwable", ex); // NOI18N
            }
        }

        return response.build();
    }

    /**
     * DOCUMENT ME!
     *
     * @param   <T>       DOCUMENT ME!
     * @param   response  DOCUMENT ME!
     * @param   type      DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static <T extends Throwable> T fromResponse(final ClientResponse response, final Class<T> type) {
        if (response != null) {
            try {
                return Converter.deserialiseFromString(response.getEntity(String.class), type);
            } catch (final Exception e) {
                LOG.warn("could not deserialise throwable", e); // NOI18N
            }
        }

        return null;
    }

    
    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    @Provider
    public static final class RemoteExceptionMapper implements ExceptionMapper<RemoteException> {

        //~ Methods ------------------------------------------------------------

        @Override
        public Response toResponse(final RemoteException e) {
            final Response.ResponseBuilder builder = Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR);

            return ServerExceptionMapper.toResponse(e, builder);
        }
    } 
    
    @Provider
    public static final class TextTooLongExceptionMapper implements ExceptionMapper<TextTooLongException> {

        //~ Methods ------------------------------------------------------------

        @Override
        public Response toResponse(final TextTooLongException e) {
            final Response.ResponseBuilder builder = Response.status(HttpStatus.SC_PRECONDITION_FAILED);

            return ServerExceptionMapper.toResponse(e, builder);
        }
    }
}
