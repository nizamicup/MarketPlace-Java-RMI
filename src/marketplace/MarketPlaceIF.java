/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package marketplace;

import Trader.TraderIF;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Nizam
 */
public interface MarketPlaceIF extends Remote {

    void registerClient(TraderIF obj) throws RemoteException;

    void unregisterClient(TraderIF obj) throws RemoteException;

    void sell(String name, float price, String clientId) throws RemoteException;

    boolean buy(String name, float price, String clientId) throws RemoteException;

    void wish(String item, float price, String clientId) throws RemoteException;
    
    Map<String,TraderIF> getClients() throws RemoteException;
    
    List<Item> getItems() throws RemoteException;
}
