/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.consulting.rmigoeshttp.step_1_easy.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author thorsten
 */
public interface RMIRemoteInterface extends Remote {
	public String trim(String s) throws RemoteException;
	public void say(String s) throws RemoteException;
     	public int add(int a, int b) throws RemoteException;

}