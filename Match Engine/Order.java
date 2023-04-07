/******************************************************************************
 *  Compilation:  javac Order.java
 *  Execution:    java Order
 *
 *  Java 11 used (javac 11.0.18)
 *   
 ******************************************************************************/

import java.util.*; 

public class Order{ 

    //INSTANCE VARIABLES
    public final String side;
    public int quantity;

    //CONSTRUCTOR
    public Order(String s, int q){
        this.side = s;
        this.quantity = q;
    }

    //METHODS

    public int getQuantity(){
        return this.quantity;
    }

    public void setQuantity(int newQuantity){
        this.quantity = newQuantity;
    }

    public boolean isBuy(){
        return this.side.equals("buy");
    }

    public int boughtFromAListOfSellers(LinkedList<Order> listOfSellers){
        int amountNeededAtFirst = this.getQuantity();
        while (this.getQuantity() > 0 && listOfSellers.size() > 0){
            shareOneToOne(listOfSellers.peek());
            if (listOfSellers.peek().getQuantity() == 0) {listOfSellers.remove();}
        }
        return amountNeededAtFirst - this.getQuantity();
    }

    public void sellForAListOfBuyers(double priceInDeal, LinkedList<Order> listOfBuyers){
        int amountNeededBeforeShare = 0;
        int amountNeededNow = 0;
        while (this.getQuantity() > 0 && listOfBuyers.size() > 0){
            amountNeededBeforeShare = this.getQuantity();
            shareOneToOne(listOfBuyers.peek());
            amountNeededNow = this.getQuantity();
            if (listOfBuyers.peek().getQuantity() == 0) {listOfBuyers.remove();}
            if (amountNeededBeforeShare - amountNeededNow > 0) {
                System.out.println("Trade, price: " + priceInDeal + ", qty: "+ (amountNeededBeforeShare - amountNeededNow));
            }
        }
    }

    public void shareOneToOne(Order possibleMatch){
        int quantityNeeded = this.getQuantity();
        int quantityAsked = possibleMatch.getQuantity();
        if (quantityNeeded >= quantityAsked){
            this.setQuantity(quantityNeeded - quantityAsked);
            possibleMatch.setQuantity(0);
        }
        else{
            this.setQuantity(0);
            possibleMatch.setQuantity(quantityAsked - quantityNeeded);
        }
    }

    public void cleanZerosOnTheBook(double price, 
                                     PriorityQueue<Double> prices, 
                                     Hashtable<Double, LinkedList<Order>> listsOfOrdersPerPrice) {
        if(listsOfOrdersPerPrice.get(price).size() == 0){
            listsOfOrdersPerPrice.remove(price);
            prices.remove(price);
        }
    }

}