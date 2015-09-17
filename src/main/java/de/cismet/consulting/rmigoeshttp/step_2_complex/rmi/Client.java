/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.consulting.rmigoeshttp.step_2_complex.rmi;

import de.cismet.consulting.rmigoeshttp.step_2_complex.data.CustomSubType;
import de.cismet.consulting.rmigoeshttp.step_2_complex.data.CustomType;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author thorsten
 */
public class Client {
    public static void main(String args[]) throws Exception {
        Registry registry = LocateRegistry.getRegistry("localhost");
        RMIRemoteInterface obj = (RMIRemoteInterface) registry.lookup("RMIServerComplex");
        CustomSubType cst=new CustomSubType();
        cst.setInput("Looonng String with trailing spaces                    ");
        
        CustomType ct=new CustomType();
        ct.setA(5);
        ct.setB(8);
        ct.setSpeech("My Text");
        ct.setT(cst);
        CustomType res=obj.doStuff(ct);
        
        
        System.out.println("5+8="+ res.getC());
        System.out.println("trimmed="+res.getT().getOutput()+"!!!");
        Thread.sleep(1000);
        System.out.println("Can even talk locally");
        res.talk();
    }

}
