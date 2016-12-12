/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Trader;

import java.rmi.Remote;
import java.rmi.RemoteException;
import marketplace.Item;

/**
 *
 * @author Nizam
 */
public interface TraderIF extends Remote{
    void retrieveMsg(String message) throws RemoteException;
     String getID() throws RemoteException;
     void notifyCustomer(Item item, float price) throws RemoteException;
     void notitySeller(Item item, float price) throws RemoteException;
      
}
