/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.consulting.rmigoeshttp.step_2_complex.data;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;

/**
 *
 * @author thorsten
 */
public class CustomType implements Serializable {

    int a = 0;
    int b = 0;
    int c = 0;

    CustomSubType t = null;

    String speech;

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    public int getC() {
        return c;
    }

    public void setC(int c) {
        this.c = c;
    }

    public CustomSubType getT() {
        return t;
    }

    public void setT(CustomSubType t) {
        this.t = t;
    }

    public String getSpeech() {
        return speech;
    }

    public void setSpeech(String speech) {
        this.speech = speech;
    }

    public void talk() throws IOException {
        ProcessBuilder b = new ProcessBuilder("/usr/bin/say", speech);
        b.start();
    }

}
