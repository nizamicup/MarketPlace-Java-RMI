/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package marketplace;

import java.io.Serializable;

/**
 *
 * @author Nizam
 */
public class Item implements Serializable{

    String name;
    float price;
    String ClientId;

    public Item(String name, float price, String ClientId) {
        this.name = name;
        this.price = price;
        this.ClientId = ClientId;
    }

    public String getName() {
        return name;
    }

    public float getPrice() {
        return price;
    }
    public String getClientId(){
       return ClientId;
    }

}
