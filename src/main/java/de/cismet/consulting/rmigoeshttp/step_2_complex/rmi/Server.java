/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.consulting.rmigoeshttp.step_2_complex.rmi;

import de.cismet.consulting.rmigoeshttp.step_2_complex.data.CustomType;
import java.io.IOException;
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


    @Override
    public CustomType doStuff(CustomType ct) throws RemoteException {
        ct.setC(ct.getA()+ct.getB());
        
        ct.getT().setOutput(ct.getT().getInput().trim());
        try {
            ct.talk();
        } catch (IOException ex) {
            throw new RemoteException("no say for you",ex);
        }
        
        return ct;
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
        	reg.rebind("RMIServerComplex", stub);

        } catch (RemoteException e) {
        	e.printStackTrace();
        }
    }
}

