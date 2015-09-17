/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.consulting.rmigoeshttp.step_1_easy.rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author thorsten
 */
public class Client {
    public static void main(String args[]) throws Exception {
        Registry registry = LocateRegistry.getRegistry("localhost");
        RMIRemoteInterface obj = (RMIRemoteInterface) registry.lookup("RMIServerEasy");
        System.out.println("5+6="+obj.add(5,6));
        System.out.println("trimmed="+obj.trim("long text with spaces at the end                  ")+"!!!");
        obj.say("-v RMI Client Demo done");
        
    }

}
