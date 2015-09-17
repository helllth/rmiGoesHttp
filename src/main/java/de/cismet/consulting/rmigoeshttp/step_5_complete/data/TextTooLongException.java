/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.consulting.rmigoeshttp.step_5_complete.data;

/**
 *
 * @author thorsten
 */
public class TextTooLongException extends Exception{

    public TextTooLongException() {
    }

    public TextTooLongException(String message) {
        super(message);
    }

    public TextTooLongException(String message, Throwable cause) {
        super(message, cause);
    }

    public TextTooLongException(Throwable cause) {
        super(cause);
    }
    
}
