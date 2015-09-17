/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.consulting.rmigoeshttp.step_2_complex.rmi;

import de.cismet.consulting.rmigoeshttp.step_2_complex.data.CustomType;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author thorsten
 */
public interface RMIRemoteInterface extends Remote {
	public CustomType doStuff(CustomType ct) throws RemoteException;
}