/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.consulting.rmigoeshttp.step_0_simple.rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author thorsten
 */
public class Server implements RMIRemoteInterface{
    public static final String MESSAGE = "Hello world";
 
    public Server() throws RemoteException {
    	
    }
 
    public String getMessage() throws RemoteException {
        return MESSAGE;
    }
 
    public static void main(String args[]) throws Exception {

//    	if(System.getSecurityManager()==null){
//            System.setSecurityManager(new RMISecurityManager());
//    	}
        System.out.println("RMI server started");
        
        //Instantiate RmiServer
        Server obj = new Server();
 
        try { //special exception handler for registry creation
        	
            RMIRemoteInterface stub = (RMIRemoteInterface) UnicastRemoteObject.exportObject(obj,0);
            Registry reg;
            try {
            	reg = LocateRegistry.createRegistry(1099);
                System.out.println("java RMI registry created.");

            } catch(Exception e) {
            	System.out.println("Using existing registry");
            	reg = LocateRegistry.getRegistry();
            }
        	reg.rebind("RMIServerEasy", stub);

        } catch (RemoteException e) {
        	e.printStackTrace();
        }
    }
}

