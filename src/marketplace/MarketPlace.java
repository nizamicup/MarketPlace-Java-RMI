/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package marketplace;

import Trader.TraderIF;
import Bank.Bank;
import Bank.RejectedException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nizam
 */
public class MarketPlace extends UnicastRemoteObject
        implements MarketPlaceIF {

    String name;
    double price;
    Map<String, TraderIF> clients;// = new ArrayList[]();
    List<Item> items;
    List<Item> wishItems;
    //Item item;
    TraderIF client;
    Bank bank;
    static final String NAME = "rmi://localhost/market";

    public MarketPlace(String bankName) throws RemoteException, MalformedURLException {
        clients = new HashMap<String, TraderIF>();
        items = new ArrayList<Item>();
        wishItems = new ArrayList<Item>();
        try {
            bank = (Bank) Naming.lookup(bankName);
        } catch (Exception ex) {
            System.err.println("Error looking for the bank given the URL: "
                    + bankName);
            System.exit(1);
        }

        Naming.rebind(NAME, this);
        System.out.println("Server ready.");

    }

    @Override
    public synchronized void registerClient(TraderIF obj) throws RemoteException {
        clients.put(obj.getID(), obj);
        System.out.println("Client: " + obj + "is now registerd in the MarketPlace");
    }

    @Override
    public synchronized void unregisterClient(TraderIF obj) throws RemoteException {
        clients.remove(obj);
        System.out.println("Client: " + obj + "is now removed");
    }

    @Override
    public synchronized void sell(String name, float price, String clientId) throws RemoteException {

        Item item = new Item(name, price, clientId);
        items.add(item);
        System.out.println("Adding new item from " + clientId + " for sale: "
                + name + " - " + price + " SEK.");
         TraderIF owner = clients.get(clientId);
         owner.retrieveMsg("You have successfully added an item");
        try {
            manageWishes();
        } catch (RejectedException ex) {
            System.out.println("Error in managing wishes");
        }

    }

    @Override
    public synchronized boolean buy(String name, float price, String clientId) throws RemoteException {
        boolean result;
        Item item = findItem(name, price);
        if (item != null) {
            String sellerName = item.getClientId();
            try {
                result = makePurchase(item, sellerName, clientId);
                if(!result)
                {
                    TraderIF client=clients.get(clientId);
                    client.retrieveMsg("purchase failed for this item");
                }
            } catch (RejectedException ex) {
                Logger.getLogger(MarketPlace.class.getName()).log(Level.SEVERE, null, ex);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public synchronized void wish(String name, float price, String clientId) throws RemoteException {
        Item newWish = new Item(name, price, clientId);
        wishItems.add(newWish);

        System.out.println("Adding new wish from " + clientId + ": "
                + name + " - " + price + " SEK.");

        try {
            manageWishes();
        } catch (RejectedException ex) {
            System.out.println("Error managing wishes");
        }
    }

    // @Override 
    //public Map<TraderIF> getClients() { return clients; }
    public Item findItem(String itemName, float itemPrice) {
        for (Item item : items) {
            if (item.getName().equals(itemName) && item.getPrice() == itemPrice) {
                return item;
            }
        }
        return null;
    }

    public boolean makePurchase(Item item, String sellerName, String customerName) throws RejectedException, RemoteException {
        TraderIF seller = (TraderIF) clients.get(sellerName);
        TraderIF customer = (TraderIF) clients.get(customerName);

        if (customer == null || seller == null) {
            return false;
        } else if (customer.equals(seller)) {
            return false;
        } else {
            float price = item.getPrice();

            bank.getAccount(customerName).withdraw(price);
            bank.getAccount(sellerName).deposit(price);
            seller.notitySeller(item, price);
            customer.notifyCustomer(item, price);

            items.remove(item);
        }
        System.out.println("Purchase made for" + customerName);
        return true;
    }

    @Override
    public synchronized Map<String, TraderIF> getClients() throws RemoteException {
        return clients;
    }

    @Override
    public synchronized List<Item> getItems() throws RemoteException {
        return items;
    }

    public void manageWishes() throws RejectedException, RemoteException {
        TraderIF wishOwner = null;
        ArrayList<Item> newWishes = new ArrayList<Item>();
        for (int i = 0; i < wishItems.size(); i++) {
            Item newWish = wishItems.get(i);
            List<Item> availableItems = items;
            for (int j = 0; j < availableItems.size(); j++) {
                Item newItem = availableItems.get(j);
                if (newItem.getName().equals(newWish.getName())) {
                    if (newItem.getPrice() <= newWish.getPrice()) {
                        boolean result = makePurchase(newItem, newItem.getClientId(), newWish.getClientId());
                        wishOwner = clients.get(newWish.getClientId());
                        if (result) {
                            
                            wishOwner.notifyCustomer(newItem, newItem.getPrice());
                            newWishes.remove(newWish);
                            wishItems = newWishes;
                        } else {
                            wishOwner.retrieveMsg("You can not buy this item");
                        }

                    }
                }
            }
        }

    }

}
