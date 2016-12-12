/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Trader;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;
import marketplace.MarketPlaceIF;
import Bank.Bank;
import Bank.Account;

/**
 *
 * @author Nizam
 */
public class Client {

    private static String bankName="Nordea";
    private static int initialAcmoun=10000;
    public static void main(String[] args) throws NotBoundException, MalformedURLException, RemoteException {

        try {

            MarketPlaceIF marketPlace = (MarketPlaceIF) Naming.lookup("rmi://localhost/market");
            
            System.out.println("Enter your name");
            Scanner scanner = new Scanner(System.in);
            String message;
            message = scanner.next();

            Thread clientThread = new Thread(new Trader(message,marketPlace,bankName,initialAcmoun));
           clientThread.start();
          

        } catch (Exception e) {
            System.err.println("The runtime failed: " + e.getMessage());
            System.exit(0);
        }

    }
}
