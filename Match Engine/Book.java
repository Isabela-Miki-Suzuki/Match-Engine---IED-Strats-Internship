/******************************************************************************
 *  Compilation:  javac Book.java
 *  Execution:    java Book
 *
 *  Java 11 used (javac 11.0.18)
 *   
 ******************************************************************************/

import java.util.*;

public class Book {
    public Hashtable<Double, LinkedList<Order>> listsOfLimitBuyOrdersPerPrice;
    public Hashtable<Double, LinkedList<Order>> listsOfLimitSellOrdersPerPrice;
    public PriorityQueue<Double> pricesOfBuyOrdersInDescendingOrder;
    public PriorityQueue<Double> pricesOfSellOrdersInAscendingOrder;
    public LinkedList<Order> marketBuyOrdersInTheOrderTheyAppear;
    public LinkedList<Order> marketSellOrdersInTheOrderTheyAppear;
    public Hashtable<String, LimitOrder> orderCorrespondingToIdentifier;

    public Book() {
        this.listsOfLimitBuyOrdersPerPrice = new Hashtable<>();
        this.listsOfLimitSellOrdersPerPrice = new Hashtable<>();
        this.pricesOfBuyOrdersInDescendingOrder = new PriorityQueue<Double>(11, new InvertedComparator());
        this.pricesOfSellOrdersInAscendingOrder = new PriorityQueue<Double>();
        this.marketBuyOrdersInTheOrderTheyAppear = new LinkedList<>();
        this.marketSellOrdersInTheOrderTheyAppear = new LinkedList<>();
        this.orderCorrespondingToIdentifier = new Hashtable<>();
    }
}

class InvertedComparator implements Comparator<Double>
 {
     public int compare(Double d1, Double d2) 
     {
         return -d1.compareTo(d2);
     }
 }