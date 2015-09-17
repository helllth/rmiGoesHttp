/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.consulting.rmigoeshttp.step_0_simple.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author thorsten
 */
public interface RMIRemoteInterface extends Remote {
	public String getMessage() throws RemoteException;
}