/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package marketplace;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nizam
 */
public class Server{
    private static final int REGISTRY_PORT_NUMBER = 1099;
    private static final String BANK = "rmi://localhost/Nordea";
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws RemoteException, MalformedURLException{
       try {
            
            try {
                LocateRegistry.getRegistry(REGISTRY_PORT_NUMBER).list();
            } catch (RemoteException e) {
                LocateRegistry.createRegistry(REGISTRY_PORT_NUMBER);
            }
            
            //Naming.rebind("rmi://localhost/market", new MarketPlace());
           new MarketPlace(BANK);
        } catch (RemoteException re) {
            System.out.println(re);
            System.exit(1);
        }
       
    }
    
}
