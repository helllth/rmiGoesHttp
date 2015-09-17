/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.consulting.rmigoeshttp.step_5_complete.http;

/**
 *
 * @author thorsten
 */
public class ClientSideCommunicationException extends Exception{

    public ClientSideCommunicationException() {
    }

    public ClientSideCommunicationException(String message) {
        super(message);
    }

    public ClientSideCommunicationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClientSideCommunicationException(Throwable cause) {
        super(cause);
    }

    public ClientSideCommunicationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}
